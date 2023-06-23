package org.example.chat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Class that provides and implements observer of the type {@link PropertyChangeListener}
 */
public class ClientMessageObserver implements PropertyChangeListener {
    private final BiConsumer<String, String> messageConsumer;

    private ClientMessageObserver(final BiConsumer<String, String> messageConsumer){
        this.messageConsumer = messageConsumer;
    }

    public static ClientMessageObserver of(final BiConsumer<String, String> messageConsumer){
        return new ClientMessageObserver(messageConsumer);
    }

    /**
     * Method used to attach observer to singleton observable.
     */
    public void attach() {
        ClientMessageObservable.getInstance().addObserver(this);
    }

    /**
     * Method used to detach observer from singleton observable
     */
    public void detach() {
        ClientMessageObservable.getInstance().removeObserver(this);
    }

    /**
     * executes attachment, running and deattachment of the observer (listener) from the observable
     * @param logic
     */
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
