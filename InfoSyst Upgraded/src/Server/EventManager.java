package Server;

import Server.EventListener;

public class EventManager {
    private EventListener subscriber;

    public EventManager(EventListener subscriber) {
        this.subscriber = subscriber;
    }

    public void notify(String eventType) {
        subscriber.update(eventType);
    }
}
