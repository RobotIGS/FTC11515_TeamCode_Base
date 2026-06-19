package org.firstinspires.ftc.teamcode.opModes.autonomous.beispiele;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opModes.autonomous.BasisAutonomous;

@Autonomous(name = "90° Drehen", group = "Standard", preselectTeleOp = "FullControl")
public class Auto90Drehen extends BasisAutonomous {
    @Override
    public void runOnce() {
        super.runOnce();

        hwMap.navi.setHalteRotation(true);
        hwMap.navi.setZielRotation(90, true);
        schleifeFahren();
    }
}
