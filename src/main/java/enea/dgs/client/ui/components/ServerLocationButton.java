package enea.dgs.client.ui.components;

import enea.dgs.client.ServerLocation;
import enea.dgs.client.ui.structure.Vector2;

public class ServerLocationButton {

    private static final Vector2<Float> SIZE_TO_WINDOW_RATION = Vector2.of(0.6f, 0.1f);

    private final Button button;
    private final ServerLocation serverLocation;

    public ServerLocationButton(Vector2<Integer> windowResolution, float yOffset, ServerLocation serverLocation) {
        this.button = new Button(windowResolution, SIZE_TO_WINDOW_RATION, yOffset);
        this.serverLocation = serverLocation;
    }

    public Button getButton() { return button; }

    public ServerLocation getServerLocation() {
        return serverLocation;
    }

}
