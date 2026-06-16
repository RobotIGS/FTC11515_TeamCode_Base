package org.firstinspires.ftc.teamcode.opModes.autonomous.blau;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opModes.autonomous.AutoKorb;

@Autonomous(name = "Blau Korb", group = "BLAU", preselectTeleOp = "FullControl")
public class AutoBKorb extends AutoKorb {
    @Override
    public void starten() {
        istRot = false;
        super.starten();
    }
}
