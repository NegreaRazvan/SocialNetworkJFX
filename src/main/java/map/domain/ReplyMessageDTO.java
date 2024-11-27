package map.domain;

import java.time.LocalDateTime;

public class ReplyMessageDTO extends MessageDTO {
    String message;
    Long idReplyMessage;

    public ReplyMessageDTO(Long id, Long from, Long to, String message, LocalDateTime date, String msg, Long idReplyMessage) {
        super(id, from, to, message, date);
        this.message = msg;
        this.idReplyMessage = idReplyMessage;
    }

    public String getMsg() {
        return message;
    }

    public Long getIdReplyMessage() {
        return idReplyMessage;
    }


}
