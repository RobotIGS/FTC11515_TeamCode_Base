package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;

@Autonomous(name = "Autonome Losfahren")
public class AutonomeLosfahren extends BaseAutonomous {

    @Override
    public void run() {
        hwMap.robot.navi.setKeepRotation(true);
        hwMap.robot.drive(new Position2D(100, 0));
        schleife();
    }

    void schleife() {
        while (opModeIsActive() && hwMap.navi.isDrivingToPosition()) {
            hwMap.robot.step();
            telemetry.addLine(hwMap.navi.debug());
            telemetry.addLine(hwMap.chassis.debug());
            telemetry.addLine(hwMap.navi.getAccProfile().debug());
            telemetry.update();
        }
    }
}