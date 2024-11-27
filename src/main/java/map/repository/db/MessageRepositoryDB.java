package map.repository.db;

import map.domain.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MessageRepositoryDB extends AbstractDBRepository<Long, MessageDTO> {
    public MessageRepositoryDB(String url, String user, String password, String queryLoad) {
        super(url, user, password, queryLoad);
    }

    @Override
    public MessageDTO queryToEntity(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        Long to = rs.getLong("to");
        Long from = rs.getLong("from");
        String message = rs.getString("message");
        LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();
        String idReply = rs.getString("idReplyMessage");
        Long idReplyMessage = rs.getLong("idOfTheReplyMessage");
        if(idReply == null)
            return new MessageDTO(id, from, to, message, date);
        else
            return new ReplyMessageDTO(id,from,to,message,date,idReply, idReplyMessage);
    }

    @Override
    public PreparedStatement entityToSaveStatement(Connection con, MessageDTO entity) throws SQLException {
        String query = "INSERT INTO public.\"Message\" (\"to\", \"from\", message, date, \"idReplyMessage\", \"idOfTheReplyMessage\") VALUES (?, ?, ?, ?, ?,?)";
        PreparedStatement ps = con.prepareStatement(query);{
            ps.setLong(1, entity.getTo());
            ps.setLong(2, entity.getFrom());
            ps.setString(3, entity.getMessage());
            ps.setTimestamp(4, Timestamp.valueOf( entity.getDate()));
            if(entity instanceof ReplyMessageDTO) {
                ps.setString(5, ((ReplyMessageDTO) entity).getMsg());
                ps.setLong(6, ((ReplyMessageDTO) entity).getIdReplyMessage());
            }
            else {
                ps.setNull(5, Types.NULL);
                ps.setNull(6, Types.NULL);
            }

        }
        return ps;
    }

    @Override
    public PreparedStatement entityToDeleteStatement(Connection con, Long aLong) throws SQLException {
        return null;
    }

    @Override
    public ResultSet entityToFindStatement(Connection con, Long aLong) throws SQLException {
        String query = "SELECT id, \"to\", \"from\", message, date, \"idReplyMessage\", \"idOfTheReplyMessage\" FROM public.\"Message\" WHERE id=? ";
        ResultSet rs;
        PreparedStatement ps = con.prepareStatement(query);{
            ps.setLong(1, aLong);
            rs = ps.executeQuery();
        }
        return rs;
    }



    public ResultSet entityToFindStatement(Connection con, String query,Long userId) throws SQLException {
        ResultSet rs;
        PreparedStatement ps = con.prepareStatement(query);{
            ps.setLong(1, userId);
            rs = ps.executeQuery();
        }
        return rs;
    }

//    public Optional<MessageDTO> findOne(Long from,Long to) {
//        try (Connection conn = DriverManager.getConnection(url, user, password);) {
//            ResultSet rs;
//            if(to==null)
//                rs=entityToFindStatement(conn, "SELECT id, \"to\", \"from\", message, date, \"idReplyMessage\" FROM public.\"Message\" WHERE \"from\" = ? ", from);
//            else
//                rs=entityToFindStatement(conn, "SELECT id, \"to\", \"from\", message, date, \"idReplyMessage\" FROM public.\"Message\" WHERE \"to\" = ? ", to);
//            MessageDTO entity = null;
//            if(rs.next()) {
//                entity = queryToEntity(rs);
//            }
//            return Optional.ofNullable(entity);
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return Optional.empty();
//    }

    @Override
    public PreparedStatement entityToUpdateStatement(Connection con, MessageDTO entity) throws SQLException {
        return null;
    }

    public Iterable<MessageDTO> findAll(Long from, Long to){
        Set<MessageDTO> entities = new HashSet<>();
        String query;
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            query = "SELECT id, \"to\", \"from\", message, date, \"idReplyMessage\", \"idOfTheReplyMessage\" FROM public.\"Message\" WHERE (\"from\" = ? AND \"to\" = ? ) OR (\"from\" = ? AND \"to\" = ? )";
            PreparedStatement pstmt = conn.prepareStatement(query);
            {
                pstmt.setLong(1, from);
                pstmt.setLong(2, to);
                pstmt.setLong(3, to);
                pstmt.setLong(4, from);
                ResultSet rs = pstmt.executeQuery();
                {
                    while (rs.next()) {
                        MessageDTO u= queryToEntity(rs);
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
}
