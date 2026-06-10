package org.firstinspires.ftc.teamcode.Tools.Steuerung;

import android.annotation.SuppressLint;

/**
 * Hochperformante und zeitkonsistente Klasse zum Gegensteuern/Bremsen.
 * Optimiert für FTC-Control-Hubs: Vermeidet rechenintensive Funktionen wie Math.exp()
 * und Math.pow() zugunsten einfacher mathematischer Operationen.
 */
public class Gegensteuern {
    private final double MAX_BREMSZEIT = 750.0; // in Millisekunden
    private final double JOYSTICK_DEADZONE = 0.02;

    // Zeitkonstante tau (in Sekunden). 0.1s entspricht einem Reaktionsfenster von ca. 100ms.
    private final double FILTER_TAU = 0.10;

    private final String richtung;

    private long lastTime = 0;
    private long aktuelleBremsDauer = 0;
    private double durchschnitt = 0;
    private double basisBremsKraft = 0;
    private double endwert = 0;
    private long bremszeitEnde = 0;

    public Gegensteuern(String richtung) {
        this.richtung = richtung;
    }

    public double calculate(boolean geg, double steuer_alt, double steuer_neu) {
        final long now = System.currentTimeMillis();

        // 1. Delta-Time (dt) bestimmen
        if (lastTime == 0) {
            lastTime = now;
        }
        double dt = (now - lastTime) / 1000.0;
        lastTime = now;

        // Sicherheits-Check für extremen Lag (z.B. nach dem Init)
        if (dt > 0.2) {
            dt = 0.05;
        }

        if (!geg) {
            bremszeitEnde = 0;
            durchschnitt = steuer_neu; // Filter direkt auf Ziel setzen
            basisBremsKraft = 0;
            return steuer_neu;
        }

        final double alpha = dt / (FILTER_TAU + dt);
        durchschnitt = (alpha * steuer_neu) + ((1.0 - alpha) * durchschnitt);

        final double absNeu = Math.abs(steuer_neu);

        if (absNeu > JOYSTICK_DEADZONE) {
            // Abbruch: Fahrer steuert wieder aktiv selbst
            bremszeitEnde = 0;
            basisBremsKraft = 0;
            endwert = steuer_neu;
        } else if (Math.abs(steuer_alt) > JOYSTICK_DEADZONE && bremszeitEnde <= now) {
            // Aktivierung: Stick wurde gerade losgelassen und Bremsung läuft noch nicht
            // Optimiert: durchschnitt * durchschnitt statt Math.pow()
            aktuelleBremsDauer = Math.round(MAX_BREMSZEIT * (durchschnitt * durchschnitt));
            if (aktuelleBremsDauer > 0) {
                bremszeitEnde = now + aktuelleBremsDauer;
                basisBremsKraft = durchschnitt;
            } else {
                basisBremsKraft = 0;
            }
            endwert = steuer_neu;
        } else if (now < bremszeitEnde) {
            // Aktive Bremsrampe: Gegenkraft sinkt linear auf 0
            final long restZeit = bremszeitEnde - now;
            final double rampenFaktor = (double) restZeit / aktuelleBremsDauer;

            // Keine Begrenzung nötig, da 'basisBremsKraft' mathematisch nie [-1.0, 1.0] überschreiten kann
            endwert = -basisBremsKraft * rampenFaktor;
        } else {
            // Normaler Stillstand
            basisBremsKraft = 0;
            endwert = steuer_neu;
        }

        return endwert;
    }

    @SuppressLint("DefaultLocale")
    public String debug() {
        final long now = System.currentTimeMillis();
        final boolean isBraking = now < bremszeitEnde;
        final double rampenFaktor = isBraking ? (double) (bremszeitEnde - now) / aktuelleBremsDauer : 0.0;

        return "--- Gegensteuern Debug " + this.richtung + " ---\n" +
                String.format("Status       : %s\n", isBraking ? "bremsen" : "nicht aktiv") +
                String.format("Durchschnitt : %+1.4f\n", durchschnitt) +
                String.format("Basis-Kraft  : %+1.4f\n", basisBremsKraft) +
                String.format("Rampe Faktor : %1.2f\n", rampenFaktor) +
                String.format("Rückgabe     : %+1.4f\n", endwert);
    }
}