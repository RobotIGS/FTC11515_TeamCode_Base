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

    public Velocity() {
        this(0.0, 0.0, 0.0);
    }

    public double getVX() {
        return vx;
    }

    public void setVX(double vx) {
        this.vx = vx;
    }


    public double getVY() {
        return vy;
    }

    public void setVY(double vy) {
        this.vy = vy;
    }


    public double getWZ() {
        return wz;
    }


    public void setWZ(double wz) {
        this.wz = wz;
    }

    public void set(double vx, double vy, double wz) {
        this.vx = vx;
        this.vy = vy;
        this.wz = wz;
    }

    public double getAbsolute() {
        return Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2) + Math.pow(wz, 2));
    }


    public Velocity copy() {
        return new Velocity(this.vx, this.vy, this.wz);
    }


    public Velocity getNormalization() {
        double new_wz;
        double alpha;

        // wz has to be in [-1:1]
        new_wz = Math.max(-1.0, Math.min(1.0, wz));

        // null vector (vx,vy)
        if (vx == 0.0 && vy == 0.0) {
            return new Velocity(0.0, 0.0, new_wz);
        }

        // normalize
        alpha = Math.atan2(vy, vx);
        return new Velocity(Math.cos(alpha), Math.sin(alpha), new_wz);
    }
}
