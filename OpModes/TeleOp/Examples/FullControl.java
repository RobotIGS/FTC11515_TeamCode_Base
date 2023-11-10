package org.firstinspires.ftc.teamcode.OpModes.TeleOp.Examples;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OpModes.TeleOp.BaseTeleOp;
import org.firstinspires.ftc.teamcode.Tools.Chassis.MecanumChassis;
import org.firstinspires.ftc.teamcode.Tools.FieldNavigation;
import org.firstinspires.ftc.teamcode.Tools.Chassis.ChassisBase;
import org.firstinspires.ftc.teamcode.Tools.Robot;

@TeleOp(name="FullControl", group="Examples")
//@Disabled
public class FullControl extends BaseTeleOp {
    private Robot robot;
    private FieldNavigation navi;
    private ChassisBase chassis;

    @Override
    public void initialize() {
        navi = new FieldNavigation();
        chassis = new MecanumChassis();
        chassis.setRotationAxis(1);
        chassis.populateMotorArray(hardwareMap);

        robot = new Robot(navi, chassis);
    }

    @Override
    public void run() {
        robot.setSpeed(-gamepad1.left_stick_y*0.5, -gamepad1.left_stick_x*0.5, -gamepad1.right_stick_x*0.5);
        robot.step();

        telemetry.addLine(chassis.debug());
        telemetry.addLine(navi.debug());
        telemetry.update();
    }
}
