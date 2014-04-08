
package eu.diversify.disco.experiments.controllers.decentralised;

public class Position {

    private final double x;
    private final double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    double distanceTo(Position position) {
        final double sum =
                Math.pow(x - position.x, 2)
                + Math.pow(y - position.y, 2);
        return Math.sqrt(sum);
    }
}
