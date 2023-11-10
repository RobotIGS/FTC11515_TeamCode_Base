package org.firstinspires.ftc.teamcode.OpModes.Autonomous.Examples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.OpModes.Autonomous.BaseAutonomous;
import org.firstinspires.ftc.teamcode.Tools.Chassis.Chassis;
import org.firstinspires.ftc.teamcode.Tools.Chassis.MecanumChassis;
import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;
import org.firstinspires.ftc.teamcode.Tools.FieldNavigation;
import org.firstinspires.ftc.teamcode.Tools.Robot;

@Autonomous(name="Test Coordinates Driving", group="Examples")
//@Disabled
public class TestCoordinateDriving extends BaseAutonomous {
    private Robot robot;
    private Chassis chassis;
    private FieldNavigation navi;

    @Override
    public void initialize() {
        navi = new FieldNavigation(new Position2D(100,50), 0.0);
        chassis = new MecanumChassis();
        chassis.setRotationAxis(1);
        chassis.populateMotorArray(hardwareMap);

        robot = new Robot(navi, chassis);
    }

    @Override
    public void run() {
        navi.setTargetRotation(90);
        navi.setKeepRotation(true);
        robot.drive(new Position2D(0.0, 0.0), false);

        while (opModeIsActive()) {
            robot.step();
            telemetry.addLine(chassis.debug());
            telemetry.addLine(navi.debug());
            telemetry.update();
        }
    }
}
