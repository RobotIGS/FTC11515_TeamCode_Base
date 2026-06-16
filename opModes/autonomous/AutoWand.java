package org.firstinspires.ftc.teamcode.opModes.autonomous;

import static org.firstinspires.ftc.teamcode.tools.steuerung.Navigation.PLATTENLAENGE;

import org.firstinspires.ftc.teamcode.tools.datentypen.Position2D;

public class AutoWand extends BasisAutonomousFunktionen {
    @Override
    public void starten() {
        super.starten();

        hwMap.robot.fahreZuPosition(new Position2D(2.6 * PLATTENLAENGE, 0), true);
        schleifeFahren();
        schiessen();
        hwMap.robot.fahreZuPosition(new Position2D(-2.45 * PLATTENLAENGE, 0), true);
        schleifeFahren();

        hwMap.navi.setHalteRotation(true);
        hwMap.robot.drehen(istRot ? -90 : 90, true);
        schleifeFahren();
        hwMap.navi.setHalteRotation(false);
        aufnehmen();

        hwMap.robot.fahreZuPosition(new Position2D(2 * -PLATTENLAENGE, 0), true);
        schleifeFahren();
        hwMap.robot.fahreZuPosition(new Position2D(0, 2.5 * PLATTENLAENGE * (istRot ? 1 : -1)), true);
        schleifeFahren();
        schiessen();

        hwMap.robot.fahreZuPosition(new Position2D(0, -1 * PLATTENLAENGE * (istRot ? 1 : -1)), true);
        schleifeFahren();
    }
}
