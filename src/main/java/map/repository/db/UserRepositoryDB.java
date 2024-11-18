package map.repository.db;

import map.domain.User;

import java.sql.*;
import java.util.Optional;

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
        User user=new User(firstName, lastName, password, username, isAdmin);
        user.setId(id);
        return user;
    }

    @Override
    public PreparedStatement entityToSaveStatement(Connection con, User entity) throws SQLException {
        String query = "INSERT INTO public.\"User\" (first_name, last_name, password, username, admin) VALUES (?, ?, ?,?,?)";
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
        String query = "SELECT id, first_name, last_name, password, username, admin FROM public.\"User\" WHERE id = ?";
        ResultSet rs;
        PreparedStatement ps = con.prepareStatement(query);{
            ps.setLong(1, id);
            rs = ps.executeQuery();
        }
        return rs;
    }

    public ResultSet entityToFindStatement(Connection con,  String username) throws SQLException {
        String query = "SELECT id, first_name, last_name, password, username, admin FROM public.\"User\" WHERE username = ?";
        ResultSet rs;
        PreparedStatement ps = con.prepareStatement(query);{
            ps.setString(1, username);
            rs = ps.executeQuery();
        }
        return rs;
    }

    public ResultSet entityToFindStatement(Connection con,  String username, String password) throws SQLException {
        String query = "SELECT id, first_name, last_name, password, username, admin FROM public.\"User\" WHERE username = ? AND password = ?";
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

    @Override
    public PreparedStatement entityToUpdateStatement(Connection con, User entity) throws SQLException {
        String query = "UPDATE public.\"User\" SET first_name = ?, last_name = ?, password = ?, username = ?, admin = ? WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);{
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getPassword());
            ps.setString(4, entity.getUsername());
            ps.setBoolean(5, entity.getIsAdmin());
            ps.setLong(6, entity.getId());
        }
        return ps;
    }



}