package org.firstinspires.ftc.teamcode.Tools;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.teamcode.Tools.Chassis.ChassisCapabilities;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Position2D;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Rotation;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Velocity;
import org.firstinspires.ftc.teamcode.Tools.Steuerung.AccelerationProfile;
import org.firstinspires.ftc.teamcode.Tools.Steuerung.PidController;

public class FieldNavigation {
    public final static double plattenlänge = 365.75 / 6;
    private final Rotation current_rotation;
    private final Rotation target_rotation;
    private final Velocity velocity;
    public PidController rotationPidController;
    public Position2D distance;
    public boolean drive_sneak; // flag for storing the current speed mode
    public boolean drive_gegensteuern; // flag for storing the current gegensteuern mode
    public double speed_normal;
    public double speed_sneak;
    public double speed_drehen;
    private boolean is_driving_to_position;
    private boolean keeprotation;
    private Position2D current_position;
    private Position2D target_position;
    private double driving_accuracy;
    private AccelerationProfile accProfile;
    private double rotation_accuracy;
    private ChassisCapabilities chassisCapabilities;

    /**
     * create new FieldNavigation object with given position and pid controller for rotation
     *
     * @param current_position position of the robot in CM
     * @param pidController    PID Controller used for rotation
     */
    public FieldNavigation(Position2D current_position, PidController pidController) {
        this.is_driving_to_position = false;
        this.current_position = current_position;
        this.current_rotation = new Rotation(0.0);
        this.target_rotation = new Rotation(0.0);
        this.target_position = current_position;
        this.distance = new Position2D();
        this.velocity = new Velocity();

        this.rotationPidController = pidController;
        this.accProfile = null;

        this.keeprotation = true;
        this.drive_sneak = true;
        this.drive_gegensteuern = true;
    }

    public boolean isDrivingToPosition() {
        return this.is_driving_to_position;
    }

    public void setDrivingAccuracy(double accu) {
        this.driving_accuracy = accu;
    }

    public void setRotationAccuracy(double accu) {
        this.rotation_accuracy = accu;
    }

    /**
     * set the acceleration profile
     *
     * @param accProfile the acceleration profile or null to deactivate
     */
    public void setAccelerationProfile(AccelerationProfile accProfile) {
        this.accProfile = accProfile;
    }

    /**
     * give the filed navigator the capabilities of the used chassis
     *
     * @param capabilities the capabilities of the chassis
     */
    public void setChassisCapabilities(ChassisCapabilities capabilities) {
        this.chassisCapabilities = capabilities;
    }

    /**
     * drive to position
     *
     * @param p target position
     */
    public void drive_pos(Position2D p) {
        this.is_driving_to_position = true;
        this.target_position = p;

        // start the acceleration profile
        this.accProfile.start(this.current_position, this.target_position);
    }

    /**
     * drive a relative distance
     *
     * @param d relative target position
     */
    public void drive_rel(Position2D d) {
        d.rotate(this.current_rotation.get());
        d.add(this.current_position);
        drive_pos(d);
    }

    public AccelerationProfile getAccProfile() {
        return accProfile;
    }

    /**
     * get target velocity
     *
     * @return target velocity
     */
    public Velocity getVelocity() {
        return velocity;
    }

    /**
     * set current rotation
     *
     * @param rot current rotation
     */
    public void setCurrentRotation(double rot) {
        current_rotation.set(rot);
    }

    /**
     * set target rotation
     *
     * @param rotation new target rotation or delta rotation
     * @param rel      specifies if rotation is relative
     */
    public void setTargetRotation(double rotation, boolean rel) {
        this.rotationPidController.reset(); // reset pid controller before usage
        if (rel) {
            target_rotation.add(rotation);
        } else {
            target_rotation.set(rotation);
        }
    }

    /**
     * set the velocity factor used in the autonomous driving
     *
     * @param velFactor the factor [0-1] (domain gets forced)
     */
    public void setSpeedNormal(double velFactor) {
        this.speed_normal = Math.min(1, Math.abs(velFactor));
    }

