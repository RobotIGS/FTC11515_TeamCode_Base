package org.firstinspires.ftc.teamcode.Tools;

import org.firstinspires.ftc.teamcode.Tools.Chassis.Chassis;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Position2D;
import org.firstinspires.ftc.teamcode.Tools.Steuerung.FieldNavigation;

public class Robot {
    public FieldNavigation navi;
    public Chassis chassis;

    public Robot(FieldNavigation navi, Chassis chassis) {
        this.navi = navi;
        this.chassis = chassis;
    }

    public void drive_to_pos(Position2D d, boolean rel) {
        navi.setTargetPosition(d, rel);
    }

    public void rotate(double rotation, boolean rel) {
        navi.setTargetRotation(rotation, rel);
        drive_to_pos(navi.getTargetPosition().copy(), false);
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
