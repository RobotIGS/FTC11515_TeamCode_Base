package org.firstinspires.ftc.teamcode.opModes.autonomous.beispiele;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opModes.autonomous.BasisAutonomous;
import org.firstinspires.ftc.teamcode.tools.datentypen.Position2D;

@Autonomous(name = "Quadrat", group = "Standard")
public class AutoQuadrat extends BasisAutonomous {
    @Override
    public void starten() {
        super.starten();

        for (int i = 0; i < 4; i++) {
            hwMap.robot.navi.setHalteRotation(false);
            hwMap.robot.fahreZuPosition(new Position2D(100, 0), true);
            schleifeFahren();
            schleifeWarten(500);
            hwMap.robot.navi.setHalteRotation(true);
            hwMap.robot.drehen(90, true);
            schleifeFahren();
            schleifeWarten(500);
        }
    }
}