    public void setSpeedSneak(double speed_sneak) {
        this.speed_sneak = speed_sneak;
    }

    public void setSpeedDrehen(double speed_drehen) {
        this.speed_drehen = speed_drehen;
    }

    /**
     * set if the robot should keep the target rotation
     *
     * @param keep whether the rotation has to be kept
     */
    public void setKeepRotation(boolean keep) {
        keeprotation = keep;
    }

    /**
     * calculate current position utilising the driven distance since the last refresh
     *
     * @param d the driven distance
     */
    public void addDrivenDistance(Position2D d) {
        d.rotate(current_rotation.get());
        current_position.add(d);
    }

    /**
     * manual drive
     *
     * @param vx forward speed (+ => forward)
     * @param vy sideways speed (+ => left)
     * @param wz rotation speed (+ => turn left)
     */
    public void drive_speed(double vx, double vy, double wz) {
        is_driving_to_position = false;
        this.velocity.set(vx, vy, wz);
    }

    public void stop() {
        drive_speed(0.0, 0.0, 0.0);
    }


    @SuppressLint("DefaultLocale")
    public String debug() {
        Rotation rotation_error = new Rotation(target_rotation.get());
        rotation_error.add(-current_rotation.get());

        String ret = "--- FieldNavigation Debug ---\n";
        ret += String.format("driving : %s\ntarget position : x=%+3.1f y=%+3.1f rot=%+3.1f\n",
                (this.is_driving_to_position ? "True" : "False"), target_position.getX(), target_position.getY(), target_rotation.get());
        ret += String.format("distance : x=%+3.1f y=%+3.1f\n", this.distance.getX(), this.distance.getY());
        ret += String.format("position : x=%+3.1f y=%+3.1f rot=%+3.1f\n", current_position.getX(), current_position.getY(), current_rotation.get());
        ret += String.format("velocity : x=%+1.2f y=%+1.2f wz=%+1.2f\n", velocity.getVX(), velocity.getVY(), velocity.getWZ());
        ret += String.format("rotation error : %f\n", rotation_error.get());
        ret += String.format("pid value : %f\n", rotationPidController.pid_value);
        ret += String.format("integral : %f\n", rotationPidController.integral);
        ret += String.format("last_error : %f\n", rotationPidController.last_error);
        return ret;
    }

    /**
     * refresh
     */
    public void step() {
        if (is_driving_to_position) {
            // calculate the distance to the target position
            this.distance = target_position.copy();
            this.distance.subtract(current_position);

            // calculate the error in the rotation
            Rotation rotation_error = new Rotation(target_rotation.get());
            rotation_error.add(-current_rotation.get());

            // setting the velocity for the chassis
            double velFactor = this.accProfile != null ? this.accProfile.step(this.current_position) * this.speed_normal : this.speed_normal;

            // calculate velocity for the chassis
            Position2D distance = this.distance.getNormalization();
            distance.rotate(-this.current_rotation.get());

            // test if in range of the target position (reached)
            if ((Math.abs(this.distance.getAbsolute()) <= this.driving_accuracy && !keeprotation) ||
                    (Math.abs(this.distance.getAbsolute()) <= this.driving_accuracy && keeprotation
                            && Math.abs(rotation_error.get()) <= rotation_accuracy))
                stop();

                // if sideways is allowed : just drive in the direction and rotate
            else if (chassisCapabilities.getDriveSideways()) {
                velocity.set(
                        distance.getX() * velFactor,
                        distance.getY() * velFactor,
                        keeprotation ? rotationPidController.step(rotation_error.get() / 180) : 0.0
                );
            }

            // just drive forward in the direction and rotate to the target
            else if (chassisCapabilities.getRotate()) {
                if (this.distance.getAbsolute() > this.driving_accuracy) {
                    rotation_error.set(Math.toDegrees(Math.asin(distance.getY())));
                    if (distance.getX() < 0) {
                        rotation_error.set(180 - rotation_error.get());
                    }
                }

                velocity.set(
                        distance.getX() * velFactor,
                        0.0,
                        rotationPidController.step(rotation_error.get() / 180)
                );
            }
        }
    }
}
