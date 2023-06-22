package org.example.chat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import java.util.function.BiConsumer;

public class ClientMessageObserver implements PropertyChangeListener {
    private final BiConsumer<String, String> messageConsumer;

    private ClientMessageObserver(final BiConsumer<String, String> messageConsumer){
        this.messageConsumer = messageConsumer;
    }

    public static ClientMessageObserver of(final BiConsumer<String, String> messageConsumer){
        return new ClientMessageObserver(messageConsumer);
    }

    public void attach() {
        ClientMessageObservable.getInstance().addObserver(this);
    }

    public void detach() {
        ClientMessageObservable.getInstance().removeObserver(this);
    }

    public void executeWrapped(Runnable logic){
        if(Objects.isNull(logic))
            return;
        attach();
        logic.run();
        detach();
    }

    @Override
    public void propertyChange(final PropertyChangeEvent propertyChangeEvent){
        if (Objects.nonNull(propertyChangeEvent) && Objects.nonNull(propertyChangeEvent.getNewValue())){
            messageConsumer.accept((String) propertyChangeEvent.getOldValue(), (String) propertyChangeEvent.getNewValue());
        }
    }

}
