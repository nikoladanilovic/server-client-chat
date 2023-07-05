package enea.dgs.client.ui.components;

import enea.dgs.client.ui.structure.Vector2;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class Button {

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final List<Vector2<Float>> vertices;

    public Button(Vector2<Integer> windowResolution, Vector2<Float> sizeToWindowRation, float yOffset) {
        yOffset += 0.1f; // first button should not be drawn on the top of the window
        this.x = (int) (((1f - sizeToWindowRation.getX()) / 2) * windowResolution.getX());
        this.y = (int) ((yOffset / 2) * windowResolution.getY());
        this.width = (int) (windowResolution.getX() * sizeToWindowRation.getX());
        this.height = (int) (windowResolution.getY() * sizeToWindowRation.getY());
        float x1 = -1 * sizeToWindowRation.getX();
        float x2 = sizeToWindowRation.getX();
        float y1 = 1f - yOffset;
        float y2 = y1 - sizeToWindowRation.getY();
        this.vertices = List.of(Vector2.of(x1, y1), Vector2.of(x2, y1), Vector2.of(x2, y2), Vector2.of(x1, y2));
    }

    public boolean checkForCollisionWith(Vector2<Integer> mouseClick) {
        return mouseClick.getX() >= x
                && mouseClick.getX() <= x + width
                && mouseClick.getY() >= y
                && mouseClick.getY() <= y + height;
    }

    public void draw() {
        glBegin(GL_QUADS);
        glColor3f(0.0f, 0.0f, 1.0f);
        vertices.forEach(vertex -> glVertex2f(vertex.getX(), vertex.getY()));
        glEnd();
    }

}
