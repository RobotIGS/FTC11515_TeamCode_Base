package org.firstinspires.ftc.teamcode.OpModes.Autonomous.Examples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.OpModes.Autonomous.BaseAutonomous;
import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;

@Autonomous(name = "Autonome Quadrat")
public class AutonomeQuadrat extends BaseAutonomous {

    @Override
    public void run() {
        hwMap.robot.navi.setKeepRotation(true);

        for (int i = 0; i < 4; i++) {
            hwMap.robot.drive(new Position2D(100, 0));
            schleife();
            sleep(500);
            hwMap.robot.rotate(90);
            schleife();
            sleep(500);
        }
    }

    void schleife() {
        while (opModeIsActive() && hwMap.navi.getIsDrivingToPosition()) {
            hwMap.robot.step();
            telemetry.addLine(hwMap.navi.debug());
            telemetry.addLine(hwMap.chassis.debug());
            telemetry.addLine(hwMap.navi.getAccProfile().debug());
            telemetry.update();
        }
    }
}
