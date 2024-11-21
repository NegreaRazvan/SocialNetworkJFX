package map.repository.file;

import map.domain.User;

import java.util.Optional;

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
        Boolean isAdmin;
        if("true".equals(splited[5]))
            isAdmin = true;
        else isAdmin = false;
        User u = new User(splited[1], splited[2],splited[3],splited[4],isAdmin, 0);
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
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName() + ";" + entity.getPassword() + ";" + entity.getUsername() + ";" + entity.getIsAdmin();
    }
}
