package org.firstinspires.ftc.teamcode.opModes.autonomous.blau;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opModes.autonomous.AutoGerade;

@Autonomous(name = "Blau Gerade", group = "BLAU", preselectTeleOp = "FullControl")
public class AutoBGerade extends AutoGerade {
    @Override
    public void starten() {
        istRot = false;
        super.starten();
    }
}
