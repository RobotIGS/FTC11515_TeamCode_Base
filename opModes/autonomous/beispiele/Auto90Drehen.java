package org.firstinspires.ftc.teamcode.opModes.autonomous.beispiele;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opModes.autonomous.BasisAutonomous;

@Autonomous(name = "90° Drehen", group = "Standard", preselectTeleOp = "FullControl")
public class Auto90Drehen extends BasisAutonomous {
    @Override
    public void starten() {
        super.starten();

        hwMap.navi.setHalteRotation(true);
        hwMap.robot.drehen(90, true);
        schleifeFahren();
    }
}
