package org.firstinspires.ftc.teamcode.OpModes.Autonomous.Beispiele;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.OpModes.Autonomous.BaseAutonomous;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Position2D;

@Autonomous(name = "Losfahren Vorne nach 25")
public class AutoLosfahrenVorne25 extends BaseAutonomous {
    @Override
    public void initialize() {
        super.initialize();
        hwMap.navi.setKeepRotation(false);
    }

    @Override
    public void run() {
        super.run();

        loop_wait(25000);
        hwMap.robot.drive_to_pos(new Position2D(50, 0), true);
        loop_while_driving();
    }
}