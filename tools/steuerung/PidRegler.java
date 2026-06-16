package org.firstinspires.ftc.teamcode.tools.steuerung;

public class PidRegler {
    public double kP;
    public double kI;
    public double kD;

    public double pidValue;
    private double errorSum = 0.0;
    public double lastError = 0.0;
    private long lastTime = 0;

    /**
     * @param p Proportional-Anteil (Standard-Reaktion auf den Fehler)
     * @param i Integral-Anteil (gleicht statische Abweichungen über die Zeit aus)
     * @param d Derivative-Anteil (dämpft Schwingungen bei schnellen Bewegungen)
     */
    public PidRegler(double p, double i, double d) {
        this.kP = p;
        this.kI = i;
        this.kD = d;
    }

    public void reset() {
        errorSum = 0.0;
        lastError = 0.0;
        lastTime = 0;
    }

    public void changeValues(double p, double i, double d) {
        this.kP = p;
        this.kI = i;
        this.kD = d;
    }

    public double step(double error) {
        double MAX_INTEGRAL = 1.0;

        error /= 180;

        long currentTime = System.nanoTime();
        // Zeitunterschied in Sekunden (Präzision im Nanosekundenbereich)
        double dt = (lastTime == 0) ? 0 : (currentTime - lastTime) / 1e9;
        lastTime = currentTime;

        // P-Anteil (Proportional)
        double pPart = kP * error;

        // I-Anteil (Integral) mit Schutz vor unendlichem Aufschwingen
        if (dt > 0) {
            errorSum += error * dt;
        }
        double iPart = Math.max(-MAX_INTEGRAL, Math.min(MAX_INTEGRAL, kI * errorSum));

        // D-Anteil (Dämpfung)
        double dPart = 0.0;
        if (dt > 0) {
            dPart = kD * (error - lastError) / dt;
        }
        lastError = error;

        // Gesamtsignal berechnen und auf FTC-Motorbereich (-1.0 bis 1.0) begrenzen
        pidValue = pPart + iPart + dPart;
        pidValue = Math.max(-1.0, Math.min(1.0, pidValue));
        return pidValue;
    }
}