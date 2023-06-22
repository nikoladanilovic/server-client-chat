package org.example.chat;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ClientMessageObservable {

    private static final ClientMessageObservable INSTANCE = new ClientMessageObservable();
    private final PropertyChangeSupport observableSuport;


    private ClientMessageObservable() {
        this.observableSuport = new PropertyChangeSupport(this);
    }

    public static ClientMessageObservable getInstance(){
        return INSTANCE;
    }

    public void addObserver(final ClientMessageObserver listener){
        observableSuport.addPropertyChangeListener(listener);
    }

    public void removeObserver(final ClientMessageObserver listener){
        observableSuport.removePropertyChangeListener(listener);
    }

    public PropertyChangeListener[] getAttachedObservers() {
        return observableSuport.getPropertyChangeListeners();
    }

    public void broadcastMessageFrom(final String clientIdentifier, final String message){
        observableSuport.firePropertyChange(clientIdentifier, clientIdentifier, message);

    }

}
