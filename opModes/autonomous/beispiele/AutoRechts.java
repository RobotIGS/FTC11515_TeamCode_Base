package org.firstinspires.ftc.teamcode.opModes.autonomous.beispiele;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opModes.autonomous.BasisAutonomous;
import org.firstinspires.ftc.teamcode.tools.datentypen.Position2D;

@Autonomous(name = "Rechts", group = "Standard", preselectTeleOp = "FullControl")
public class AutoRechts extends BasisAutonomous {
    @Override
    public void initialisieren() {
        super.initialisieren();
        hwMap.navi.setHalteRotation(false);
    }

    @Override
    public void runOnce() {
        super.runOnce();

        hwMap.navi.setzeZielPosition(new Position2D(0, -35), true);
        schleifeFahren();
    }
}
