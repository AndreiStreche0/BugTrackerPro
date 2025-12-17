package notifications;

import java.util.ArrayList;
import java.util.List;

/**
 * class used to send notifications
 */
public abstract class Observers {
    private final List<Observer> observers = new ArrayList<>();

    /**
     * adds a new observer
     * @param observer the observer
     */
    public void addObserver(final Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * removes an observer
     * @param observer the observer
     */
    public void removeObserver(final Observer observer) {
        observers.remove(observer);
    }

    /**
     * sends notification too all the observers
     * @param message the message used for notification
     */
    public void sendNotifications(final String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
