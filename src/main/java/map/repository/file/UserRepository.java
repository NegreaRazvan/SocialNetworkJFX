package map.repository.file;

import map.domain.User;

public class UserRepository extends AbstractFileRepository<Long, User>{
    public UserRepository(String fileName) {
        super(fileName);
    }

    /**
     * transforms the line into a "user"
     * @param line - a string that is going to have the format "id;firstName;lastName"
     * @return the user
     */
    @Override
    public User lineToEntity(String line) {
        String[] splited = line.split(";");
        User u = new User(splited[1], splited[2],splited[3]);
        u.setId(Long.parseLong(splited[0]));
        return u;
    }

    /**
     * transforms the user into a valid line format
     * @param entity
     * @return line - a string that is going to have the format "id;firstName;lastName"
     */
    @Override
    public String entityToLine(User entity) {
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName();
    }
}
