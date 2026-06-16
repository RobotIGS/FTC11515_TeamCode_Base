package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.tools.HwMap;
import org.firstinspires.ftc.teamcode.tools.steuerung.Gegensteuern;

@TeleOp(name = "FullControl", group = "FTC")
public class FullControl extends BasisTeleOp {
    final Gegensteuern gegensteuernX = new Gegensteuern("X");
    final Gegensteuern gegensteuernY = new Gegensteuern("Y");
    double altVx;
    double altVy;

    /* ADD VARIABLES ONLY USED IN FULL CONTROL */
    // Kopfdrehen
    private boolean kopfManuell = false;
    private double targetGlobalRotation;
    private double kopfLokalPosition = 0;
    private long kopfLetzterUpdateZeitstempel = 0;

    private double servoRampeLimitUnten = 0;
    private double servoRampeLimitOben = 0;
    /* END SECTION */

    @Override
    public void initialisieren() {
        super.initialisieren();
        hwMap = new HwMap(hardwareMap);
        /* ADD CODE WHICH IS RUN ONCE WHEN INIT IS PRESSED */

        /* END SECTION */
    }

    @Override
    public void runOnce() {
        /* ADD CODE WHICH IS RUN ONCE WHEN PLAY IS PRESSED */
        servoRampeLimitUnten = hwMap.sRampeL.getPosition();
        servoRampeLimitOben = hwMap.sRampeL.getPosition() + 0.4;
        targetGlobalRotation = hwMap.chassis.getRotation();
        /* END SECTION */
    }

    @Override
    public void runLoop() {
        fahren();
        saison();
        aktualisiereKopf();
        hwMap.robot.schritt();
        telemetrie();
    }

    @Override
    public void fahren() {
        if (istTasteGedrueckt("gp1_lb", gamepad1.left_bumper)) {
            hwMap.navi.sneak = !hwMap.navi.sneak;
        }
        if (istTasteGedrueckt("gp1_rb", gamepad1.right_bumper)) {
            hwMap.navi.fahreGegensteuern = !hwMap.navi.fahreGegensteuern;
        }

        double vx = -gamepad1.left_stick_y * (hwMap.navi.sneak ? hwMap.navi.geschwindigkeitSneak : hwMap.navi.geschwindigkeitNormal);
        double vy = -gamepad1.right_stick_x * (hwMap.navi.sneak ? hwMap.navi.geschwindigkeitSneak : hwMap.navi.geschwindigkeitNormal);
        double vz = (gamepad1.left_trigger - gamepad1.right_trigger) * hwMap.navi.geschwindigkeitDrehen * (hwMap.navi.sneak ? hwMap.navi.geschwindigkeitSneak : hwMap.navi.geschwindigkeitNormal);

        hwMap.navi.setGeschwindigkeit(
                gegensteuernX.calculate(hwMap.navi.fahreGegensteuern, altVx, vx),
                gegensteuernY.calculate(hwMap.navi.fahreGegensteuern, altVy, vy),
                vz);

        altVx = vx;
        altVy = vy;
    }

    @Override
    public void telemetrie() {
        telemetry.addData("Sneak", hwMap.navi.sneak);
        telemetry.addData("Gegensteuern", hwMap.navi.fahreGegensteuern);
        telemetry.addData("Kopf Modus", kopfManuell ? "MANUELL" : "AUTO");
        telemetry.addLine();
        telemetry.addLine(hwMap.navi.debug());
        telemetry.addLine(hwMap.chassis.debug());
        telemetry.addLine(gegensteuernX.debug());
        telemetry.addLine(gegensteuernY.debug());
        telemetry.addLine();
        telemetry.addData("Basis Schussgeschwindigkeit", hwMap.geschwindigkeitSchuss);
        telemetry.addData("Anpasste Schussgeschwindigkeit", hwMap.getAnpassteSchussgeschwindigkeit());
        telemetry.addLine();
        telemetry.addData("Kopf Target", targetGlobalRotation);
        telemetry.addData("Kopf Position", kopfLokalPosition);
        telemetry.addData("Servo Rampe L", hwMap.sRampeL.getPosition());
        telemetry.addData("Servo Rampe R", hwMap.sRampeR.getPosition());

        telemetry.update();
    }

