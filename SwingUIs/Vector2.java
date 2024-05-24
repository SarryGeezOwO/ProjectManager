package SwingUIs;

public class Vector2 {
    public int x;
    public int y;

    public Vector2() {
        x = 0;
        y = 0;
    }

    public Vector2(int s) {
        this.x = s;
        this.y = s;
    }

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2 add(Vector2 first, Vector2 second) {
        return new Vector2(
            first.x + second.x,
            first.y + second.y
        );
    }

    public static Vector2 subtract(Vector2 first, Vector2 second) {
        return new Vector2(
            first.x - second.x,
            first.y - second.y
        );
    }

    public static Vector2 multiply(Vector2 first, Vector2 second) {
        return new Vector2(
            first.x * second.x,
            first.y * second.y
        );
    }

    public static Vector2 divide(Vector2 first, Vector2 second) {
        return new Vector2(
            first.x / second.x,
            first.y / second.y
        );
    }
}
