package map.domain;

import java.sql.Date;
import java.time.LocalDateTime;

public class Friend extends Entity<Long> {


    Tuple<Long> friendship;
    LocalDateTime date;
    Boolean request;

    public Friend(Long e1, Long e2, Boolean request) {
        friendship = new Tuple<>(e1, e2);
        this.request = request;
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

    public void setDate(LocalDateTime date) { this.date = date; }


    public Boolean request() { return request; }

    public void setRequest(Boolean request) { this.request = request; }


    @Override
    public String toString() {
        return "First: " + friendship.first() + ", Second: " + friendship.second() + ", Date: " + date;
    }

}
