package map.domain;

import java.time.LocalDateTime;
import java.util.List;

public class MessageDTO extends Entity<Long>{
    Long from;
    Long to;
    String message;
    LocalDateTime date;

    public MessageDTO(Long id, Long from, Long to, String message, LocalDateTime date) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        setId(id);
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
