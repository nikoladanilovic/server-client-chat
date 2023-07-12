package enea.dgs.client.ui.components;

import enea.dgs.client.ui.structure.Vector2;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

public class QuadTests {

//    @Test(groups="smoke")
    @Test
    public void testCheckForCollisionWith() {
        Quad quad = new Quad(Vector2.of(800, 600), Vector2.of(0.5f, 0.5f), 0.4f);
        Assert.assertFalse(quad.checkForCollisionWith(Vector2.of(-1, -1)));
        Assert.assertFalse(quad.checkForCollisionWith(Vector2.of(0, 0)));
        Assert.assertFalse(quad.checkForCollisionWith(Vector2.of(0, 600)));
        Assert.assertFalse(quad.checkForCollisionWith(Vector2.of(800, 0)));
        Assert.assertFalse(quad.checkForCollisionWith(Vector2.of(800, 600)));
        Assert.assertFalse(quad.checkForCollisionWith(Vector2.of(3840, 2160)));

        Assert.assertTrue(quad.checkForCollisionWith(Vector2.of(400, 300)));
        Assert.assertTrue(quad.checkForCollisionWith(Vector2.of(200, 150)));
        Assert.assertTrue(quad.checkForCollisionWith(Vector2.of(600, 150)));
        Assert.assertTrue(quad.checkForCollisionWith(Vector2.of(600, 450)));
        Assert.assertTrue(quad.checkForCollisionWith(Vector2.of(200, 450)));

        Assert.assertFalse(quad.checkForCollisionWith(Vector2.of(199, 150)));
        Assert.assertFalse(quad.checkForCollisionWith(Vector2.of(601, 150)));
        Assert.assertFalse(quad.checkForCollisionWith(Vector2.of(601, 450)));
        Assert.assertFalse(quad.checkForCollisionWith(Vector2.of(199, 450)));
        Assert.assertFalse(quad.checkForCollisionWith(Vector2.of(200, 149)));
        Assert.assertFalse(quad.checkForCollisionWith(Vector2.of(600, 149)));
        Assert.assertFalse(quad.checkForCollisionWith(Vector2.of(600, 451)));
        Assert.assertFalse(quad.checkForCollisionWith(Vector2.of(200, 451)));
    }

    /**
     * Some detailed explanation.
     */
    @Test(description = "Verifies whether the moveUp() method moves the quad by 0.1f in the y direction.")
    public void testMoveUp() {
        Quad quadSpy = Mockito.spy(new Quad(Vector2.of(800, 600), Vector2.of(0.5f, 0.5f), 0.4f));
        quadSpy.moveUp();
        Mockito.verify(quadSpy, Mockito.times(1)).move(Mockito.<Vector2<Float>>any());
        List<Vector2<Float>> vertices = quadSpy.getVertices();
        Assert.assertEquals(vertices.size(), 4);
        Assert.assertEquals(vertices.get(0).toString(), "-0.5,0.6");
        Assert.assertEquals(vertices.get(1).toString(), "0.5,0.6");
        Assert.assertEquals(vertices.get(2).toString(), "0.5,0.1");
        Assert.assertEquals(vertices.get(3).toString(), "-0.5,0.1");

        Quad quadMock = Mockito.mock(Quad.class);
        Mockito.doCallRealMethod().when(quadMock).moveUp();
        quadMock.moveUp();
        Mockito.verify(quadMock, Mockito.times(1)).move(Mockito.<Vector2<Float>>any());
        System.out.println("SPY: " + quadSpy.getVertices().stream().map(Vector2::toString).collect(Collectors.joining("|")));
        System.out.println("MOCK: " + quadMock.getVertices().stream().map(Vector2::toString).collect(Collectors.joining("|")));
    }

}