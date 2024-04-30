package org.firstinspires.ftc.teamcode.Tools;

import org.firstinspires.ftc.teamcode.Tools.Chassis.Chassis;
import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;

public class Robot {
    public FieldNavigation navi;
    public Chassis chassis;
    protected Profile profile;

    /**
     * create Robot object
     * @param navi the field-navigator used for this robot
     * @param chassis the chassis of the robot
     */
    public Robot(FieldNavigation navi, Chassis chassis) {
        this.navi = navi;
        this.chassis = chassis;

        // transmit the capabilities of the chassis
        this.navi.setChassisCapabilities(chassis.getCapabilities());
    }

    /**
     * Set acceleration profile
     * @param p new acceleration profile
     */
    public void setProfile(Profile p) {
        profile = p;
    }

    /**
     * get current acceleration profile
     * @return the current acceleration profile
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * set robot speed
     * @param vx forward speed (+ => forward)
     * @param vy sideways speed (+ => left)
     * @param wz rotation speed (+ => turn left)
     * @param p use of acceleration profile
     */
    public void setSpeed(double vx, double vy, double wz, boolean p) {
        navi.drive_speed(vx,vy,wz);
        // TODO: handle profile parameter p
    }

    /**
     * set robot speed not utilising the acceleration profile
     * @param vx forward speed (+ => forward)
     * @param vy sideways speed (+ => left)
     * @param wz rotation speed (+ => turn left)
     */
    public void setSpeed(double vx, double vy, double wz) {
        setSpeed(vx, vy, wz, false);
    }

    /**
     * drive to position
     * @param d relative or absolute target position
     * @param rel interpret d as a relative position
     */
    public void drive(Position2D d, boolean rel) {
        if (rel)
            navi.drive_rel(d);
        else
            navi.drive_pos(d);
        navi.rotationPIDcontroller.reset();
    }

    /**
     * drive to relative position
     * @param d relative position
     */
    public void drive(Position2D d) {
        drive(d, true);
    }

    /**
     * rotate the robot
     * @param rotation target rotation (relative if rel)
     * @param rel specify if rotation is relative
     */
    public void rotate(float rotation, boolean rel) {
        navi.rotationPIDcontroller.reset(); // reset pid controller before usage
        navi.setTargetRotation(rotation, rel);
        drive(new Position2D(0.0, 0.0), true);
    }

    /**
     * rotate the robot relative
     * @param rotation target rotation (relative if rel)
     */
    public void rotate(float rotation) { rotate(rotation, true); }

    /**
     * stop all
     */
    public void stop() {
        navi.stop();
        chassis.stopMotors();
    }

    /**
     * refresh everything
     */
    public void step() {
        navi.setRotation(chassis.getRotation());
        navi.addDrivenDistance(chassis.getDrivenDistance());
        navi.step();
        chassis.setVelocity(navi.getVelocity());
        chassis.step();
    }
}
