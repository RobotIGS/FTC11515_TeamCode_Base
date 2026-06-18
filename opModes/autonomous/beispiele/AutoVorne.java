package org.firstinspires.ftc.teamcode.opModes.autonomous.beispiele;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opModes.autonomous.BasisAutonomous;
import org.firstinspires.ftc.teamcode.tools.datentypen.Position2D;

@Autonomous(name = "Vorne", group = "Standard", preselectTeleOp = "FullControl")
public class AutoVorne extends BasisAutonomous {
    @Override
    public void initialisieren() {
        super.initialisieren();
        hwMap.navi.setHalteRotation(false);
    }

    @Override
    public void starten() {
        super.starten();

        hwMap.navi.setzeZielPosition(new Position2D(35, 0), true);
        schleifeFahren();
    }
}
