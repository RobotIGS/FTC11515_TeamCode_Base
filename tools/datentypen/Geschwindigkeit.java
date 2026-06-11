package org.firstinspires.ftc.teamcode.tools.datentypen;

public class Geschwindigkeit {
    private double vx;
    private double vy;
    private double vz;

    /**
     * @param vx x-Wert
     * @param vy y-Wert
     * @param vz Rotation
     */
    public Geschwindigkeit(double vx, double vy, double vz) {
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    public Geschwindigkeit() {
        this(0.0, 0.0, 0.0);
    }

    public double getVX() {
        return vx;
    }

    public double getVY() {
        return vy;
    }

    public double getVZ() {
        return vz;
    }


    public void set(double vx, double vy, double vz) {
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    public double getBetrag() {
        return Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2) + Math.pow(vz, 2));
    }

    public Geschwindigkeit copy() {
        return new Geschwindigkeit(this.vx, this.vy, this.vz);
    }

    public Geschwindigkeit getNormalisierung() {
        // vz muss in [-1:1] sein
        double neueVZ = Math.max(-1.0, Math.min(1.0, vz));

        // Nullvektor (vx,vy)
        if (vx == 0.0 && vy == 0.0) {
            return new Geschwindigkeit(0.0, 0.0, neueVZ);
        }

        // normalisieren
        double alpha = Math.atan2(vy, vx);
        return new Geschwindigkeit(Math.cos(alpha), Math.sin(alpha), neueVZ);
    }
}
