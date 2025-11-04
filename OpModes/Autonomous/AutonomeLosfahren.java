package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Tools.Datatypes.Position2D;

@Autonomous(name = "Autonome Losfahren")
public class AutonomeLosfahren extends BaseAutonomous {

    @Override
    public void run() {
        hwMap.robot.drive(new Position2D(100, 0));
        loop_driving_update();
    }
}