package layers.repository.db;

import layers.domain.User;
import Utils.paging.Page;
import Utils.paging.Pageable;

import java.sql.*;
import java.util.*;

public class UserRepositoryDB extends AbstractDBRepository<Long, User> {

    public UserRepositoryDB(String url, String user, String password, String queryLoad) {
        super(url, user, password, queryLoad);
    }

    @Override
    public User queryToEntity(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String password = rs.getString("password");
        Boolean isAdmin = rs.getBoolean("admin");
        String username= rs.getString("username");
        Integer numberNotifications = rs.getInt("number_notifications");
        User user=new User(firstName, lastName, password, username, isAdmin, numberNotifications);
        user.setId(id);
        return user;
    }

    @Override
    public PreparedStatement entityToSaveStatement(Connection con, User entity) throws SQLException {
        String query = "INSERT INTO public.\"User\" (first_name, last_name, password, username, admin, number_notifications) VALUES (?, ?, ?,?,?, 0)";
        PreparedStatement ps = con.prepareStatement(query);{
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getPassword());
            ps.setString(4, entity.getUsername());
            ps.setBoolean(5, entity.getIsAdmin());
        }
        return ps;
    }

    @Override
    public PreparedStatement entityToDeleteStatement(Connection con, Long id) throws SQLException {
        String query = "DELETE FROM public.\"User\" WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);{
            ps.setLong(1, id);
        }
        return ps;
    }

    @Override
    public ResultSet entityToFindStatement(Connection con, Long id) throws SQLException {
        String query = "SELECT id, first_name, last_name, password, username, admin, number_notifications FROM public.\"User\" WHERE id = ?";
        ResultSet rs;
        PreparedStatement ps = con.prepareStatement(query);{
            ps.setLong(1, id);
            rs = ps.executeQuery();
        }
        return rs;
    }

    public ResultSet entityToFindStatement(Connection con,  String username) throws SQLException {
        String query = "SELECT id, first_name, last_name, password, username, admin, number_notifications FROM public.\"User\" WHERE username = ?";
        ResultSet rs;
        PreparedStatement ps = con.prepareStatement(query);{
            ps.setString(1, username);
            rs = ps.executeQuery();
        }
        return rs;
    }

    public ResultSet entityToFindStatement(Connection con,  String username, String password) throws SQLException {
        String query = "SELECT id, first_name, last_name, password, username, admin, number_notifications FROM public.\"User\" WHERE username = ? AND password = ?";
        ResultSet rs;
        PreparedStatement ps = con.prepareStatement(query);{
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
        }
        return rs;
    }


    public Optional<User> findOne(String username){
        try (Connection conn = DriverManager.getConnection(url, user, password);) {
            ResultSet rs = entityToFindStatement(conn, username);
            User entity = null;
            if(rs.next()) {
                entity = queryToEntity(rs);
            }
            return Optional.ofNullable(entity);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<User> findOne(String username, String pass){
        try (Connection conn = DriverManager.getConnection(url, user, password);) {
            ResultSet rs = entityToFindStatement(conn, username,pass);
            User entity = null;
            if(rs.next()) {
                entity = queryToEntity(rs);
            }
            return Optional.ofNullable(entity);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Iterable<User> findAll(Long userId){
        Set<User> entities = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            PreparedStatement pstmt = conn.prepareStatement("SELECT *\n" +
                    "FROM \"User\"\n" +
                    "WHERE id!=? AND id NOT IN (SELECT U.id\n" +
                    "    FROM \"User\" U\n" +
                    "    INNER JOIN public.\"Friendship\" F on U.id = F.friend_id OR U.id = F.user_id\n" +
                    "    WHERE (F.user_id = ? or F.friend_id = ?) AND U.id!=?\n" +
                    ")");
            {
                pstmt.setLong(1, userId);
                pstmt.setLong(2, userId);
                pstmt.setLong(3, userId);
                pstmt.setLong(4, userId);
                ResultSet rs = pstmt.executeQuery();
                {
                    while (rs.next()) {
                        User u= queryToEntity(rs);
                        entities.add(u);
                    }
                }
            }
            return entities;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Iterable<User> findAllUsersThatAreFriends(Long userId){
        Set<User> entities = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            PreparedStatement pstmt = conn.prepareStatement("SELECT U.id,U.last_name,U.first_name,U.password,U.username,U.admin, U.number_notifications\n" +
                    "    FROM \"User\" U\n" +
                    "    INNER JOIN public.\"Friendship\" F on U.id = F.friend_id OR U.id = F.user_id\n" +
                    "    WHERE (F.user_id = ? or F.friend_id = ?) AND U.id!=? AND F.request=false"
            );
            {
                pstmt.setLong(1, userId);
                pstmt.setLong(2, userId);
                pstmt.setLong(3, userId);
                ResultSet rs = pstmt.executeQuery();
                {
                    while (rs.next()) {
                        User u= queryToEntity(rs);
                        entities.add(u);
                    }
                }
            }
            return entities;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int countFriends(Long userId) {
        String  query = "SELECT Count(*) as count" +
                "    FROM \"User\" U\n" +
                "    INNER JOIN public.\"Friendship\" F on U.id = F.friend_id OR U.id = F.user_id\n" +
                "    WHERE (F.user_id = ? or F.friend_id = ?) AND U.id!=? AND F.request=false";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setLong(1, userId);
            pstmt.setLong(2, userId);
            pstmt.setLong(3, userId);
            ResultSet result = pstmt.executeQuery();
            int totalNumberOfMovies = 0;
            if (result.next()) {
                totalNumberOfMovies = result.getInt("count");
            }
            return totalNumberOfMovies;
        }
        catch (SQLException e){
                e.printStackTrace();
        }
        return 0;
    }

    public Page<User> findAllUsersThatAreFriends(Pageable pageable, Long userId){
        List<User> entities = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            PreparedStatement pstmt = conn.prepareStatement("SELECT U.id,U.last_name,U.first_name,U.password,U.username,U.admin, U.number_notifications\n" +
                    "    FROM \"User\" U\n" +
                    "    INNER JOIN public.\"Friendship\" F on U.id = F.friend_id OR U.id = F.user_id\n" +
                    "    WHERE (F.user_id = ? or F.friend_id = ?) AND U.id!=? AND F.request=false" +
                    " LIMIT ? OFFSET ? "
            );
            {
                pstmt.setLong(1, userId);
                pstmt.setLong(2, userId);
                pstmt.setLong(3, userId);
                pstmt.setInt(4, pageable.getPageSize());
                pstmt.setInt(5,pageable.getPageNumber()*pageable.getPageSize());
                ResultSet rs = pstmt.executeQuery();
                {
                    while (rs.next()) {
                        User u= queryToEntity(rs);
                        entities.add(u);
                    }
                }
            }
            return new Page<>(entities, 10);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Iterable<User> findAllFriendRequests(Long userId){
        Set<User> entities = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            PreparedStatement pstmt = conn.prepareStatement("SELECT U.id,U.last_name,U.first_name,U.password,U.username,U.admin,U.number_notifications\n" +
                    "    FROM \"User\" U\n" +
                    "    INNER JOIN public.\"Friendship\" F on U.id = F.friend_id or U.id = F.user_id\n" +
                    "    WHERE F.friend_id = ? AND U.id!=? AND F.request=true"
            );
            {
                pstmt.setLong(1, userId);
                pstmt.setLong(2, userId);
                ResultSet rs = pstmt.executeQuery();
                {
                    while (rs.next()) {
                        User u= queryToEntity(rs);
                        entities.add(u);
                    }
                }
            }
            return entities;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PreparedStatement entityToUpdateStatement(Connection con, User entity) throws SQLException {
        String query = "UPDATE public.\"User\" SET first_name = ?, last_name = ?, password = ?, username = ?, admin = ?, number_notifications = ? WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);{
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getPassword());
            ps.setString(4, entity.getUsername());
            ps.setBoolean(5, entity.getIsAdmin());
            ps.setInt(6, entity.getNumberOfNotifications());
            ps.setLong(7, entity.getId());
        }
        return ps;
    }



}