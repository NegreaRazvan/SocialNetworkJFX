package map.repository.db;

import map.domain.Friend;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendRepositoryDB extends AbstractDBRepository<Long, Friend> {
    public FriendRepositoryDB(String url, String user, String password, String queryLoad) {
        super(url, user, password, queryLoad);
    }

    @Override
    public Friend queryToEntity(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        Long userId = rs.getLong("user_id");
        Long friendId = rs.getLong("friend_id");
        Boolean request =rs.getBoolean("request");
        Optional<Date> date = Optional.ofNullable(rs.getDate("date"));
        Friend friend = new Friend(userId, friendId, request);
        if(date.isPresent())
            friend.setDate(date.get());
        else
            friend.setDate(null);
        friend.setId(id);
        return friend;
    }

    @Override
    public PreparedStatement entityToSaveStatement(Connection con, Friend entity) throws SQLException {
        String query;
        PreparedStatement ps;
        if(!entity.request()) {
            query = "INSERT INTO public.\"Friendship\" (user_id, friend_id, request) VALUES (?, ?, ?)";
            ps = con.prepareStatement(query);
            ps.setLong(1, entity.first());
            ps.setLong(2, entity.second());
            ps.setBoolean(3, entity.request());
        }else{
            query = "INSERT INTO public.\"Friendship\" (user_id, friend_id, request, date) VALUES (?, ?, ?, ?)";
            ps = con.prepareStatement(query);
            ps.setLong(1, entity.first());
            ps.setLong(2, entity.second());
            ps.setBoolean(3, entity.request());
            ps.setDate(4, Date.valueOf(LocalDate.now()));
        }
        return ps;
    }

    @Override
    public PreparedStatement entityToDeleteStatement(Connection con, Long id) throws SQLException {
        String query = "DELETE FROM public.\"Friendship\" WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);{
            ps.setLong(1, id);
        }
        return ps;
    }

    @Override
    public ResultSet entityToFindStatement(Connection con, Long id) throws SQLException {
        String query = "SELECT id, user_id, friend_id, request, date FROM public.\"Friendship\" WHERE id = ?";
        ResultSet rs;
        PreparedStatement ps = con.prepareStatement(query);{
            ps.setLong(1, id);
            rs = ps.executeQuery();
        }
        return rs;
    }

    public ResultSet entityToFindStatement(Connection con, Long userId, Long friendId) throws SQLException {
        String query = "SELECT id, user_id, friend_id, request, date FROM public.\"Friendship\" WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?) ";
        ResultSet rs;
        PreparedStatement ps = con.prepareStatement(query);{
            ps.setLong(1, userId);
            ps.setLong(2, friendId);
            ps.setLong(3, friendId);
            ps.setLong(4, userId);
            rs = ps.executeQuery();
        }
        return rs;
    }

    public Optional<Friend> findOne(Long userId, Long friendId) {
        try (Connection conn = DriverManager.getConnection(url, user, password);) {
            ResultSet rs = entityToFindStatement(conn, userId, friendId);
            Friend entity = null;
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


    public Iterable<Long> findAll(Long userId){
        Set<Long> entities = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            PreparedStatement pstmt = conn.prepareStatement("SELECT id\n" +
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
                        Long id=rs.getLong("id");
                        entities.add(id);
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

    public Iterable<Long> findAllUsersThatAreFriends(Long userId){
        Set<Long> entities = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            PreparedStatement pstmt = conn.prepareStatement("SELECT U.id\n" +
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
                        Long id=rs.getLong("id");
                        entities.add(id);
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
    public PreparedStatement entityToUpdateStatement(Connection con, Friend entity) throws SQLException {
        String query;
        PreparedStatement ps;
        if(!entity.request()) {
            query = "UPDATE public.\"Friendship\" SET user_id = ?, friend_id = ?, request = ? WHERE id = ?";
            ps = con.prepareStatement(query);
            {
                ps.setLong(1, entity.first());
                ps.setLong(2, entity.second());
                ps.setBoolean(3, entity.request());
                ps.setLong(4, entity.getId());
            }
        }else{
            query = "UPDATE public.\"Friendship\" SET user_id = ?, friend_id = ?, request = ?, date = ? WHERE id = ?";
            ps = con.prepareStatement(query);{
                ps.setLong(1, entity.first());
                ps.setLong(2, entity.second());
                ps.setBoolean(3, entity.request());
                ps.setDate(4, Date.valueOf(LocalDate.now()));
                ps.setLong(5, entity.getId());
            }
        }
        return ps;
    }
}