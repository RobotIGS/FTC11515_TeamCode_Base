package org.firstinspires.ftc.teamcode.Tools.Datatypes;

public class Velocity {
    private double vx;
    private double vy;
    private double wz;

    /**
     * create velocity object
     *
     * @param vx x value
     * @param vy y value
     * @param wz rotation
     */
    public Velocity(double vx, double vy, double wz) {
        this.vx = vx;
        this.vy = vy;
        this.wz = wz;
    }

    /**
     * create velocity object (v = 0)
     */
    public Velocity() {
        this(0.0, 0.0, 0.0);
    }

    public void add(Velocity vel) {
        this.vx += vel.vx;
        this.vy += vel.vy;
        this.wz += vel.wz;
    }

    public void subtract(Velocity vel) {
        this.vx -= vel.vx;
        this.vy -= vel.vy;
        this.wz -= vel.wz;
    }

    /**
     * get x velocity
     *
     * @return x velocity
     */
    public double getVX() {
        return vx;
    }

    /**
     * set x velocity
     *
     * @param vx x velocity
     */
    public void setVX(double vx) {
        this.vx = vx;
    }

    /**
     * get y velocity
     *
     * @return y velocity
     */
    public double getVY() {
        return vy;
    }

    /**
     * set y velocity
     *
     * @param vy y velocity
     */
    public void setVY(double vy) {
        this.vx = vy;
    }

    /**
     * get rotation
     *
     * @return rotation
     */
    public double getWZ() {
        return wz;
    }

    /**
     * set rotation
     *
     * @param wz rotation
     */
    public void setWZ(double wz) {
        this.wz = wz;
    }

    public void set(double vx, double vy, double wz) {
        this.vx = vx;
        this.vy = vy;
        this.wz = wz;
    }

    /**
     * return absolute value
     *
     * @return get absolute velocity
     */
    public double getAbsolute() {
        return Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2) + Math.pow(wz, 2));
    }

    /**
     * copy velocity object
     *
     * @return copy of object
     */
    public Velocity copy() {
        return new Velocity(this.vx, this.vy, this.wz);
    }

    /**
     * normalize velocity
     *
     * @return normalized copy
     */
    public Velocity getNormalization() {
        double new_wz;
        double alpha;

        // wz has to be in [-1:1]
        new_wz = Math.max(-1.0, Math.min(1.0, wz));

        // null vector (vx,vy)
        if (vx == 0.0 && vy == 0.0) return new Velocity(0.0, 0.0, new_wz);

            // one dim
        else if (vx == 0.0)
            return new Velocity(0.0, 1.0, new_wz);
        else if (vy == 0.0)
            return new Velocity(1.0, 0.0, new_wz);

        // normalize
        alpha = Math.atan(vy / vx);
        return new Velocity(Math.cos(alpha), Math.sin(alpha), new_wz);
    }
}
