package org.firstinspires.ftc.teamcode.opModes.autonomous;

public class BasisAutonomousFunktionen extends BasisAutonomous {
    private double targetGlobalRotation;
    private double kopfLokalPosition = 0;
    private long kopfLetzterUpdateZeitstempel = 0;

    public void starten() {
        super.starten();
        targetGlobalRotation = hwMap.chassis.getRotation();
        aktualisiereKopf();
    }

    public void schiessen() {
        aktualisiereKopf();
        hwMap.mSchiessen.setPower(hwMap.geschwindigkeitSchuss);
        schleifeWarten(500);
        hwMap.mInnen.setPower(1);
        schleifeWarten(2000);
        hwMap.mSchiessen.setPower(0);
        hwMap.mInnen.setPower(0);
    }

    public void aufnehmen() {
        hwMap.navi.setGeschwindigkeit(0.15, 0, 0);

        hwMap.mAufnehmen.setPower(hwMap.geschwindigkeitAufnehmen);
        hwMap.mInnen.setPower(0.5);
        schleifeWarten(2500);
        hwMap.mAufnehmen.setPower(0);
        hwMap.mInnen.setPower(0);

        hwMap.navi.setGeschwindigkeit(0, 0, 0);
    }

    private void aktualisiereKopf() {
        long jetzt = System.nanoTime();
        double aktuelleRotation = hwMap.chassis.getRotation();

        double dt = (jetzt - kopfLetzterUpdateZeitstempel) / 1e9;
        if (kopfLetzterUpdateZeitstempel != 0 && dt > 0) {
            // 1. Position-Tracking (Basiert auf der Power des letzten Zyklus)
            kopfLokalPosition += hwMap.crsKopfDrehen.getPower() * hwMap.KOPF_MAX_SPEED * dt;

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
        } else {
            targetGlobalRotation = aktuelleRotation;
        }
        kopfLetzterUpdateZeitstempel = jetzt;
    }
}
