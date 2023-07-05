package enea.dgs.client.ui.structure;

public class Vector2<T> {

    private final T x;
    private final T y;

    private Vector2(final T x, final T y) {
        this.x = x;
        this.y = y;
    }

    public static <T> Vector2<T> of(final T x, final T y) {
        return new Vector2<>(x, y);
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }

}
