package map.domain;

import java.time.LocalDateTime;

public class ReplyMessageDTO extends MessageDTO {
    String message;

    public ReplyMessageDTO(Long id, Long from, Long to, String message, LocalDateTime date, String msg) {
        super(id, from, to, message, date);
        this.message = msg;
    }

    public String getMsg() {
        return message;
    }
}
