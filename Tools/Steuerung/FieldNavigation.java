package org.firstinspires.ftc.teamcode.Tools.Steuerung;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.teamcode.Tools.Datatypes.Position2D;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Rotation;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Velocity;

public class FieldNavigation {
    public final static double PLATTENLAENGE = 365.75 / 6;
    private final Rotation currentRotation;
    private final Rotation targetRotation;
    private final Velocity velocity;
    private final Position2D currentPosition;
    public PidRegler rotationPidRegler;
    public Position2D distance;
    public boolean driveSneak;
    public boolean driveGegensteuern;
    public double speedNormal;
    public double speedSneak;
    public double speedDrehen;
    public double speedAuto;
    private boolean isDrivingToPosition;
    private boolean driveKeepRotation;
    private Position2D targetPosition;
    private double drivingAccuracy;
    private AccelerationProfile accProfile;
    private double rotationAccuracy;

    public FieldNavigation(Position2D currentPosition, PidRegler pidRegler) {
        this.isDrivingToPosition = false;
        this.currentPosition = currentPosition;
        this.targetPosition = currentPosition.copy();

        this.currentRotation = new Rotation(0.0);
        this.targetRotation = new Rotation(0.0);

        this.distance = new Position2D();
        this.velocity = new Velocity();

        this.rotationPidRegler = pidRegler;
        this.accProfile = null;

        this.driveSneak = true;
        this.driveGegensteuern = true;
    }

    public boolean isDrivingToPosition() {
        return this.isDrivingToPosition;
    }

    public AccelerationProfile getAccProfile() {
        return accProfile;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public void setDrivingAccuracy(double accu) {
        this.drivingAccuracy = accu;
    }

    public void setRotationAccuracy(double accu) {
        this.rotationAccuracy = accu;
    }

    public void setAccelerationProfile(AccelerationProfile accProfile) {
        this.accProfile = accProfile;
    }

    public void setTargetPosition(Position2D p, boolean rel) {
        if (rel) {
            p.rotate(this.currentRotation.get());
            p.add(this.currentPosition);
            setTargetPosition(p, false);
        } else {
            this.isDrivingToPosition = true;
            this.targetPosition = p;
            if (this.accProfile != null) {
                this.accProfile.start(this.currentPosition, this.targetPosition); // start the acceleration profile
            }
        }
    }

    public Position2D getTargetPosition() {
        return targetPosition;
    }

    public void setCurrentRotation(double rot) {
        currentRotation.set(rot);
    }

    public void setTargetRotation(double rotation, boolean relative) {
        rotationPidRegler.reset();
        if (relative) {
            targetRotation.add(rotation);
        } else {
            targetRotation.set(rotation);
        }
    }

    public void setSpeedNormal(double speedNormal) {
        this.speedNormal = Math.max(0, Math.min(1, speedNormal)); // factor [0-1]
    }

    public void setSpeedSneak(double speedSneak) {
        this.speedSneak = Math.max(0, Math.min(1, speedSneak)); // factor [0-1]
    }

    public void setSpeedDrehen(double speedDrehen) {
        this.speedDrehen = Math.max(0, Math.min(1, speedDrehen)); // factor [0-1]
    }

    public void setSpeedAuto(double speedAuto) {
        this.speedAuto = Math.max(0, Math.min(1, speedAuto)); // factor [0-1]
    }

    public void setKeepRotation(boolean keepRotation) {
        this.driveKeepRotation = keepRotation;
    }

    public void addDrivenDistance(Position2D d) {
        Position2D d_rotated = d.copy();
        d_rotated.rotate(currentRotation.get());
        currentPosition.add(d_rotated);
    }

    public void setSpeed(double vx, double vy, double wz) {
        isDrivingToPosition = false;
        this.velocity.set(vx, vy, wz);
    }

    public void stop() {
        setSpeed(0.0, 0.0, 0.0);
    }

    public void step() {
        if (isDrivingToPosition) {
            // calculate the distance to the target position
            this.distance = targetPosition.copy();
            this.distance.subtract(currentPosition);

            // calculate the error in the rotation
            Rotation rotation_error = new Rotation(currentRotation.get());
            rotation_error.add(-targetRotation.get());

            // setting the velocity for the chassis
            double velFactor = this.accProfile != null ? this.accProfile.step(this.currentPosition) * this.speedAuto : this.speedAuto;

            // calculate velocity for the chassis
            Position2D distance = this.distance.getNormalization();
            distance.rotate(-this.currentRotation.get());

            // test if in range of the target position (reached)
            if ((Math.abs(this.distance.getAbsolute()) <= this.drivingAccuracy && !driveKeepRotation) ||
                    (Math.abs(this.distance.getAbsolute()) <= this.drivingAccuracy && driveKeepRotation
                            && Math.abs(rotation_error.get()) <= rotationAccuracy)) {
                stop();

            } else if (true) { // if sideways is allowed: just drive in the direction and rotate
                velocity.set(
                        distance.getX() * velFactor,
                        distance.getY() * velFactor,
                        driveKeepRotation ? rotationPidRegler.step(rotation_error.get()) : 0.0
                );
            } else if (true) { // if rotation is allowed: just drive forward in the direction and rotate to the target
                if (this.distance.getAbsolute() > this.drivingAccuracy) {
                    rotation_error.set(Math.toDegrees(Math.asin(distance.getY())));
                    if (distance.getX() < 0) {
                        rotation_error.set(180 - rotation_error.get());
                    }
                }

                velocity.set(
                        distance.getX() * velFactor,
                        0.0,
                        rotationPidRegler.step(rotation_error.get())
                );
            }
        }
    }

    @SuppressLint("DefaultLocale")
    public String debug() {
        String ret = "--- FieldNavigation Debug ---\n";
        ret += String.format("position: x=%+3.1f y=%+3.1f rot: %+1.2f\n", currentPosition.getX(), currentPosition.getY(), currentRotation.get());
        ret += String.format("velocity: x=%+1.2f y=%+1.2f wz=%+1.2f\n", velocity.getVX(), velocity.getVY(), velocity.getWZ());
        if (this.isDrivingToPosition) {
            ret += "driving pos: True\n";
            ret += String.format("   target pos: x=%+3.1f y=%+3.1f rot=%+1.2f\n", targetPosition.getX(), targetPosition.getY(), targetRotation.get());
            ret += String.format("   distance: x=%+3.1f y=%+3.1f\n", this.distance.getX(), this.distance.getY());
        } else {
            ret += "driving pos: False\n";
        }
        return ret;
    }
}