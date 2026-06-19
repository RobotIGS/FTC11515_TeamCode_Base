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
    public boolean zweiSticks = false;

    /* ADD VARIABLES ONLY USED IN FULL CONTROL */
    // Kopfdrehen
    private boolean kopfManuell = false;
    private double kopfZielrotation;
    private double kopfRotation = 0;
    private long kopfLetzterUpdateZeitstempel = 0;

    private double rampeLokalPosition = 0.3;
    private long rampeLetzterUpdateZeitstempel = 0;

    /* END SECTION */

    @Override
    public void initialisieren() {
        super.initialisieren();
        hwMap = new HwMap(hardwareMap);
        /* ADD CODE WHICH IS RUN ONCE WHEN INIT IS PRESSED */

    }

    @Override
    public void runOnce() {
        /* ADD CODE WHICH IS RUN ONCE WHEN PLAY IS PRESSED */
        kopfZielrotation = hwMap.chassis.getRotation();
    }

    @Override
    public void runLoop() {
        fahren();
        saison();
        aktualisiereKopf();
        aktualisiereRampe();
        hwMap.navi.schritt();
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
        double vy = (zweiSticks ? -gamepad1.right_stick_x : -gamepad1.left_stick_x) * (hwMap.navi.sneak ? hwMap.navi.geschwindigkeitSneak : hwMap.navi.geschwindigkeitNormal);
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
        telemetry.addData("Sneak", hwMap.navi.sneak ? "JA" : "NEIN");
        telemetry.addData("Gegensteuern", hwMap.navi.fahreGegensteuern ? "JA" : "NEIN");
        telemetry.addData("Kopf Modus", kopfManuell ? "MANUELL" : "AUTO");
        telemetry.addLine();
        telemetry.addData("Schussgeschwindigkeit", hwMap.geschwindigkeitSchuss);
        telemetry.addLine();
        telemetry.addLine(hwMap.navi.debug());
        telemetry.addLine(hwMap.chassis.debug());
        telemetry.addLine(gegensteuernX.debug());
        telemetry.addLine(gegensteuernY.debug());
        telemetry.addLine();
        telemetry.addData("Kopf Ziel", kopfZielrotation);
        telemetry.addData("Kopf Position", kopfRotation);
        telemetry.update();
    }

    @Override
    public void saison() {
        // Schussgeschwindigkeit
        if (istTasteGedrueckt("gp2_rb", gamepad2.right_bumper)) {
            hwMap.geschwindigkeitSchuss = Math.min(1.0, hwMap.geschwindigkeitSchuss + 0.05);
        }
        if (istTasteGedrueckt("gp2_lb", gamepad2.left_bumper)) {
            hwMap.geschwindigkeitSchuss = Math.max(0.05, hwMap.geschwindigkeitSchuss - 0.05);
        }

        // motor aufnehmen
        if (istTasteGedrueckt("gp2_a", gamepad2.a)) {
            if (hwMap.mAufnehmen.getPower() == 0) {
                hwMap.mAufnehmen.setPower(1);
            } else {
                hwMap.mAufnehmen.setPower(0);
            }
        }

        // motor innen
        if (istTasteGedrueckt("gp2_b", gamepad2.b)) {
            if (hwMap.mInnen.getPower() == 0) {
                hwMap.mInnen.setPower(1);
            } else {
                hwMap.mInnen.setPower(0);
            }
        }

        // motor innen mond
        if (istTasteGedrueckt("gp2_y", gamepad2.y)) {
            if (hwMap.mInnenMond.getPower() == 0) {
                hwMap.mInnenMond.setPower(-1);
            } else {
                hwMap.mInnenMond.setPower(0);
            }
        }

        // motor schiessen
        if (istTasteGedrueckt("gp2_x", gamepad2.x)) {
            if (hwMap.mSchiessen.getPower() == 0) {
                hwMap.mSchiessen.setVelocity(hwMap.geschwindigkeitSchuss * HwMap.MotorWerte.ticksProSekundeErrechnen(HwMap.MotorWerte.M_6000_RPM));
            } else {
                hwMap.mSchiessen.setPower(0);
            }
        } else if (hwMap.geschwindigkeitSchuss * HwMap.MotorWerte.ticksProSekundeErrechnen(HwMap.MotorWerte.M_6000_RPM) != hwMap.mSchiessen.getVelocity() && hwMap.mSchiessen.getPower() != 0) {
            hwMap.mSchiessen.setVelocity(hwMap.geschwindigkeitSchuss * HwMap.MotorWerte.ticksProSekundeErrechnen(HwMap.MotorWerte.M_6000_RPM));
        }
    }

    private void aktualisiereKopf() {
        if (istTasteGedrueckt("gp2_rsb", gamepad2.left_stick_button)) {
            kopfManuell = !kopfManuell;
        }

        long jetzt = System.nanoTime();
        double aktuelleRotation = hwMap.chassis.getRotation();

        double dt = (jetzt - kopfLetzterUpdateZeitstempel) / 1e9;
        if (kopfLetzterUpdateZeitstempel != 0 && dt > 0) {
            // 1. Position-Tracking (Basiert auf der Power des letzten Zyklus)
            kopfRotation += hwMap.crsKopfDrehen.getPower() * hwMap.KOPF_MAX_SPEED * dt;

            if (kopfManuell) {
                // Manueller Modus: Stick steuert die Power direkt
                hwMap.crsKopfDrehen.setPower(-gamepad2.left_stick_x);

                // Target global normalisieren (-180 bis 180)
                while (kopfRotation > 180) kopfRotation -= 360;
                while (kopfRotation < -180) kopfRotation += 360;

                // Ziel-Rotation synchronisieren für nahtlosen Übergang zurück zu Auto
                kopfZielrotation = aktuelleRotation + kopfRotation;

                // Target global normalisieren (-180 bis 180)
                while (kopfZielrotation > 180) kopfZielrotation -= 360;
                while (kopfZielrotation < -180) kopfZielrotation += 360;
            } else {
                // 2. Chassis-Stabilisierung (Ziel-Ansteuerung)
                double idealLokalPosition = kopfZielrotation - aktuelleRotation;

                // Kürzesten Weg finden, der am nächsten an der aktuellen Position liegt
                while (idealLokalPosition - kopfRotation > 180) idealLokalPosition -= 360;
                while (idealLokalPosition - kopfRotation < -180) idealLokalPosition += 360;

                // Soft-Limits (±180°)
                double clampedLokalPosition = Math.max(-180, Math.min(180, idealLokalPosition));

                double error = clampedLokalPosition - kopfRotation;

                // Berechnung der Ausgleichs-Power
                double stabilizationPower = (Math.abs(error) < 0.1) ? 0 : (error / dt / hwMap.KOPF_MAX_SPEED);

                hwMap.crsKopfDrehen.setPower(Math.max(-1.0, Math.min(1.0, stabilizationPower)));
            }
        } else {
            kopfZielrotation = aktuelleRotation;
        }

        kopfLetzterUpdateZeitstempel = jetzt;
    }

    private void aktualisiereRampe() {
        long jetzt = System.nanoTime();
        double dt = (jetzt - rampeLetzterUpdateZeitstempel) / 1e9;

        if (rampeLetzterUpdateZeitstempel != 0 && dt > 0) {
            // Position-Tracking basierend auf der Power des letzten Zyklus
            rampeLokalPosition += hwMap.crsRampeL.getPower() * dt;

            // Limits einhalten (0.0 bis 0.3)
            rampeLokalPosition = Math.max(0.0, Math.min(0.3, rampeLokalPosition));

            double power = gamepad2.left_trigger - gamepad2.right_trigger;

            // Stop wenn Limit erreicht
            if (rampeLokalPosition <= 0.0 && power < 0) power = 0;
            if (rampeLokalPosition >= 0.3 && power > 0) power = 0;

            hwMap.crsRampeL.setPower(power);
            hwMap.crsRampeR.setPower(-power); // Invertiert für die andere Seite
        }

        rampeLetzterUpdateZeitstempel = jetzt;
    }
}
