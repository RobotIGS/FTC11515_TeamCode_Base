package org.firstinspires.ftc.teamcode.Tools.Steuerung;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.teamcode.Tools.Datatypes.Position2D;

public class AccelerationProfile {
    protected Position2D endPosition;
    protected Position2D startPosition;
    protected Long startTime;

    protected double deaccelerationDistance;
    protected double accelerationTime;

    protected double factor;
    protected double distance;
    protected double distanceToStart;
    protected double distanceToEnd;

    /**
     * create the acceleration profile
     *
     * @param deaccelerationDistance_in_cm the distance after which the acceleration profile has reached 100%
     */
    public AccelerationProfile(double deaccelerationDistance_in_cm, double accelerationTime_in_s) {
        this.deaccelerationDistance = Math.abs(deaccelerationDistance_in_cm);
        this.accelerationTime = (long) (accelerationTime_in_s * 1000);
        this.startTime = System.currentTimeMillis();
    }

    public void start(Position2D start, Position2D end) {
        this.startPosition = start.copy();
        this.endPosition = end.copy();
        this.startTime = System.currentTimeMillis();
    }

    public double step(Position2D position) {
        if (this.endPosition == null || this.startPosition == null) {
            return 1.0;
        }

        distanceToEnd = position.copy().subtract(this.endPosition).getAbsolute();
        distanceToStart = position.copy().subtract(this.startPosition).getAbsolute();

        // Berechne Brems-Faktor (1 -> 0)
        double decelFactor = distanceToEnd / deaccelerationDistance;

        // Berechne Anfahr-Faktor (0 -> 1)
        double accelFactor = 1.0;
        if (accelerationTime > 0) {
            double timeProgress = (double) (System.currentTimeMillis() - startTime) / accelerationTime;
            accelFactor = (timeProgress < 1) ? -timeProgress * (timeProgress - 2) : 1.0;
        }

        // Der kleinste Faktor gewinnt (Trapez-Profil)
        factor = Math.min(Math.min(accelFactor, decelFactor), 1.0);
        return Math.max(0.1, factor); // Nie ganz auf 0 fallen, damit er das Ziel erreicht
    }


    public double get() {
        return factor;
    }

    @SuppressLint("DefaultLocale")
    public String debug() {
        String ret = "--- Acceleration Profile Debug ---\n";
        ret += String.format("value: %+.4f\n", (factor));
        ret += String.format("distance: %+.4f\n", (distance));
        ret += String.format("distanceToSTART: %+.4f\n", (distanceToStart));
        ret += String.format("distanceToEND: %+.4f\n", (distanceToEnd));
        ret += String.format("time: %d\n", (System.currentTimeMillis() - startTime));
        return ret;
    }
}
