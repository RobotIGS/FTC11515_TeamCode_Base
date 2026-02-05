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

    public void drive_to_pos(Position2D d, boolean rel) {
        if (rel)
            navi.setTargetPosition_rel(d);
        else
            navi.setTargetPosition_abs(d);
    }

    public void drive_to_pos(Position2D d) {
        drive_to_pos(d, true);
    }

    public void rotate(double rotation, boolean rel) {
        navi.setTargetRotation(rotation, rel);
        drive_to_pos(navi.getTargetPosition());
    }

    public void rotate(double rotation) {
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
