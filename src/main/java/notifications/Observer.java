package notifications;

/**
 * interface that has the required methods for an observer
 */
public interface Observer {
    /**
     * method used for receiving notifications
     * @param notification the notification being sent
     */
    void update(String notification);
}
