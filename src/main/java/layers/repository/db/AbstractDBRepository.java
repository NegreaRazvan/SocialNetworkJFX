package layers.repository.db;

import layers.domain.Entity;
import layers.repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractDBRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    protected String url;
    protected String user;
    protected String password;
    private String queryLoad;

    public AbstractDBRepository(String url, String user, String password, String queryLoad) {
        this.url=url;
        this.user=user;
        this.password=password;
        this.queryLoad=queryLoad;
    }


    public abstract E queryToEntity(ResultSet rs) throws SQLException;
    public abstract PreparedStatement entityToSaveStatement(Connection con,E entity) throws SQLException;
    public abstract PreparedStatement entityToDeleteStatement(Connection con,ID id) throws SQLException;
    public abstract ResultSet entityToFindStatement(Connection con,ID id) throws SQLException;

    public abstract PreparedStatement entityToUpdateStatement(Connection con,E entity) throws SQLException;

    @Override
    public Optional<E> save(E entity) {
        try (Connection conn = DriverManager.getConnection(url, user, password);) {
            PreparedStatement pstmt = entityToSaveStatement(conn, entity);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<E> findAll(){
        Set<E> entities = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(queryLoad);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                E entity = queryToEntity(rs);
                entities.add(entity);
            }
            return entities;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<ID> findAllIds(){
        Set<ID> ids = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(queryLoad);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                E entity = queryToEntity(rs);
                ids.add(entity.getId());
            }
            return ids;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Optional<E> delete(ID id) {
        try (Connection conn = DriverManager.getConnection(url, user, password);) {
            Optional<E> entity = findOne(id);
            PreparedStatement pstmt = entityToDeleteStatement(conn, id);
            pstmt.executeUpdate();
            return entity;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<E> update(E entity) {
        try (Connection conn = DriverManager.getConnection(url, user, password);) {
            PreparedStatement pstmt = entityToUpdateStatement(conn, entity);
            pstmt.executeUpdate();
            return Optional.empty();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<E> findOne(ID id){
        try (Connection conn = DriverManager.getConnection(url, user, password);) {
            ResultSet rs = entityToFindStatement(conn, id);
            E entity = null;
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

}