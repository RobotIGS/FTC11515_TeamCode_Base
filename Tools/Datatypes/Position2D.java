package org.firstinspires.ftc.teamcode.Tools.Datatypes;

public class Position2D {
    private double x;
    private double y;

    /**
     * create position object
     *
     * @param x x value
     * @param y y value
     */
    public Position2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Position2D() {
        this(0.0, 0.0);
    }

    public Position2D add(Position2D pos) {
        this.x += pos.x;
        this.y += pos.y;
        return new Position2D(this.x, this.y);
    }

    public Position2D subtract(Position2D pos) {
        this.x -= pos.x;
        this.y -= pos.y;
        return new Position2D(this.x, this.y);
    }

    /**
     * get x position
     *
     * @return x position
     */
    public double getX() {
        return x;
    }

    /**
     * set x position
     *
     * @param x x position
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * get y position
     *
     * @return y position
     */
    public double getY() {
        return y;
    }

    /**
     * set y position
     *
     * @param y y position
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * set x,y position
     *
     * @param x x position
     * @param y y position
     */
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * rotate position around origin
     *
     * @param alpha rotation angle (mathematical positive)
     */
    public void rotate(double alpha) {
        // see rotation matrix
        double sin = Math.sin(Math.toRadians(alpha));
        double cos = Math.cos(Math.toRadians(alpha));
        double x = this.x;

        this.x = x * cos - y * sin;
        this.y = x * sin + y * cos;
    }

    /**
     * return absolute value
     *
     * @return get absolute distance
     */
    public double getAbsolute() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    /**
     * copy position object
     *
     * @return copy of object
     */
    public Position2D copy() {
        return new Position2D(this.x, this.y);
    }

    /**
     * normalize position (absolute value = 1)
     *
     * @return normalized copy
     */
    public Position2D getNormalization() {
        double abs = getAbsolute();

        if (this.x == 0.0 && this.y == 0.0)
            return copy();

        return new Position2D((this.x / abs), (this.y / abs));
    }
}
