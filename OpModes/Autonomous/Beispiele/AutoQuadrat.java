package org.firstinspires.ftc.teamcode.OpModes.Autonomous.Beispiele;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.OpModes.Autonomous.BaseAutonomous;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Position2D;

@Autonomous(name = "Auto Quadrat")
public class AutoQuadrat extends BaseAutonomous {
    @Override
    public void run() {
        for (int i = 0; i < 4; i++) {
            hwMap.robot.drive_to_pos(new Position2D(100, 0));
            loop_while_driving();
            loop_wait(500);
            hwMap.robot.rotate(90);
            loop_while_driving();
            loop_wait(500);
        }
    }
}