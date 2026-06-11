package org.firstinspires.ftc.teamcode.tools.steuerung;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.teamcode.tools.datentypen.Position2D;

public class BeschleunigungsProfil {
    protected Position2D zielPosition;
    protected Position2D startPosition;
    protected Long startZeit;

    protected final double bremsDistanz;
    protected final double beschleunigungsZeit;

    protected double faktor;
    protected double distanz;
    protected double distanzZumStart;
    protected double distanzZumZiel;

    public BeschleunigungsProfil(double bremsDistanzInCm, double beschleunigungsZeitInS) {
        this.bremsDistanz = Math.abs(bremsDistanzInCm);
        this.beschleunigungsZeit = (long) (beschleunigungsZeitInS * 1000);
        this.startZeit = System.currentTimeMillis();
    }

    public void starten(Position2D start, Position2D ziel) {
        this.startPosition = start.copy();
        this.zielPosition = ziel.copy();
        this.startZeit = System.currentTimeMillis();
    }

    public double schritt(Position2D position) {
        if (this.zielPosition == null || this.startPosition == null) {
            return 1.0;
        }

        distanzZumZiel = position.copy().subtrahieren(this.zielPosition).getAbsolute();
        distanzZumStart = position.copy().subtrahieren(this.startPosition).getAbsolute();

        // Berechne Brems-Faktor (1 -> 0)
        double bremsFaktor = distanzZumZiel / bremsDistanz;

        // Berechne Anfahr-Faktor (0 -> 1)
        double anfahrFaktor = 1.0;
        if (beschleunigungsZeit > 0) {
            double zeitFortschritt = (double) (System.currentTimeMillis() - startZeit) / beschleunigungsZeit;
            anfahrFaktor = (zeitFortschritt < 1) ? -zeitFortschritt * (zeitFortschritt - 2) : 1.0;
        }

        // Der kleinste Faktor gewinnt (Trapez-Profil)
        faktor = Math.min(Math.min(anfahrFaktor, bremsFaktor), 1.0);
        return Math.max(0.1, faktor); // Nie ganz auf 0 fallen, damit er das Ziel erreicht
    }

    @SuppressLint("DefaultLocale")
    public String debug() {
        String ausgabe = "--- Beschleunigungsprofil ---\n";
        ausgabe += String.format("Wert: %+.4f\n", (faktor));
        ausgabe += String.format("Distanz: %+.4f\n", (distanz));
        ausgabe += String.format("Distanz zum START: %+.4f\n", (distanzZumStart));
        ausgabe += String.format("Distanz zum ZIEL: %+.4f\n", (distanzZumZiel));
        ausgabe += String.format("Zeit: %d\n", (System.currentTimeMillis() - startZeit));
        return ausgabe;
    }
}
