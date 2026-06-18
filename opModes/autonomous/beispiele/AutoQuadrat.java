package org.firstinspires.ftc.teamcode.opModes.autonomous.beispiele;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opModes.autonomous.BasisAutonomous;
import org.firstinspires.ftc.teamcode.tools.datentypen.Position2D;

@Autonomous(name = "Quadrat", group = "Standard", preselectTeleOp = "FullControl")
public class AutoQuadrat extends BasisAutonomous {
    @Override
    public void starten() {
        super.starten();

        for (int i = 0; i < 4; i++) {
            hwMap.navi.setHalteRotation(false);
            hwMap.navi.setzeZielPosition(new Position2D(100, 0), true);
            schleifeFahren();
            schleifeWarten(500);
            hwMap.navi.setHalteRotation(true);
            hwMap.navi.setZielRotation(90, true);
            schleifeFahren();
            schleifeWarten(500);
        }
    }
}
