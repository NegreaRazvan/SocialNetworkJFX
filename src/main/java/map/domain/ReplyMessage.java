package map.domain;

import java.time.LocalDateTime;
import java.util.List;

public class ReplyMessage extends Message {
    String message;
    public ReplyMessage(Long id, User from, List<User> to, String messageDesc, LocalDateTime date, String message) {
        super(id, from, to, messageDesc, date);
        this.message = message;
    }

    public String  getMsg(){return message;}
}
