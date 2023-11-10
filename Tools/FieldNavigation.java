package org.firstinspires.ftc.teamcode.Tools;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.teamcode.Tools.DTypes.Velocity;
import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;

import java.util.Locale;

public class FieldNavigation {
    private boolean driving;
    private Position2D position;
    private double rotation;
    private Position2D target_position;
    private double target_rotation;
    private double driving_accuracy;
    private Velocity velocity;

    private PIDcontroller rotationPIDcontroller;
    private boolean keeprotation;
    public Position2D distance;

    /**
     * create new FieldNavigation object with given position and rotation
     * @param position position of the robot in CM
     * @param rotation rotation of the robot in Degrees
     */

    // TODO remove rotation as it is part of chassis in the constructor
    public FieldNavigation(Position2D position, double rotation) {
        this.driving = false;
        this.position = position;
        this.rotation = rotation;
        this.target_position = position;
        this.driving_accuracy = 1;
        this.velocity = new Velocity();
        this.rotationPIDcontroller = new PIDcontroller(0.01,0.0,0.0);
        keeprotation = false;
    }

    /**
     * create new FieldNavigation object with (0|0) as position and 0 as rotation
     */
    public FieldNavigation() {
        this(new Position2D(), 0.0);
    }

    /**
     * return if the robot is currently driving
     * @return is driving
     */
    public boolean getDriving() {
        return driving;
    }

    /**
     * set driving accuracy
     * @param accu accuracy in CM
     */
    public void setDriving_accuracy(double accu) {
        driving_accuracy = accu;
    }

    /**
     * drive to position
     * @param p target position
     */
    public void drive_pos(Position2D p) {
        driving = true;
        target_position = p;
    }

    /**
     * drive a relative distance
     * @param d relative target position
     */
    public void drive_rel(Position2D d) {
        d.rotate(-this.rotation);
        d.add(this.position);
        drive_pos(d);
    }

    /**
     * get target velocity
     * @return target velocity
     */
    public Velocity getVelocity() {
        return velocity;
    }

    /**
     * get current position
     * @return current position
     */
    public Position2D getPosition(){
        return position;
    }

    /**
     * set current rotation
     * @param rot current rotation
     */
    public void setRotation(double rot) {
        rotation = rot;
    }

    /**
     * set target rotation
     * @param rot current rotation
     */
    public void setTargetRotation(double rot) {
        target_rotation = rot;
    }

    /**
     * set current position
     * @param p position
     */
    public void setPosition(Position2D p) {
        position = p;
    }

    /**
     * set if the robot should keep the target rotation
     * @param keep
     */
    public void setKeepRotation(boolean keep) {keeprotation = keep;}

    /**
     * calculate current position utilising the driven distance since the last refresh
     * @param d the driven distance
     */
    public void addDrivenDistance(Position2D d) {
        d.rotate(rotation);
        position.add(d);
    }

    /**
     * manual drive
     * @param vx forward speed (+ => forward)
     * @param vy sideways speed (+ => left)
     * @param wz rotation speed (+ => turn left)
     */
    public void drive_speed(double vx, double vy, double wz){
        if (driving)
           driving = false;

        this.velocity.set(vx,vy,wz);
    }

    public void stop(){
        drive_speed(0.0,0.0,0.0);
    }


    public String debug() {
        String ret = "--- FieldNavigation Debug ---\n";
        ret += String.format("driving :: %s\ntarget position :: x=%+3.1f y=%+3.1f\n",
                (this.driving?"True":"False"), target_position.getX(), target_position.getY());
        ret += String.format("distance :: x=%+3.1f %+3.1f\n", this.distance.getX(), this.distance.getY());
        ret += String.format("position :: x=%+3.1f y=%+3.1f\n",
                position.getX(), position.getY());
        ret += String.format("velocity :: x=%+1.2f y=%+1.2f wz=%+1.2f\n",
                velocity.getVX(), velocity.getVY(), velocity.getWZ());
        return ret;
    }

    /**
     * refresh
     */
    public void step() {
        if (driving) {
            // calculate the distance to the target position
            this.distance = target_position.copy();
            this.distance.subract(position);

            // test if in range of the target position (reached)
            if (Math.abs(distance.getAbsolute()) <= this.driving_accuracy)
                stop();

            else {
                // calculate velocity for the chassis
                Position2D distance = this.distance.getNormalization();
                distance.rotate(-this.rotation);

                // setting the velocity for the chassis
                velocity.set(distance.getX() * 0.3, distance.getY() * 0.3, 0.0);
            }
        }

        if (keeprotation) {
            velocity.setWZ(rotationPIDcontroller.step(target_rotation-rotation));
        }
    }
}
