package filters;

/**
 * interface used for filtering logic
 * @param <T> parameter that could be a Ticket or a Developer
 */
public interface Filter <T> {
    /**
     * @param item ticket or developer that the filter will be applied on
     * @return true if the item passes the filter
     */
    boolean filter(T item);
}
