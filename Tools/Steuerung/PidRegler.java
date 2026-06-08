package org.firstinspires.ftc.teamcode.Tools.Steuerung;

public class PidRegler {
    private double kP;
    private double kI;
    private double kD;

    double pidValue;
    private double errorSum = 0.0;
    double lastError = 0.0;
    private long lastTime = 0;

    // Begrenzung für das I-Glied, um unkontrolliertes Aufschwingen zu verhindern (Anti-Windup)
    private double maxIntegral = 0.2;

    /**
     * Erstellt einen neuen PID-Regler.
     *
     * @param p Proportional-Anteil (Standard-Reaktion auf den Fehler)
     * @param i Integral-Anteil (gleicht statische Abweichungen über die Zeit aus)
     * @param d Derivative-Anteil (dämpft Schwingungen bei schnellen Bewegungen)
     */
    public PidRegler(double p, double i, double d) {
        this.kP = p;
        this.kI = i;
        this.kD = d;
    }


    /**
     * Setzt den internen Zustand zurück. Vor jeder neuen Drehung aufrufen!
     */
    public void reset() {
        errorSum = 0.0;
        lastError = 0.0;
        lastTime = 0;
    }

    /**
     * Berechnet die Motorleistung basierend auf dem direkt übergebenen Fehler.
     * Diese Variante ist universell einsetzbar (auch für Distanzen, Lift-Systeme etc.).
     *
     * @param error Der aktuelle Fehler (Sollwert - Istwert)
     * @return Motorleistung für die Korrektur (-1.0 bis 1.0)
     */
    public double step(double error) {
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
        double iPart = Math.max(-maxIntegral, Math.min(maxIntegral, kI * errorSum));

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

    /**
     * Setzt das maximale Limit für den Integral-Anteil (Anti-Windup).
     */
    public void setIntegralLimit(double limit) {

    }
}