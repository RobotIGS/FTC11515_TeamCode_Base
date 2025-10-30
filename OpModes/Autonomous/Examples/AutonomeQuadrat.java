package org.firstinspires.ftc.teamcode.OpModes.Autonomous.Examples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.OpModes.Autonomous.BaseAutonomous;
import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;

@Autonomous(name = "Autonome Quadrat")
public class AutonomeQuadrat extends BaseAutonomous {

    @Override
    public void run() {
        for (int i = 0; i < 4; i++) {
            hwMap.robot.drive(new Position2D(100, 0));
            driving_update();
            sleep(500);
            hwMap.robot.rotate(90);
            driving_update();
            sleep(500);
        }
    }
}