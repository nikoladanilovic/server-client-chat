package enea.dgs.chat;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Singleton class representing an observable notifying attached observers about changes in its
 * state.
 */
public class ClientMessageObservable {

    private static final ClientMessageObservable INSTANCE = new ClientMessageObservable();
    private final Set<IClientMessageObserver> observers = new HashSet<>();

    /**
     * Constructor instantiating observable support.
     */
    private ClientMessageObservable() { }

    /**
     * Returns singleton instance.
     *
     * @return {@link ClientMessageObservable}
     */
    public static ClientMessageObservable getInstance() {
        return INSTANCE;
    }

    /**
     * Attaches the provided listener on the observable bus. Will be notified when an event comes
     * matching the provided key.
     * <p>
     * The same listener object may be added more than once. For each key, the listener will be
     * invoked the number of times it was added for that key. If key or listener is {@code null}, no
     * exception is thrown and no action is taken.
     *
     * @param observer {@link IClientMessageObserver} instance
     */
    public void addObserver(final IClientMessageObserver observer) {
        observers.add(observer);
    }

    /**
     * Remove the provided listener from the observable bus.
     *
     * @param observer {@link IClientMessageObserver} instance
     */
    public void removeObserver(final IClientMessageObserver observer) {
        observers.remove(observer);
    }

    /**
     * Triggers broadcast to every attached observer (client).
     * <p>
     * If any of the provided parameters is {@code null}, firing of filed event will not be triggered.
     *
     * @param clientIdentifier {@link String} instance
     * @param message {@link String} instance
     */
    public synchronized void broadcastMessageFrom(final String clientIdentifier, final String message) {
        if (nonNullAndNonEmpty(clientIdentifier) && nonNullAndNonEmpty(message))
            observers.forEach(observer -> observer.onMessageReceived(clientIdentifier, message));
    }

    /**
     * Returns whether the provided String value is null or empty.
     *
     * @param value {@link String} instance
     * @return {@code boolean} flag
     */
    public static boolean nonNullAndNonEmpty(final String value) {
        return !Objects.isNull(value) && !value.isEmpty();
    }

}
