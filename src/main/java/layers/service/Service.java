package layers.service;

import Utils.PasswordHashing;
import layers.domain.*;
import layers.domain.validators.UsernameUpperCaseException;
import layers.domain.validators.ValidationException;
import layers.domain.validators.Validator;
import Utils.events.ChangeEventType;
import Utils.events.FriendEntityChangeEvent;
import Utils.observer.Observable;
import Utils.observer.Observer;
import Utils.paging.Page;
import Utils.paging.Pageable;
import layers.repository.Repository;
import layers.repository.db.FriendRepositoryDB;
import layers.repository.db.MessageRepositoryDB;
import layers.repository.db.UserRepositoryDB;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

public class Service implements ServiceInterface, Observable<FriendEntityChangeEvent> {

    private Validator<User> userValidator;
    private Repository<Long, MessageDTO> messageRepository;
    private Repository<Long,User> userRepository;
    private Repository<Long,Friend> friendRepository;
    private List<Observer<FriendEntityChangeEvent>> observers=new ArrayList<>();

    public Service(Validator<User> userValidator, Repository<Long, User> userRepository, Repository<Long, Friend> friendRepository, Repository<Long,MessageDTO> messageRepository) {
        this.userValidator = userValidator;
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public Optional<User> findOneUser(Long id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null");
        return userRepository.findOne(id);
    }

    public Optional<User> findOneUser(String username) {
        Optional.ofNullable(username).orElseThrow(() -> new IllegalArgumentException("Username must be not null"));
        return ((UserRepositoryDB)userRepository).findOne(username);
    }

    public Optional<User> findOneUser(String username,String password) {
        Optional.ofNullable(username).orElseThrow(() -> new IllegalArgumentException("Username must be not null"));
        Optional.ofNullable(password).orElseThrow(() -> new IllegalArgumentException("Password must be not null"));
        return ((UserRepositoryDB)userRepository).findOne(username,password);
    }

    @Override
    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> saveUser(String firstName, String lastName, String password, String username) throws ValidationException, NoSuchAlgorithmException {
        Optional.ofNullable(firstName).orElseThrow(() -> new IllegalArgumentException("firstName must be not null"));
        Optional.ofNullable(lastName).orElseThrow(() -> new IllegalArgumentException("lastName must be not null"));
        Optional.ofNullable(password).orElseThrow(() -> new IllegalArgumentException("Password must be not null"));
        Optional.ofNullable(username).orElseThrow(() -> new IllegalArgumentException("Username must be not null"));
        User entity = new User(firstName, lastName,password, username, false, 0, null);
        try{
            userValidator.validate(entity);
        }catch (UsernameUpperCaseException e){
            entity= new User(firstName, lastName, password, e.getMessage(), false, 0, null);

        }

        entity.setPassword(PasswordHashing.hashPassword(entity.getPassword()));

        userRepository.save(entity);
        return Optional.empty();
    }


    public void acceptFriend(Long userId, Long friendId) {
        Friend friendship=((FriendRepositoryDB)friendRepository).findOne(userId,friendId).get();
        friendship.setRequest(false);
        friendRepository.update(friendship);
        notifyObservers(new FriendEntityChangeEvent(ChangeEventType.ADD, friendship));
    }

    @Override
    public Optional<User> deleteUser(Long id) {
        Optional.ofNullable(id).orElseThrow(() -> new IllegalArgumentException("id must be not null"));
        return userRepository.delete(id);
    }

    @Override
    public User updateUser(Long id, String firstName, String lastName, String password, String username, Boolean isAdmin, Integer numberOfNotifications, String profilePicture,Boolean passwordReset) throws ValidationException, NoSuchAlgorithmException {
        Optional.ofNullable(firstName).orElseThrow(() -> new IllegalArgumentException("firstName must be not null"));
        Optional.ofNullable(lastName).orElseThrow(() -> new IllegalArgumentException("lastName must be not null"));
        Optional.ofNullable(password).orElseThrow(() -> new IllegalArgumentException("Password must be not null"));
        Optional.ofNullable(username).orElseThrow(() -> new IllegalArgumentException("Username must be not null"));

        User entity = new User(firstName, lastName, password,username, isAdmin, numberOfNotifications, profilePicture);

        if(!passwordReset)
            try{
                userValidator.validate(entity);
            }catch (UsernameUpperCaseException e){
                entity= new User(firstName, lastName, password, e.getMessage(), isAdmin, numberOfNotifications, profilePicture);}

        else entity.setPassword(PasswordHashing.hashPassword(password));

        entity.setId(id);

        if(((UserRepositoryDB)userRepository).findOne(username).isEmpty())
            throw new ValidationException("User not found");

        if (userRepository.findOne(entity.getId()).isPresent()) {
            userRepository.update(entity);
            return null;
        }else
            throw new ValidationException("User not found");
    }

    ///TO DO: CHANGE THE NAME
    public Iterable<User> findAllNonFriendsOfUser(Long idUser) {
        Optional.ofNullable(idUser).orElseThrow(() -> new IllegalArgumentException("idUser must be not null"));
        return ((UserRepositoryDB)userRepository).findAll(idUser);
    }

    public Iterable<MessageDTO> findAllSentMessages(Long from, Long to){
        Optional.ofNullable(from).orElseThrow(() -> new IllegalArgumentException("idUser must be not null"));
        Optional.ofNullable(to).orElseThrow(() -> new IllegalArgumentException("idUser must be not null"));
        return ((MessageRepositoryDB)messageRepository).findAll(from,to);
    }

    public Iterable<User> findAllFriendsOfTheUser(Long idUser) {
        Optional.ofNullable(idUser).orElseThrow(() -> new IllegalArgumentException("idUser must be not null"));
        return ((UserRepositoryDB)userRepository).findAllUsersThatAreFriends(idUser);
    }

    public int countFriends(Long idUser) {
        Optional.ofNullable(idUser).orElseThrow(() -> new IllegalArgumentException("idUser must be not null"));
        return ((UserRepositoryDB)userRepository).countFriends(idUser);
    }

    public Page<User> findAllFriendsOfTheUser(Pageable pageable, Long idUser) {
        Optional.ofNullable(idUser).orElseThrow(() -> new IllegalArgumentException("idUser must be not null"));
        return ((UserRepositoryDB)userRepository).findAllUsersThatAreFriends(pageable,idUser);
    }

    public Iterable<User> findAllFriendRequestsOfTheUser(Long idUser) {
        Optional.ofNullable(idUser).orElseThrow(() -> new IllegalArgumentException("idUser must be not null"));
        return ((UserRepositoryDB)userRepository).findAllFriendRequests(idUser);
    }

    public Optional<Friend> findOneFriend(Long idUser, Long idFriend) {
        Optional.ofNullable(idUser).orElseThrow(() -> new IllegalArgumentException("idUser must be not null"));
        Optional.ofNullable(idFriend).orElseThrow(() -> new IllegalArgumentException("idFriend must be not null"));
        return ((FriendRepositoryDB)friendRepository).findOne(idUser,idFriend);
    }

    private boolean findFriend(Friend friend) {
        for(var f: friendRepository.findAll()){
            if((f.first().equals(friend.second())&&f.second().equals(friend.first()))||(f.first().equals(friend.first())&&f.second().equals(friend.second())))
                return true;
        }
        return false;
    }

    private boolean isFriendLinkAlreadyInRepo(Friend friend) {
        return findFriend(friend);
    }

    @Override
    public Optional<Friend> saveFriend(Long userId, Long friendId, Boolean request) throws ValidationException {
        Optional.ofNullable(userRepository.findOne(userId)).orElseThrow(() -> new ValidationException("First User not found"));
        Optional.ofNullable(userRepository.findOne(friendId)).orElseThrow(() -> new ValidationException("Second User not found"));
        Friend entity = new Friend(userId, friendId, request);
        friendRepository.save(entity);
        notifyObservers(new FriendEntityChangeEvent(ChangeEventType.REQUEST, entity));
        return Optional.empty();

    }

    @Override
    public Optional<Friend> deleteFriend(Long id, ChangeEventType event) {
        Optional.ofNullable(id).orElseThrow(() -> new ValidationException("id must be not null"));
        Optional<Friend> friend=friendRepository.delete(id);
        notifyObservers(new FriendEntityChangeEvent(event, friend.get()));
        return friend;
    }

    @Override
    public Iterable<Friend> findAllFriends(){
        return friendRepository.findAll();
    }

    ///from a previous version which used these
    private Graph graphSetup(){
        var users=userRepository.findAll();
        int c=0;
        for(var user : users)
            c++;
        Graph graph = new Graph(c);
        for(var friend : friendRepository.findAll())
            graph.addEdge(Math.toIntExact(friend.first()),Math.toIntExact(friend.second()));
        return graph;
    }


    @Override
    public int numberOfConnectedComponents() {
        Graph graph=graphSetup();
        graph.DFScomp();
        return graph.ConnectedComponents();
    }

    @Override
    public ArrayList<Optional<User>> longestPath() {
        Graph graph=graphSetup();
        ArrayList<Integer> path=graph.DFSLongest();
        ArrayList<Optional<User>>users =new ArrayList<Optional<User>>();
        path.forEach(x->users.add((userRepository.findOne(Integer.toUnsignedLong(x)))));
        return users;
    }
    ///

    @Override
    public Optional<MessageDTO> saveMessage(Long to, Long from, String message, String idReplyMessage,Long idOfTheReplyMessage) {
        Optional.ofNullable(userRepository.findOne(to)).orElseThrow(() -> new ValidationException("User id not found"));
        Optional.ofNullable(userRepository.findOne(from)).orElseThrow(() -> new ValidationException("User not found"));
        MessageDTO messageDTO;
        if(idReplyMessage==null)
            messageDTO=new MessageDTO(null, from,to, message, LocalDateTime.now());
        else
            messageDTO=new ReplyMessageDTO(null, from, to ,message, LocalDateTime.now(), idReplyMessage, idOfTheReplyMessage);
        messageRepository.save(messageDTO);
        notifyObservers(new FriendEntityChangeEvent(ChangeEventType.MESSAGESENT, messageDTO));
        return Optional.empty();
    }


    @Override
    public Optional<MessageDTO> findOneMessage(Long to, Long from) {
        return Optional.empty();
    }

    @Override
    public Optional<MessageDTO> findOneMessage(Long id){
        return messageRepository.findOne(id);
    }

    @Override
    public void addObserver(Observer<FriendEntityChangeEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<FriendEntityChangeEvent> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(FriendEntityChangeEvent event) {
        observers.forEach(observer -> observer.update(event));
    }
}
