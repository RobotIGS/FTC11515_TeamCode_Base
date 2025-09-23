package org.firstinspires.ftc.teamcode.Tools;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;

public class AccelerationProfile {
    protected Position2D endPosition;
    protected Position2D startPosition;
    protected Long startTime;

    protected double accelerationDistance;
    protected double accelerationTime;

    protected double accelerationFactor;
    protected double distance;
    protected double distanceToStart;
    protected double distanceToEnd;

    /**
     * create the acceleration profile
     *
     * @param deaccelerationDistance_in_cm the distance after which the acceleration profile has reached 100%
     */
    public AccelerationProfile(double deaccelerationDistance_in_cm, double accelerationTime_in_s) {
        this.accelerationDistance = Math.abs(deaccelerationDistance_in_cm);
        this.accelerationTime = (long) accelerationTime_in_s * 1000;
        this.startTime = System.currentTimeMillis();
    }

    /**
     * start the acceleration profile for a distance
     *
     * @param start the start position
     * @param end   the end position
     */
    public void start(Position2D start, Position2D end) {
        this.startPosition = start.copy();
        this.endPosition = end.copy();
        this.startTime = System.currentTimeMillis();
    }

    /**
     * get the velocity factor of the acceleration profile
     *
     * @param position The current position
     * @return the velocity factor in the range [0-1]
     */
    public double step(Position2D position) {
        Position2D target;

        // calculate distance between end position and current position
        target = this.endPosition.copy();
        target.subtract(position);
        distanceToEnd = Math.abs(target.getAbsolute());

        // calculate distance between start position and current position
        target = position.copy();
        target.subtract(this.startPosition);
        distanceToStart = Math.abs(target.getAbsolute());

        // deceleration
        if (distanceToStart > distanceToEnd) {
            accelerationFactor = distanceToEnd / accelerationDistance;
        }
        // acceleration
        else {
            double x = (System.currentTimeMillis() - startTime) / accelerationTime;
            accelerationFactor = (x < 1) ? -x * (x - 2) : 1.0; // calculate the parabola only for x smaller 1
        }

        // keep factor in domain
        accelerationFactor = Math.min(Math.abs(accelerationFactor), 1);

        return accelerationFactor;
    }

    public double get() {
        return accelerationFactor;
    }

    @SuppressLint("DefaultLocale")
    public String debug() {
        String ret = "--- Acceleration Profile Debug ---\n";
        ret += String.format("value : %+.4f\n", (accelerationFactor));
        ret += String.format("distance : %+.4f\n", (distance));
        ret += String.format("distanceToSTART : %+.4f\n", (distanceToStart));
        ret += String.format("distanceToEND : %+.4f\n", (distanceToEnd));
        ret += String.format("time : %d\n", (System.currentTimeMillis() - startTime));
        return ret;
    }
}
