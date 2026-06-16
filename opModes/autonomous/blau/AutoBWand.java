package org.firstinspires.ftc.teamcode.opModes.autonomous.blau;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opModes.autonomous.AutoKorb;
import org.firstinspires.ftc.teamcode.opModes.autonomous.AutoWand;

@Autonomous(name = "Blau Wand", group = "BLAU", preselectTeleOp = "FullControl")
public class AutoBWand extends AutoWand {
    @Override
    public void starten() {
        istRot = false;
        super.starten();
    }
}
