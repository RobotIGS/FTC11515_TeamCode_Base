package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;

@Autonomous(name = "Autonome Quadrat")
public class AutonomeTestQuadrat extends BaseAutonomous {
    
    @Override
    public void run() {
        hwMap.robot.navi.setKeepRotation(true);

        for(int i = 0; i<4; i++) {
            hwMap.robot.drive(new Position2D(100,0));
            schleife();
            sleep(500);
            hwMap.robot.rotate(90);
            schleife();
            sleep(500);
        }
    }

    void schleife() {
        while (opModeIsActive() && hwMap.navi.getDriving_to_position()) {
            hwMap.robot.step();
            telemetry.addLine(hwMap.navi.debug());
            telemetry.addLine(hwMap.chassis.debug());
            telemetry.addLine(hwMap.accelerationProfile.debug());
            telemetry.update();
        }
    }
}
