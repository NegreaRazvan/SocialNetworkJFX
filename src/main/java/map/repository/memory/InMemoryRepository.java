package map.repository.memory;

import map.domain.Entity;
import map.repository.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    protected Map<ID,E> entities;

    public InMemoryRepository() {
        entities = new HashMap<>();
    }

    @Override
    public Optional<E> findOne(ID id) {
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Optional<E> save(E entity) {
        return Optional.ofNullable(entities.put(entity.getId(), entity));
    }

    @Override
    public Optional<E> delete(ID id) {
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<E> update(E entity) {
        return Optional.ofNullable(entities.put(entity.getId(), entity));
    }

    @Override
    public Iterable<ID> findAllIds() {
        return entities.keySet();
    }
}