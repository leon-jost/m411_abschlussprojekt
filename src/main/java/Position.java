public class Position {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void print() {
        System.out.println("(" + x + ", " + y + ")");
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public double distance(Position position) {
        int distance_x = Math.abs(x - position.x);
        int distance_y = Math.abs(y - position.y);
        return Math.sqrt((distance_x * distance_x) + (distance_y * distance_y));
    }
}
