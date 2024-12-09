package layers.service;

import layers.domain.Friend;
import layers.domain.MessageDTO;
import layers.domain.User;
import layers.domain.validators.ValidationException;
import Utils.events.ChangeEventType;

import java.util.ArrayList;
import java.util.Optional;

public interface ServiceInterface{
    /**
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    Optional<User> findOneUser(Long id);

    Optional<User> findOneUser(String username);

    /**
     *
     * @return all entities
     */
    Iterable<User> findAllUsers();

    /**
     * @param firstName -the first name of the user, must not be null
     * @param lastName  - the last name of the user, must not be null
     * @return null- if the given entity is saved
     * otherwise returns the entity (id already exists)
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.     *
     */
    Optional<User> saveUser(String firstName, String lastName, String password,String username) throws ValidationException;


    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException if the given id is null.
     */
    Optional<User> deleteUser(Long id);

    /**
     *
     * @param id -the id of the entity to be updated
     * @param firstName -the first name of the user, must not be null
     * @param lastName - the last name of the user, must not be null
     *
     * @return null - if the entity is updated,
     *                otherwise  returns the entity  - (User.g id does not exist).
     * @throws IllegalArgumentException
     *             if the given entity is null.
     * @throws ValidationException
     *             if the entity is not valid.
     */
    User updateUser(Long id, String firstName,String lastName,String password, String username, Boolean isAdmin, Integer numberOfNotifications);

    /**
     * @param userId   - the id of the user
     * @param friendId - the id of the friend
     * @return null- if the given entity is saved
     * otherwise returns the entity (id already exists)
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.     *
     */
    Optional<Friend> saveFriend(Long userId, Long friendId, Boolean request);

    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException if the given id is null.
     */
    Optional<Friend> deleteFriend(Long id, ChangeEventType event);

    /**
     *
     * @return all entities
     */
    Iterable<Friend> findAllFriends();

    /**
     * @return the number of connected components the social network has
     */
    int numberOfConnectedComponents();

    /**
     * @return the vertices that make up the longest path
     */
    ArrayList<Optional<User>> longestPath();

    public Optional<MessageDTO> saveMessage(Long to, Long from, String message, String idReplyMessage,Long idOfTheReplyMessage);

    public Optional<MessageDTO> findOneMessage(Long to, Long from);

    public Optional<MessageDTO> findOneMessage(Long id);
}
