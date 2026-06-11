package org.firstinspires.ftc.teamcode.tools;

import org.firstinspires.ftc.teamcode.tools.chassis.Chassis;
import org.firstinspires.ftc.teamcode.tools.datentypen.Position2D;
import org.firstinspires.ftc.teamcode.tools.steuerung.FeldNavigation;

public class Roboter {
    public final FeldNavigation navi;
    public final Chassis chassis;

    public Roboter(FeldNavigation navi, Chassis chassis) {
        this.navi = navi;
        this.chassis = chassis;
    }

    public void fahreZuPosition(Position2D d, boolean rel) {
        navi.setzeZielPosition(d, rel);
    }

    public void drehen(double rotation, boolean rel) {
        navi.setZielRotation(rotation, rel);
        fahreZuPosition(navi.getZielPosition().copy(), false);
    }

    public void stoppen() {
        navi.stoppen();
        chassis.stoppeMotoren();
    }

    public void schritt() {
        navi.setAktuelleRotation(chassis.getRotation());
        navi.addiereGefahreneDistanz(chassis.getGefahreneDistanz());
        navi.schritt();
        chassis.setGeschwindigkeit(navi.getGeschwindigkeit());
        chassis.schritt();
    }
}
