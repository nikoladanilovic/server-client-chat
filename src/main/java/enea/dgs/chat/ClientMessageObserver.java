package enea.dgs.chat;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Class representing an observer watching for messages from other clients.
 */
public class ClientMessageObserver implements IClientMessageObserver {

    private final BiConsumer<String, String> messageConsumer;

    /**
     * Constructor accepting key to watch.
     *
     * @param messageConsumer {@link Consumer} instance - logic executed when some client message is received
     */
    private ClientMessageObserver(final BiConsumer<String, String> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    /**
     * Factory method constructor accepting the provided key which is used to attach to observation
     * bus (listen events linked to it).
     *
     * @return {@link ClientMessageObserver} instance
     */
    public static ClientMessageObserver of(final BiConsumer<String, String> messageConsumer) {
        return new ClientMessageObserver(messageConsumer);
    }

    /**
     * Attaches the observer to the observable bus.
     */
    public void attach() { ClientMessageObservable.getInstance().addObserver(this); }

    /**
     * Detaches the observer from the observable bus.
     */
    public void detach() { ClientMessageObservable.getInstance().removeObserver(this); }

    @Override
    public void onMessageReceived(final String clientID, final String message) {
        if (Objects.nonNull(clientID) && Objects.nonNull(message))
            messageConsumer.accept(clientID, message);
    }
}
