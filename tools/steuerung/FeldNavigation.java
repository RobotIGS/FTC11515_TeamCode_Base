package org.firstinspires.ftc.teamcode.tools.steuerung;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.teamcode.tools.datentypen.Geschwindigkeit;
import org.firstinspires.ftc.teamcode.tools.datentypen.Position2D;
import org.firstinspires.ftc.teamcode.tools.datentypen.Rotation;

public class FeldNavigation {
    public final static double PLATTENLAENGE = 365.75 / 6;
    private final Rotation aktuelleRotation;
    private final Rotation zielRotation;
    private final Geschwindigkeit geschwindigkeit;
    private final Position2D aktuellePosition;
    public final PidRegler rotationsPidRegler;
    public Position2D distanz;
    public boolean sneak;
    public boolean fahreGegensteuern;
    public double geschwindigkeitNormal;
    public double geschwindigkeitSneak;
    public double geschwindigkeitDrehen;
    public double geschwindigkeitAuto;
    private boolean positionsfahren;
    private boolean fahrenRotationHalten;
    private Position2D zielPosition;
    private double fahrGenauigkeit;
    private BeschleunigungsProfil beschleunigungsProfil;
    private double rotationsGenauigkeit;

    public FeldNavigation(Position2D aktuellePosition, PidRegler pidRegler) {
        this.positionsfahren = false;
        this.aktuellePosition = aktuellePosition;
        this.zielPosition = aktuellePosition.copy();

        this.aktuelleRotation = new Rotation(0.0);
        this.zielRotation = new Rotation(0.0);

        this.distanz = new Position2D();
        this.geschwindigkeit = new Geschwindigkeit();

        this.rotationsPidRegler = pidRegler;
        this.beschleunigungsProfil = null;

        this.sneak = true;
        this.fahreGegensteuern = true;
    }

    public boolean isPositionsfahren() {
        return this.positionsfahren;
    }

    public BeschleunigungsProfil getBeschleunigungsProfil() {
        return beschleunigungsProfil;
    }

    public Geschwindigkeit getGeschwindigkeit() {
        return geschwindigkeit;
    }

    public void setFahrGenauigkeit(double fahrGenauigkeit) {
        this.fahrGenauigkeit = fahrGenauigkeit;
    }

    public void setRotationsGenauigkeit(double rotationsGenauigkeit) {
        this.rotationsGenauigkeit = rotationsGenauigkeit;
    }

    public void setBeschleunigungsProfil(BeschleunigungsProfil beschleunigungsProfil) {
        this.beschleunigungsProfil = beschleunigungsProfil;
    }

    public void setzeZielPosition(Position2D p, boolean rel) {
        if (rel) {
            p.rotieren(this.aktuelleRotation.get());
            p.addieren(this.aktuellePosition);
            setzeZielPosition(p, false);
        } else {
            this.positionsfahren = true;
            this.zielPosition = p;
            if (this.beschleunigungsProfil != null) {
                this.beschleunigungsProfil.starten(this.aktuellePosition, this.zielPosition); // starte das Beschleunigungsprofil
            }
        }
    }

    public Position2D getZielPosition() {
        return zielPosition;
    }

    public void setAktuelleRotation(double rot) {
        aktuelleRotation.set(rot);
    }

    public void setZielRotation(double rotation, boolean relative) {
        rotationsPidRegler.reset();
        if (relative) {
            zielRotation.addieren(rotation);
        } else {
            zielRotation.set(rotation);
        }
    }

    public void setGeschwindigkeitNormal(double geschwindigkeitNormal) {
        this.geschwindigkeitNormal = Math.max(0, Math.min(1, geschwindigkeitNormal)); // faktor [0-1]
    }

    public void setGeschwindigkeitSchleichend(double geschwindigkeitSneak) {
        this.geschwindigkeitSneak = Math.max(0, Math.min(1, geschwindigkeitSneak)); // faktor [0-1]
    }

    public void setGeschwindigkeitDrehen(double geschwindigkeitDrehen) {
        this.geschwindigkeitDrehen = Math.max(0, Math.min(1, geschwindigkeitDrehen)); // faktor [0-1]
    }

