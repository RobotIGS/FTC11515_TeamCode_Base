package org.firstinspires.ftc.teamcode.opModes.autonomous;

import static org.firstinspires.ftc.teamcode.tools.steuerung.FeldNavigation.PLATTENLAENGE;

import org.firstinspires.ftc.teamcode.tools.datentypen.Position2D;

public class AutoGerade extends BasisAutonomousFunktionen {
    @Override
    public void starten() {
        super.starten();

        hwMap.robot.fahreZuPosition(new Position2D(-4 * PLATTENLAENGE, -100), true);
        schleifeFahren();
        schiessen();

        if (false) {
            hwMap.robot.drehen(istRot ? -70 : 70, true);
            schleifeFahren();
            hwMap.robot.fahreZuPosition(new Position2D(0, PLATTENLAENGE * (istRot ? 1 : -1)), true);
            schleifeFahren();
            aufnehmen();

            hwMap.robot.fahreZuPosition(new Position2D(-PLATTENLAENGE, PLATTENLAENGE * (istRot ? -1 : 1)), true);
            schleifeFahren();
            hwMap.robot.drehen(istRot ? 70 : -70, true);
            schleifeFahren();
            schiessen();
        }
    }
}
