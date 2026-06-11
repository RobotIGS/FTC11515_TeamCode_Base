package org.firstinspires.ftc.teamcode.opModes.autonomous.blau;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opModes.autonomous.AutoDiagonal;

@Autonomous(name = "Blau Diagonal", group = "BLAU", preselectTeleOp = "FullControl")
public class AutoBDiagonal extends AutoDiagonal {
    @Override
    public void starten() {
        istRot = false;
        super.starten();
    }
}
