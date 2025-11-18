package org.firstinspires.ftc.teamcode.Tools;

import org.firstinspires.ftc.teamcode.Tools.Chassis.Chassis;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Position2D;

public class Robot {
    public FieldNavigation navi;
    public Chassis chassis;

    public Robot(FieldNavigation navi, Chassis chassis) {
        this.navi = navi;
        this.chassis = chassis;

        // transmit the capabilities of the chassis
        this.navi.setChassisCapabilities(chassis.getCapabilities());
    }

    public void drive(Position2D d, boolean rel) {
        navi.rotationPidController.reset(); // reset pid controller before usage
        if (rel)
            navi.drive_rel(d);
        else
            navi.drive_to_pos(d);
    }

    public void drive(Position2D d) {
        drive(d, true);
    }

    public void rotate(float rotation, boolean rel) {
        navi.setTargetRotation(rotation, rel);
        drive(new Position2D(0.0, 0.0));
    }

    public void rotate(float rotation) {
        rotate(rotation, true);
    }

    public void stop() {
        navi.stop();
        chassis.stopMotors();
    }

    public void step() {
        navi.setCurrentRotation(chassis.getRotation());
        navi.addDrivenDistance(chassis.getDrivenDistance());
        navi.step();
        chassis.setVelocity(navi.getVelocity());
        chassis.step();
    }
}
