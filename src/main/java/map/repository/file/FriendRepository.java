package map.repository.file;

import map.domain.Friend;

import java.util.Date;

public class FriendRepository extends AbstractFileRepository<Long, Friend> {

    public FriendRepository(String fileName) {
        super(fileName);
    }

    /**
     * transforms the line into a "friend"
     * @param line - a string that is going to have the format "id;idUser;idFriend"
     * @return the friend
     */
    @Override
    public Friend lineToEntity(String line) {
        String[] splited = line.split(";");
        Friend f = new Friend(Long.parseLong(splited[1]), Long.parseLong(splited[2]),Boolean.parseBoolean(splited[3]));
        f.setId(Long.parseLong(splited[0]));
        return f;
    }

    /**
     * transforms the friend into a valid line format
     * @param entity
     * @return line - a string that is going to have the format "id;idUser;idFriend"
     */
    @Override
    public String entityToLine(Friend entity) {
        return entity.getId() + ";" + entity.first() + ";" + entity.second();
    }
}
