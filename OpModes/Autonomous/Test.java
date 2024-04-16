package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;

@Autonomous
public class Test extends BaseAutonomous {
    @Override
    public void run() {
        hwMap.robot.drive(new Position2D(200, 0));
        while (opModeIsActive() && hwMap.navi.getDriving()) {
            hwMap.robot.step();
            telemetry.addLine(hwMap.navi.debug());
            telemetry.update();
        }
        while (opModeIsActive()) {}
    }
}
