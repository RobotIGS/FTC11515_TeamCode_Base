package org.firstinspires.ftc.teamcode.OpModes.Autonomous.Blau;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.OpModes.Autonomous.AutoSchräg;

@Autonomous(name = "Blau schräg", group = "BLAU")
public class AutoB_schräg extends AutoSchräg {
    @Override
    public void run() {
        isRed = false;
        super.run();
    }
}
