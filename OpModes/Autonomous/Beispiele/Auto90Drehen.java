package org.firstinspires.ftc.teamcode.OpModes.Autonomous.Beispiele;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.OpModes.Autonomous.BaseAutonomous;

@Autonomous(name = "90 Drehen")
public class Auto90Drehen extends BaseAutonomous {
    @Override
    public void run() {
        super.run();

        hwMap.robot.rotate(90, true);
        loop_while_driving();
    }
}