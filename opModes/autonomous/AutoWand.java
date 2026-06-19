package org.firstinspires.ftc.teamcode.opModes.autonomous;

import static org.firstinspires.ftc.teamcode.tools.steuerung.Navigation.PLATTENLAENGE;

import org.firstinspires.ftc.teamcode.tools.datentypen.Position2D;

public class AutoWand extends BasisAutonomousFunktionen {
    @Override
    public void runOnce() {
        super.runOnce();

        hwMap.navi.setzeZielPosition(new Position2D(2.4 * PLATTENLAENGE, 0), true);
        schleifeFahren();
        schiessen();
        hwMap.navi.setzeZielPosition(new Position2D(-2 * PLATTENLAENGE, 0), true);
        schleifeFahren();

        hwMap.navi.setHalteRotation(true);
        hwMap.navi.setZielRotation(istRot ? -90 : 90, true);
        schleifeFahren();
        hwMap.navi.setHalteRotation(false);
        aufnehmen();

        hwMap.navi.setzeZielPosition(new Position2D(2 * -PLATTENLAENGE, 0), true);
        schleifeFahren();
        hwMap.navi.setzeZielPosition(new Position2D(0, 2.5 * PLATTENLAENGE * (istRot ? 1 : -1)), true);
        schleifeFahren();

        hwMap.navi.setHalteRotation(true);
        hwMap.navi.setZielRotation(istRot ? 90 : -90, true);
        schleifeFahren();
        hwMap.navi.setHalteRotation(false);
        schiessen();
        hwMap.navi.setHalteRotation(true);
        hwMap.navi.setZielRotation(istRot ? -90 : 90, true);
        schleifeFahren();
        hwMap.navi.setHalteRotation(false);

        hwMap.navi.setzeZielPosition(new Position2D(0, -1 * PLATTENLAENGE * (istRot ? 1 : -1)), true);
        schleifeFahren();
    }
}
