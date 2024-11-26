package map.service;

import map.domain.*;
import map.domain.validators.UsernameUpperCaseException;
import map.domain.validators.ValidationException;
import map.domain.validators.Validator;
import map.events.ChangeEventType;
import map.events.FriendEntityChangeEvent;
import map.observer.Observable;
import map.observer.Observer;
import map.repository.Repository;
import map.repository.db.AbstractDBRepository;
import map.repository.db.FriendRepositoryDB;
import map.repository.db.UserRepositoryDB;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.StreamSupport;

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
    public Optional<User> saveUser(String firstName, String lastName, String password, String username) throws ValidationException {
        Optional.ofNullable(firstName).orElseThrow(() -> new IllegalArgumentException("firstName must be not null"));
        Optional.ofNullable(lastName).orElseThrow(() -> new IllegalArgumentException("lastName must be not null"));
        Optional.ofNullable(password).orElseThrow(() -> new IllegalArgumentException("Password must be not null"));
        Optional.ofNullable(username).orElseThrow(() -> new IllegalArgumentException("Username must be not null"));
        User entity = new User(firstName, lastName,password, username, false, 0);
        try{
            userValidator.validate(entity);
        }catch (UsernameUpperCaseException e){
            entity= new User(firstName, lastName, password, e.getMessage(), false, 0);

        }
        entity.setId(null);
        Optional<User> oldEntity = userRepository.findOne(entity.getId());

        if (oldEntity.isPresent()) {
            return oldEntity;
        } else {
            userRepository.save(entity);

            return Optional.empty();
        }
    }

    public void acceptFriend(Long userId, Long friendId) {
        Friend friendship=((FriendRepositoryDB)friendRepository).findOne(userId,friendId).get();
        friendship.setRequest(false);
        friendRepository.update(friendship);
        notifyObservers(new FriendEntityChangeEvent(ChangeEventType.ADD, friendship));
    }

    private void updateRepoFriends(Long id){
        Iterable<Friend> friends = friendRepository.findAll();
        ArrayList<Friend> friend1 = new ArrayList<>();
        friends.forEach(x -> {
            Friend f=new Friend(x.first(), x.second(),x.request());
            f.setId(x.getId());
            friend1.add(f);
        });
        friend1.forEach(f-> {
            if (id.equals(f.first()) || id.equals(f.second()))
                friendRepository.delete(f.getId());
        });
    }

    @Override
    public Optional<User> deleteUser(Long id) {
        Optional.ofNullable(id).orElseThrow(() -> new IllegalArgumentException("id must be not null"));
        if(!(userRepository instanceof AbstractDBRepository<Long, User>))
            updateRepoFriends(id);

        return userRepository.delete(id);
    }

    @Override
    public User updateUser(Long id, String firstName, String lastName, String password, String username, Boolean isAdmin, Integer numberOfNotifications) throws ValidationException {
        Optional.ofNullable(firstName).orElseThrow(() -> new IllegalArgumentException("firstName must be not null"));
        Optional.ofNullable(lastName).orElseThrow(() -> new IllegalArgumentException("lastName must be not null"));
        Optional.ofNullable(password).orElseThrow(() -> new IllegalArgumentException("Password must be not null"));
        Optional.ofNullable(username).orElseThrow(() -> new IllegalArgumentException("Username must be not null"));
        User entity = new User(firstName, lastName, password,username, isAdmin, numberOfNotifications);
        try{
            userValidator.validate(entity);
        }catch (UsernameUpperCaseException e){
            entity= new User(firstName, lastName, password, e.getMessage(), isAdmin, numberOfNotifications );
        }
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
    public Iterable<User> findAllFriendsOfAUser(Long idUser) {
        Optional.ofNullable(idUser).orElseThrow(() -> new IllegalArgumentException("idUser must be not null"));
        return ((UserRepositoryDB)userRepository).findAll(idUser);
    }

    public Iterable<User> findAllFriendsOfTheUser(Long idUser) {
        Optional.ofNullable(idUser).orElseThrow(() -> new IllegalArgumentException("idUser must be not null"));
        return ((UserRepositoryDB)userRepository).findAllUsersThatAreFriends(idUser);
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
        entity.setId(null);
        if(isFriendLinkAlreadyInRepo(entity))
            throw new ValidationException("Friend link already exists");
        Optional<Friend> oldEntity = friendRepository.findOne(entity.getId());
        if (oldEntity.isPresent()) {
            return oldEntity;
        } else {
            friendRepository.save(entity);
            notifyObservers(new FriendEntityChangeEvent(ChangeEventType.REQUEST, entity));
//            calculateId();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Friend> deleteFriend(Long id, ChangeEventType event) {
        Optional.ofNullable(id).orElseThrow(() -> new ValidationException("id must be not null"));
//        calculateId();
        Optional<Friend> friend=friendRepository.delete(id);
        notifyObservers(new FriendEntityChangeEvent(event, friend.get()));
        return friend;
    }

    @Override
    public Iterable<Friend> findAllFriends(){
        return friendRepository.findAll();
    }

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

    @Override
    public Optional<MessageDTO> saveMessage(Long to, Long from, String message, String idReplyMessage) {
        Optional.ofNullable(userRepository.findOne(to)).orElseThrow(() -> new ValidationException("User id not found"));
        Optional.ofNullable(userRepository.findOne(from)).orElseThrow(() -> new ValidationException("User not found"));
        MessageDTO messageDTO;
        if(idReplyMessage==null)
            messageDTO=new MessageDTO(null, from,to, message, LocalDateTime.now());
        else
            messageDTO=new ReplyMessageDTO(null, from, to ,message, LocalDateTime.now(), idReplyMessage);
        messageRepository.save(messageDTO);
        notifyObservers(new FriendEntityChangeEvent(ChangeEventType.MESSAGESENT, messageDTO));
        return Optional.empty();
    }


    @Override
    public Optional<MessageDTO> findOneMessage(Long to, Long from) {
        return Optional.empty();
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
