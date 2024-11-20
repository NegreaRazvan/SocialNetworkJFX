package map.observer;

import map.events.Event;

public interface Observer<E extends Event> {
    void update(E event);
}
