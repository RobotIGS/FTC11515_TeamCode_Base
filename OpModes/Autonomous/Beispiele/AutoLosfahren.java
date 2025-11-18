package org.firstinspires.ftc.teamcode.OpModes.Autonomous.Beispiele;

import static org.firstinspires.ftc.teamcode.Tools.FieldNavigation.plattenlänge;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.OpModes.Autonomous.BaseAutonomous;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Position2D;

@Autonomous(name = "Auto Losfahren")
public class AutoLosfahren extends BaseAutonomous {
    @Override
    public void initialize() {
        super.initialize();
        hwMap.navi.setKeepRotation(false);
    }

    @Override
    public void run() {
        hwMap.robot.drive(new Position2D(plattenlänge, 0));
        loop_driving_update();
        loop_wait(10000);
    }
}