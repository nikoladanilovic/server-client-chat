package enea.dgs.chat;

public interface IClientMessageObserver {

    void onMessageReceived(final String clientID, final String message);

}
