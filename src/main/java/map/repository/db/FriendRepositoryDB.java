package map.repository.db;

import map.domain.Friend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendRepositoryDB extends AbstractDBRepository<Long, Friend> {
    public FriendRepositoryDB(String url, String user, String password, String queryLoad) {
        super(url, user, password, queryLoad);
    }

    @Override
    public Friend queryToEntity(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        Long userId = rs.getLong("user_id");
        Long friendId = rs.getLong("friend_id");
        Friend friend = new Friend(userId, friendId);
        friend.setId(id);
        return friend;
    }

    @Override
    public PreparedStatement entityToSaveStatement(Connection con, Friend entity) throws SQLException {
        String query = "INSERT INTO public.\"Friendship\" (user_id, friend_id) VALUES (?, ?)";
        PreparedStatement ps = con.prepareStatement(query);{
            ps.setLong(1, entity.first());
            ps.setLong(2, entity.second());
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
        String query = "SELECT id, user_id, friend_id FROM public.\"Friendship\" WHERE id = ?";
        ResultSet rs;
        PreparedStatement ps = con.prepareStatement(query);{
            ps.setLong(1, id);
            rs = ps.executeQuery();
        }
        return rs;
    }


    @Override
    public PreparedStatement entityToUpdateStatement(Connection con, Friend entity) throws SQLException {
        String query = "UPDATE public.\"Friendship\" SET user_id = ?, friend_id = ? WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);{
            ps.setLong(1, entity.first());
            ps.setLong(2, entity.second());
            ps.setLong(3, entity.getId());
        }
        return ps;
    }
}
