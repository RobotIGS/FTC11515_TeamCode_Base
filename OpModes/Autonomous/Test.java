package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;

@Autonomous(name = "Test smt.")
public class Test extends BaseAutonomous {
    @Override
    public void run() {
        hwMap.robot.drive(new Position2D(0, 100));
        while (opModeIsActive() && hwMap.navi.getDriving()) {
            hwMap.robot.step();
            telemetry.addData("velocity", hwMap.navi.getVelocity().getVX());
            //telemetry.addLine(accelerationProfile.debug());
            telemetry.addLine(hwMap.navi.debug());
            telemetry.addLine(hwMap.chassis.debug());
            telemetry.update();
        }
        while (opModeIsActive()) {}
    }
}
