package map.repository.db;

import map.domain.Friend;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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
