package org.firstinspires.ftc.teamcode.tools.datentypen;

public class Position2D {
    private double x;
    private double y;

    public Position2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Position2D() {
        this(0.0, 0.0);
    }

    public Position2D addieren(Position2D pos) {
        this.x += pos.x;
        this.y += pos.y;
        return this;
    }

    public Position2D subtrahieren(Position2D pos) {
        this.x -= pos.x;
        this.y -= pos.y;
        return this;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void rotieren(double alpha) {
        double sin = Math.sin(Math.toRadians(alpha));
        double cos = Math.cos(Math.toRadians(alpha));
        double x = this.x;

        this.x = x * cos - y * sin;
        this.y = x * sin + y * cos;
    }

    public double getAbsolute() {
        return Math.hypot(this.x, this.y);
    }

    public Position2D copy() {
        return new Position2D(this.x, this.y);
    }

    public Position2D getNormalization() {
        double abs = getAbsolute();

        if (this.x == 0.0 && this.y == 0.0)
            return copy();

        return new Position2D((this.x / abs), (this.y / abs));
    }
}