    public void setGeschwindigkeitAuto(double geschwindigkeitAuto) {
        this.geschwindigkeitAuto = Math.max(0, Math.min(1, geschwindigkeitAuto)); // faktor [0-1]
    }

    public void setHalteRotation(boolean halteRotation) {
        this.fahrenRotationHalten = halteRotation;
    }

    public void addiereGefahreneDistanz(Position2D d) {
        Position2D d_rotiert = d.copy();
        d_rotiert.rotieren(aktuelleRotation.get());
        aktuellePosition.addieren(d_rotiert);
    }

    public void setGeschwindigkeit(double vx, double vy, double vz) {
        positionsfahren = false;
        this.geschwindigkeit.set(vx, vy, vz);
    }

    public void stoppen() {
        setGeschwindigkeit(0.0, 0.0, 0.0);
    }

    public void schritt() {
        if (positionsfahren) {
            // berechne die Distanz zur Zielposition
            this.distanz = zielPosition.copy();
            this.distanz.subtrahieren(aktuellePosition);

            // berechne den Fehler in der Rotation
            Rotation rotationsFehler = new Rotation(zielRotation.get());
            rotationsFehler.addieren(-aktuelleRotation.get());

            // setze die Geschwindigkeit für das Chassis
            double geschFactor = this.beschleunigungsProfil != null ? this.beschleunigungsProfil.schritt(this.aktuellePosition) * this.geschwindigkeitAuto : this.geschwindigkeitAuto;

            // berechne Geschwindigkeit für das Chassis
            Position2D distanz = this.distanz.getNormalization();
            distanz.rotieren(-this.aktuelleRotation.get());

            // prüfe, ob im Bereich der Zielposition (erreicht)
            if ((Math.abs(this.distanz.getAbsolute()) <= this.fahrGenauigkeit && !fahrenRotationHalten) ||
                    (Math.abs(this.distanz.getAbsolute()) <= this.fahrGenauigkeit && fahrenRotationHalten
                            && Math.abs(rotationsFehler.get()) <= rotationsGenauigkeit)) {
                stoppen();

            } else if (true) { // wenn seitwärts erlaubt: einfach in die Richtung fahren und drehen
                geschwindigkeit.set(
                        distanz.getX() * geschFactor,
                        distanz.getY() * geschFactor,
                        fahrenRotationHalten ? rotationsPidRegler.step(rotationsFehler.get()) : 0.0
                );
            } else if (true) { // wenn Rotation erlaubt: einfach vorwärts in die Richtung fahren und zum Ziel drehen
                if (this.distanz.getAbsolute() > this.fahrGenauigkeit) {
                    rotationsFehler.set(Math.toDegrees(Math.asin(distanz.getY())));
                    if (distanz.getX() < 0) {
                        rotationsFehler.set(180 - rotationsFehler.get());
                    }
                }

                geschwindigkeit.set(
                        distanz.getX() * geschFactor,
                        0.0,
                        rotationsPidRegler.step(rotationsFehler.get())
                );
            }
        }
    }

    @SuppressLint("DefaultLocale")
    public String debug() {
        String ret = "--- FeldNavigation ---\n";
        ret += String.format("Position: x=%+3.1f y=%+3.1f rot: %+1.2f\n", aktuellePosition.getX(), aktuellePosition.getY(), aktuelleRotation.get());
        ret += String.format("Geschwindigkeit: x=%+1.2f y=%+1.2f wz=%+1.2f\n", geschwindigkeit.getVX(), geschwindigkeit.getVY(), geschwindigkeit.getVZ());
        if (this.positionsfahren) {
            ret += "Positionsfahren: True\n";
            ret += String.format("   Ziel: x=%+3.1f y=%+3.1f rot=%+1.2f\n", zielPosition.getX(), zielPosition.getY(), zielRotation.get());
            ret += String.format("   Distanz: x=%+3.1f y=%+3.1f\n", this.distanz.getX(), this.distanz.getY());
        } else {
            ret += "Positionsfahren: False\n";
        }
        return ret;
    }
}
