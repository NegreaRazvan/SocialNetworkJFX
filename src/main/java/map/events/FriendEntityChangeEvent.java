package map.events;

import map.domain.Friend;

public class FriendEntityChangeEvent implements Event {
    private ChangeEventType type;
    private Friend friend, oldFriend;

    public FriendEntityChangeEvent(ChangeEventType type, Friend friend, Friend oldFriend) {
        this.type = type;
        this.friend = friend;
        this.oldFriend = oldFriend;
    }

    public FriendEntityChangeEvent(ChangeEventType type, Friend friend) {
        this.type = type;
        this.friend = friend;
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
}
