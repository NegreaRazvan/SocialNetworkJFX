package Utils.observer;

import Utils.events.Event;

public interface Observer<E extends Event> {
    void update(E event);
}
