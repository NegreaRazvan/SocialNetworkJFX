package map.domain;

import java.time.LocalDateTime;

public class Friend extends Entity<Long> {


    Tuple<Long> friendship;
    LocalDateTime date;

    public Friend(Long e1, Long e2) {
        friendship = new Tuple<>(e1, e2);
    }

    /**
     * returns the first member of the tuple
     * @return first member of the tuple
     */
    public Long first() {
        return friendship.first();
    }

    /**
     * @return the second member of the tuple
     */
    public Long second() {
        return friendship.second();
    }

    public LocalDateTime date() { return date; }

    @Override
    public String toString() {
        return "First: " + friendship.first() + ", Second: " + friendship.second() + ", Date: " + date;
    }

}
