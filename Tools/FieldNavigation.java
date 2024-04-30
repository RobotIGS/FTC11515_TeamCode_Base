package org.firstinspires.ftc.teamcode.Tools;

import android.annotation.SuppressLint;
import org.firstinspires.ftc.teamcode.Tools.Chassis.ChassisCapabilities;
import org.firstinspires.ftc.teamcode.Tools.DTypes.Rotation;
import org.firstinspires.ftc.teamcode.Tools.DTypes.Velocity;
import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;

public class FieldNavigation {
    private boolean driving;
    private boolean keeprotation;

    private Position2D position;
    private Rotation rotation;
    private Position2D target_position;
    private Rotation target_rotation;
    private double driving_accuracy;
    private Velocity velocity;

    public PIDcontroller rotationPIDcontroller;
    private double autoVelFactor;
    private Profile accProfile;
    private double rotation_accuracy;
    public Position2D distance;

    private ChassisCapabilities chassisCapabilities;

    /**
     * create new FieldNavigation object with given position
     * @param position position of the robot in CM
     * @param pidController PID Controller used for rotation
     */
    public FieldNavigation(Position2D position, PIDcontroller pidController) {
        this.driving = false;
        this.position = position;
        this.rotation = new Rotation(0.0);
        this.target_rotation = new Rotation(0.0);
        this.rotation_accuracy = 2.0;
        this.target_position = position;
        this.distance = new Position2D();
        this.driving_accuracy = 3.0;
        this.velocity = new Velocity();
        this.rotationPIDcontroller = pidController;
        this.accProfile = null;
        this.autoVelFactor = 1.0;
        keeprotation = false;
    }

    /**
     * create new FieldNavigation object with given position and pid controller for rotation
     * @param position position of the robot in CM
     */
    public FieldNavigation(Position2D position) {
        this(position, new PIDcontroller(6e-3,2e-5,0.0));
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
     * set rotating accuracy
     * @param accu accuracy in degrees
     */
    public void setRotation_accuracy(double accu) {
        rotation_accuracy = accu;
    }

    /**
     * set the acceleration profile
     * @param accProfile the acceleration profile or null to deactivate
     */
    public void setProfile(Profile accProfile) {
        this.accProfile = accProfile;
    }

    /**
     * give the filed navigator the capabilities of the used chassis
     * @param capabilities the capabilities of the chassis
     */
    public void setChassisCapabilities(ChassisCapabilities capabilities) {
        this.chassisCapabilities = capabilities;
    }

    /**
     * drive to position
     * @param p target position
     */
    public void drive_pos(Position2D p) {
        this.driving = true;
        this.target_position = p;

        // start the acceleration profile
        this.accProfile.start(this.position, this.target_position);
    }

    /**
     * drive a relative distance
     * @param d relative target position
     */
    public void drive_rel(Position2D d) {
        d.rotate(this.rotation.get());
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
        rotation.set(rot);
    }

    /**
     * set target rotation
     * @param rotation new target rotation or delta rotation
     * @param rel specifies if rotation is relative
     */
    public void setTargetRotation(double rotation, boolean rel) {
        if (rel)
            target_rotation.add(rotation);
        else
            target_rotation.set(rotation);
    }

    /**
     * set the velocity factor used in the autonomous driving
     * @param velFactor the factor [0-1] (domain gets forced)
     */
    public void setAutoVelFactor(double velFactor) {
        this.autoVelFactor = Math.min(1, Math.abs(velFactor));
    }

    /**
     * set target rotation relative
     * @param rotation delta rotation
     */
    public void setTargetRotation(double rotation) {
        setTargetRotation(rotation, true);
    }

    public double getTargetRotation() {
        return target_rotation.get();
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
     * @param keep whether the rotation has to be kept
     */
    public void setKeepRotation(boolean keep) {keeprotation = keep;}

    /**
     * get keep rotation
     * @return true = keep rotation
     */
    public boolean getKeepRotation() {
        return keeprotation;
    }

    /**
     * calculate current position utilising the driven distance since the last refresh
     * @param d the driven distance
     */
    public void addDrivenDistance(Position2D d) {
        d.rotate(rotation.get());
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


    @SuppressLint("DefaultLocale")
    public String debug() {
        String ret = "--- FieldNavigation Debug ---\n";
        ret += String.format("driving :: %s\ntarget position :: x=%+3.1f y=%+3.1f rot=%+3.1f\n",
                (this.driving?"True":"False"), target_position.getX(), target_position.getY(), target_rotation.get());
        ret += String.format("distance :: x=%+3.1f %+3.1f\n", this.distance.getX(), this.distance.getY());
        ret += String.format("position :: x=%+3.1f y=%+3.1f rot=%+3.1f\n",
                position.getX(), position.getY(), rotation.get());
        ret += String.format("velocity :: x=%+1.2f y=%+1.2f wz=%+1.2f\n",
                velocity.getVX(), velocity.getVY(), velocity.getWZ());

        Rotation rotation_error = new Rotation(target_rotation.get());
        rotation_error.add(-rotation.get());
        ret += String.format("\n\nerror : %f\n", rotation_error.get());
        return ret;
    }

    /**
     * refresh
     */
    public void step() {
        if (driving) {
            // calculate the distance to the target position
            this.distance = target_position.copy();
            this.distance.subtract(position);

            // calculate the error in the rotation
            Rotation rotation_error = new Rotation(target_rotation.get());
            rotation_error.add(-rotation.get());

            // setting the velocity for the chassis
            double velFactor = this.accProfile != null ? this.accProfile.step(this.position) * this.autoVelFactor :
                    this.autoVelFactor;

            // calculate velocity for the chassis
            Position2D distance = this.distance.getNormalization();
            distance.rotate(-this.rotation.get());

            // test if in range of the target position (reached)
            if ((Math.abs(distance.getAbsolute()) <= this.driving_accuracy && !keeprotation) ||
                    (Math.abs(distance.getAbsolute()) <= this.driving_accuracy && keeprotation
                    && Math.abs(rotation_error.get()) <= rotation_accuracy))
                stop();

            // if sideways is allowed : just drive in the direction and rotate
            else if (chassisCapabilities.getDriveSideways()) {
                velocity.set(
                        distance.getX() * velFactor,
                        distance.getY() * velFactor,
                        keeprotation ? rotationPIDcontroller.step(rotation_error.get()) : 0.0);
            }

            // just drive forward in the direction and rotate to the target
            else if (chassisCapabilities.getRotate()) {
                rotation_error.set(Math.toDegrees(Math.asin(distance.getY())));
                if (distance.getX() < 0) {
                    rotation_error.set(180-rotation_error.get());
                }

                // TODO rotate to target rotation if target position was reached
                velocity.set(
                        distance.getX() * velFactor,
                        0.0,
                        keeprotation ? rotationPIDcontroller.step(rotation_error.get()) : 0.0
                );
            }
        }
    }
}
