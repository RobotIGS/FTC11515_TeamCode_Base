package org.firstinspires.ftc.teamcode.OpModes.Autonomous.Beispiele;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.OpModes.Autonomous.BaseAutonomous;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Position2D;

@Autonomous(name = "Losfahren Vorne")
public class AutoLosfahrenVorne extends BaseAutonomous {
    @Override
    public void initialize() {
        super.initialize();
        hwMap.navi.setKeepRotation(false);
    }

    @Override
    public void run() {
        hwMap.robot.drive_to_pos(new Position2D(20, 0));
        loop_while_driving();
        loop_wait(10000);
    }
}