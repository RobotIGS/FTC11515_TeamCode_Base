package org.firstinspires.ftc.teamcode.OpModes.Autonomous.Blau;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.OpModes.Autonomous.AutoGerade;

@Autonomous(name = "AutoB_gerade", group = "BLAU")
public class AutoB_gerade extends AutoGerade {
    @Override
    public void run() {
        isRed = false;
        super.run();
    }
}