    @Override
    public void saison() {
        // Rampen Winkel
        if (istTasteGedrueckt("gp2_rt", gamepad2.right_trigger_pressed) && hwMap.sRampeL.getPosition() > servoRampeLimitUnten) {
            hwMap.sRampeL.setPosition(hwMap.sRampeL.getPosition() - 0.1);
            hwMap.sRampeR.setPosition(hwMap.sRampeR.getPosition() + 0.1);
        }
        if (istTasteGedrueckt("gp2_lt", gamepad2.left_trigger_pressed) && hwMap.sRampeL.getPosition() < servoRampeLimitOben) {
            hwMap.sRampeL.setPosition(hwMap.sRampeL.getPosition() + 0.1);
            hwMap.sRampeR.setPosition(hwMap.sRampeR.getPosition() - 0.1);
        }

        // Schussgeschwindigkeit
        if (istTasteGedrueckt("gp2_rb", gamepad2.right_bumper)) {
            hwMap.geschwindigkeitSchuss = Math.min(1.0, hwMap.geschwindigkeitSchuss + 0.05);
        }
        if (istTasteGedrueckt("gp2_lb", gamepad2.left_bumper)) {
            hwMap.geschwindigkeitSchuss = Math.max(0.4, hwMap.geschwindigkeitSchuss - 0.05);
        }

        // motor aufnehmen
        if (istTasteGedrueckt("gp2_b", gamepad2.b)) {
            if (hwMap.mAufnehmen.getPower() == 0) {
                hwMap.mAufnehmen.setPower(hwMap.geschwindigkeitAufnehmen);
            } else {
                hwMap.mAufnehmen.setPower(0);
            }
        }

        // motor schiessen
        if (istTasteGedrueckt("gp2_a", gamepad2.a)) {
            if (hwMap.mSchiessen.getPower() == 0) {
                hwMap.mSchiessen.setPower(hwMap.getAnpassteSchussgeschwindigkeit());
            } else {
                hwMap.mSchiessen.setPower(0);
            }
        }

        // motor innen
        if (istTasteGedrueckt("gp2_x", gamepad2.x)) {
            if (hwMap.mInnen.getPower() == 0) {
                hwMap.mInnen.setPower(1.0);
            } else {
                hwMap.mInnen.setPower(0);
            }
        }
    }

    private void aktualisiereKopf() {
        if (istTasteGedrueckt("gp2_rsb", gamepad2.right_stick_button)) {
            kopfManuell = !kopfManuell;
        }

        long jetzt = System.nanoTime();
        double aktuelleRotation = hwMap.chassis.getRotation();

        double dt = (jetzt - kopfLetzterUpdateZeitstempel) / 1e9;
        if (kopfLetzterUpdateZeitstempel != 0 && dt > 0) {
            // 1. Position-Tracking (Basiert auf der Power des letzten Zyklus)
            kopfLokalPosition += hwMap.crsKopfDrehen.getPower() * hwMap.KOPF_MAX_SPEED * dt;

            if (kopfManuell) {
                // Manueller Modus: Stick steuert die Power direkt
                hwMap.crsKopfDrehen.setPower(-gamepad2.right_stick_x);

                // Ziel-Rotation synchronisieren für nahtlosen Übergang zurück zu Auto
                targetGlobalRotation = aktuelleRotation + kopfLokalPosition;
            } else {
                // 2. Chassis-Stabilisierung (Ziel-Ansteuerung)
                double idealLokalPosition = targetGlobalRotation - aktuelleRotation;

                // Kürzesten Weg finden, der am nächsten an der aktuellen Position liegt
                while (idealLokalPosition - kopfLokalPosition > 180) idealLokalPosition -= 360;
                while (idealLokalPosition - kopfLokalPosition < -180) idealLokalPosition += 360;

                // Soft-Limits (±180°)
                double clampedLokalPosition = Math.max(-180, Math.min(180, idealLokalPosition));

                double error = clampedLokalPosition - kopfLokalPosition;

                // Berechnung der Ausgleichs-Power
                double stabilizationPower = (Math.abs(error) < 0.1) ? 0 : (error / dt / hwMap.KOPF_MAX_SPEED);

                hwMap.crsKopfDrehen.setPower(Math.max(-1.0, Math.min(1.0, stabilizationPower)));
            }
        } else {
            targetGlobalRotation = aktuelleRotation;
        }

        kopfLetzterUpdateZeitstempel = jetzt;
    }
}
