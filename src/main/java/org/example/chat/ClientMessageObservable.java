package org.example.chat;

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

}
