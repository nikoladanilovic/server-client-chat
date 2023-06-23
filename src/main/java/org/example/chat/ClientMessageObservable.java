package org.example.chat;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Singleton that is designed as observable, to be used with observers so that they can be used in
 * server chat implementation.
 */
public class ClientMessageObservable {

    /**
     * The only instance of the singleton.
     */
    private static final ClientMessageObservable INSTANCE = new ClientMessageObservable();

    /**
     * Variable that is used to handle listeners
     */
    private final PropertyChangeSupport observableSuport;

    /**
     * Constructor for variable
     * {@link PropertyChangeSupport observableSuport}
     */
    private ClientMessageObservable() {
        this.observableSuport = new PropertyChangeSupport(this);
    }

    /**
     * Get the only instance of singleton
     * @return singleton instance
     */
    public static ClientMessageObservable getInstance(){
        return INSTANCE;
    }

    /**
     * Method that adds observer to observable (listener) to the class member
     * {@link PropertyChangeSupport observableSuport}
     * @param listener of the ClientMessageObserver type
     */
    public void addObserver(final ClientMessageObserver listener){
        observableSuport.addPropertyChangeListener(listener);
    }

    /**
     * Method that removes observer from observable (listener) to the class member
     * {@link PropertyChangeSupport observableSuport}
     * @param listener of the ClientMessageObserver type
     */
    public void removeObserver(final ClientMessageObserver listener){
        observableSuport.removePropertyChangeListener(listener);
    }

    /**
     * Method is used to get the array of attached observers
     * @return PropertyChangeListener array
     */
    public PropertyChangeListener[] getAttachedObservers() {
        return observableSuport.getPropertyChangeListeners();
    }

    /**
     * Mehod used for broadcasting messages to clients
     * @param clientIdentifier for example "client_1"
     * @param message message that is being broadcasted
     */
    public void broadcastMessageFrom(final String clientIdentifier, final String message){
        observableSuport.firePropertyChange(clientIdentifier, clientIdentifier, message);

    }

}
