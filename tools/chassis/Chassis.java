package org.firstinspires.ftc.teamcode.tools.chassis;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.tools.datentypen.Geschwindigkeit;
import org.firstinspires.ftc.teamcode.tools.datentypen.Position2D;

/**
 * Jedes Chassis muss dieses Interface implementieren
 */
public interface Chassis {
    /**
     * Hardware-Interfaces initialisieren
     *
     * @param hwMap die Hardware-Karte
     */
    void erstelleMotorArray(HardwareMap hwMap);

    /**
     * Setzt die Radgeschwindigkeiten entsprechend der gegebenen Geschwindigkeit
     *
     * @param geschwindigkeit die Chassis-Geschwindigkeit
     */
    void setGeschwindigkeit(Geschwindigkeit geschwindigkeit);

    /**
     * Gibt die gefahrene Distanz zurück, falls vom Chassis unterstützt
     *
     * @return Differenz der Position seit dem letzten Schritt oder (0|0)
     */
    Position2D getGefahreneDistanz();

    /**
     * Gibt die Rotation des Chassis auf dem Feld zurück, falls unterstützt
     *
     * @return die Rotation des Chassis oder 0
     */
    double getRotation();

    /**
     * Setzt die Start-Rotation des Chassis auf dem Feld
     *
     * @param rotation die Rotation in [0;360]
     */
    void setStartRotation(double rotation);


    /**
     * Stoppt alle Motorbewegungen
     */
    void stoppeMotoren();

    /**
     * Gibt Debug-Informationen für das Chassis zurück
     *
     * @return ein String mit Debug-Infos
     */
    String debug();

    /**
     * Aktualisiert Zustände, berechnet Rotation und gefahrene Distanz neu, etc.
     */
    void schritt();

    /**
     * Setzt die Encoder-Schritte pro Umdrehung für das Fahren
     *
     * @param motorWerte Schritte pro Umdrehung
     */
    void setMotorWerte(double[] motorWerte);
}
