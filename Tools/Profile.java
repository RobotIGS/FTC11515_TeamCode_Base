package org.firstinspires.ftc.teamcode.Tools;

import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;

public class Profile {
    protected Position2D endPosition;
    protected Position2D startPosition;

    protected double accelerationDistance;

    /**
     * create the acceleration profile
     * @param accelerationDistance the distance after which the acceleration profile has reached 100%
     */
    public Profile(double accelerationDistance) {
        this.accelerationDistance = Math.abs(accelerationDistance);
    }

    /**
     * start the acceleration profile for a distance
     * @param start the start position
     * @param end the end position
     */
    public void start(Position2D start, Position2D end) {
        this.startPosition = start;
        this.endPosition = end;
    }

    /**
     * get the velocity factor of the acceleration profile
     * @param position The current position
     * @return the velocity factor in the range [0-1]
     */
    public double step(Position2D position) {
        Position2D target;
        double distanceToEnd, distanceToStart, distance;

        // calculate distance between end position and current position
        target = this.endPosition.copy();
        target.subtract(position);
        distanceToEnd = Math.abs(target.getAbsolute());

        // calculate distance between start position and current position
        target = position.copy();
        target.subtract(position);
        distanceToStart = Math.abs(target.getAbsolute());

        // get the minimum of both distances
        distance = Math.min(distanceToStart, distanceToEnd);

        // if the minimum distance is above the acceleration distance
        if (distance > accelerationDistance) {
            return 1.0;
        }

        // else return the function value
        return Math.max(1,(distance/accelerationDistance)+0.001);
    }
}
