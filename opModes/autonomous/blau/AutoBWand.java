package org.firstinspires.ftc.teamcode.opModes.autonomous.blau;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opModes.autonomous.AutoWand;

@Autonomous(name = "Blau Wand", group = "BLAU", preselectTeleOp = "FullControl")
public class AutoBWand extends AutoWand {
    @Override
    public void runOnce() {
        istRot = false;
        super.runOnce();
    }
}
