package org.firstinspires.ftc.teamcode.opModes.autonomous;

import static org.firstinspires.ftc.teamcode.tools.steuerung.Navigation.PLATTENLAENGE;

import org.firstinspires.ftc.teamcode.tools.datentypen.Position2D;

public class AutoKorb extends BasisAutonomousFunktionen {
    @Override
    public void starten() {
        super.starten();

        hwMap.robot.fahreZuPosition(new Position2D(-2 * PLATTENLAENGE, 0), true);
        schleifeFahren();
        schiessen();

        hwMap.navi.setHalteRotation(true);
        hwMap.robot.drehen(istRot ? -40 : 40, true);
        schleifeFahren();
        hwMap.navi.setHalteRotation(false);

        hwMap.robot.fahreZuPosition(new Position2D(0, 0.4 * PLATTENLAENGE * (istRot ? -1 : 1)), true);
        schleifeFahren();
        aufnehmen();
        hwMap.robot.fahreZuPosition(new Position2D(-PLATTENLAENGE, PLATTENLAENGE * (istRot ? 1 : -1)), true);
        schleifeFahren();
        schiessen();

        hwMap.robot.fahreZuPosition(new Position2D(0, 2 * PLATTENLAENGE * (istRot ? -1 : 1)), true);
        schleifeFahren();
        aufnehmen();
        hwMap.robot.fahreZuPosition(new Position2D(0.5 * -PLATTENLAENGE, 0), true);
        schleifeFahren();
        hwMap.robot.fahreZuPosition(new Position2D(0.5 * -PLATTENLAENGE, 2 * PLATTENLAENGE * (istRot ? 1 : -1)), true);
        schleifeFahren();
        schiessen();

        hwMap.robot.fahreZuPosition(new Position2D(0, 2 * PLATTENLAENGE * (istRot ? -1 : 1)), true);
        schleifeFahren();
    }
}
