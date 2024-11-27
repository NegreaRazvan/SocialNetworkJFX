package map.events;

import map.domain.Friend;
import map.domain.MessageDTO;
import map.domain.User;

public class FriendEntityChangeEvent implements Event {
    private ChangeEventType type;
    private Friend friend, oldFriend;
    private User user, friendUser;
    MessageDTO message;

    public FriendEntityChangeEvent(ChangeEventType type, Friend friend, Friend oldFriend) {
        this.type = type;
        this.friend = friend;
        this.oldFriend = oldFriend;
    }

    public FriendEntityChangeEvent(ChangeEventType type, Friend friend) {
        this.type = type;
        this.friend = friend;
    }

    public FriendEntityChangeEvent(ChangeEventType type, User user, User friend) {
        this.type = type;
        this.user = user;
        this.friendUser = friend;
    }

    public FriendEntityChangeEvent(ChangeEventType type, MessageDTO message) {
        this.type = type;
        this.message = message;
    }


    public MessageDTO getMessage() {
        return message;
    }
    public ChangeEventType getType() {
        return type;
    }
    public Friend getFriend() {
        return friend;
    }
    public Friend getOldFriend() {
        return oldFriend;
    }

    public User getUser() {
        return user;
    }
    public User getFriendUser() {
        return friendUser;
    }
}
