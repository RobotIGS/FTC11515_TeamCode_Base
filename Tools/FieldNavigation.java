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
    private final Position2D current_position;
    public PidController rotationPidController;
    public Position2D distance;
    public boolean drive_sneak; // flag for storing the current speed mode
    public boolean drive_gegensteuern; // flag for storing the current gegensteuern mode
    public double speed_normal;
    public double speed_sneak;
    public double speed_drehen;
    private boolean is_driving_to_position;
    private boolean keeprotation;
    private Position2D target_position;
    private double driving_accuracy;
    private AccelerationProfile accProfile;
    private double rotation_accuracy;
    private ChassisCapabilities chassisCapabilities;

    public FieldNavigation(Position2D current_position, PidController pidController) {
        this.is_driving_to_position = false;
        this.current_position = current_position;
        this.target_position = current_position;

        this.current_rotation = new Rotation(0.0);
        this.target_rotation = new Rotation(0.0);

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

    public void setAccelerationProfile(AccelerationProfile accProfile) {
        this.accProfile = accProfile;
    }

    public void setChassisCapabilities(ChassisCapabilities capabilities) {
        this.chassisCapabilities = capabilities;
    }

    public void drive_to_pos(Position2D p) {
        this.is_driving_to_position = true;
        this.target_position = p;

        // start the acceleration profile
        this.accProfile.start(this.current_position, this.target_position);
    }

    public void drive_rel(Position2D d) {
        d.rotate(this.current_rotation.get());
        d.add(this.current_position);
        drive_to_pos(d);
    }

    public AccelerationProfile getAccProfile() {
        return accProfile;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public double getCurrentRotation() {
        return current_rotation.get();
    }

    public void setCurrentRotation(double rot) {
        current_rotation.set(rot);
    }

    public void setTargetRotation(double rotation, boolean relative) {
        this.rotationPidController.reset(); // reset pid controller before usage
        if (relative) {
            target_rotation.add(rotation);
        } else {
            target_rotation.set(rotation);
        }
    }

    public void setSpeedNormal(double velFactor) {
        this.speed_normal = Math.min(1, Math.abs(velFactor)); // factor [0-1]
    }

    public void setSpeedSneak(double speed_sneak) {
        this.speed_sneak = speed_sneak;
    }

    public void setSpeedDrehen(double speed_drehen) {
        this.speed_drehen = speed_drehen;
    }

    public void setKeepRotation(boolean keep_rotation) {
        this.keeprotation = keep_rotation;
    }

    public void addDrivenDistance(Position2D d) {
        d.rotate(current_rotation.get());
        current_position.add(d);
    }

    public void setSpeed(double vx, double vy, double wz) {
        is_driving_to_position = false;
        this.velocity.set(vx, vy, wz);
        // vx forward speed (+ => forward)
        // vy sideways speed (+ => left)
        // wz rotation speed (+ => turn left)
    }

    public void stop() {
        setSpeed(0.0, 0.0, 0.0);
    }

    @SuppressLint("DefaultLocale")
    public String debug() {
        String ret = "--- FieldNavigation Debug ---\n";
        ret += String.format("position: x=%+3.1f y=%+3.1f rot=%+3.1f\n", current_position.getX(), current_position.getY(), current_rotation.get());
        ret += String.format("velocity: x=%+1.2f y=%+1.2f wz=%+1.2f\n", velocity.getVX(), velocity.getVY(), velocity.getWZ());
        if (this.is_driving_to_position) {
            ret += "driving pos: True\n";
            ret += String.format("   target pos: x=%+3.1f y=%+3.1f rot=%+3.1f\n", target_position.getX(), target_position.getY(), target_rotation.get());
            ret += String.format("   distance: x=%+3.1f y=%+3.1f\n", this.distance.getX(), this.distance.getY());
        } else {
            ret += "driving pos: False\n";
        }

        ret += String.format("\ncurrent rotation: %+1.5f\n", current_rotation.get());
        ret += String.format("target rotation: %+1.5f\n", target_rotation.get());
        ret += String.format("pid: pid=%+1.5f int=%+1.5f la_er=%+1.5f\n", rotationPidController.pid_value, rotationPidController.integral, rotationPidController.last_error);
        return ret;
    }

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
