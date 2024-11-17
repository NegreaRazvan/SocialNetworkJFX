package map.repository.file;

import map.domain.Entity;
import map.repository.memory.InMemoryRepository;

import java.io.*;
import java.util.Optional;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E>{
    private String filename;

    public AbstractFileRepository(String fileName) {
        filename=fileName;
        loadData();
    }

    /**
     *
     * @param line
     * @return
     */
    public abstract E lineToEntity(String line);
    public abstract String entityToLine(E entity);

    @Override
    public Optional<E> save(E entity) {
        Optional<E> e = super.save(entity);
        if (e.isPresent())
            writeToFile(entity);
        return e;
    }

    /**
     * writes to file all the entities
     */
    private void writeToFile() {
        try  ( BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            for (E entity: entities.values()) {
                String ent = entityToLine(entity);
                writer.write(ent);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * writes to file a single entity
     * @param entity - the entity that will be written to file
     */
    private void writeToFile(E entity) {
        try  ( BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))){
            writer.write(entityToLine(entity));
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadData() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                E entity = lineToEntity(line);
                super.save(entity);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Optional<E> delete(ID id) {
        Optional<E> entity = super.delete(id);
        if (entity.isPresent())
            writeToFile();
        return entity;
    }

    @Override
    public Optional<E> update(E entity) {
        Optional<E> en = super.update(entity);
        if (en.isPresent())
            writeToFile();
        return en;
    }
}
