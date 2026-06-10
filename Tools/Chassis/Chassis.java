package org.firstinspires.ftc.teamcode.Tools.Chassis;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Tools.Datatypes.Position2D;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Velocity;

/**
 * Jedes Chassis muss dieses Interface implementieren
 */
public interface Chassis {
    /**
     * Hardware-Interfaces initialisieren
     *
     * @param hwMap die Hardware-Map
     */
    void populateMotorArray(HardwareMap hwMap);

    /**
     * Setzt die Radgeschwindigkeiten entsprechend der gegebenen Geschwindigkeit
     *
     * @param geschwindigkeit die Chassis-Geschwindigkeit
     */
    void setVelocity(Velocity geschwindigkeit);

    /**
     * Gibt die gefahrene Distanz zurück, falls vom Chassis unterstützt
     *
     * @return Differenz der Position seit dem letzten Schritt oder (0|0)
     */
    Position2D getDrivenDistance();

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
    void stopMotors();

    /**
     * Gibt Debug-Informationen für das Chassis zurück
     *
     * @return ein String mit Debug-Infos
     */
    String debug();

    /**
     * Aktualisiert Zustände, berechnet Rotation und gefahrene Distanz neu, etc.
     */
    void step();

    /**
     * Setzt die Encoder-Schritte pro Umdrehung für das Fahren
     *
     * @param fahrEncoderSchritteProUmdrehung Schritte pro Umdrehung
     */
    void setDrivingEncoderStepsPerRotation(double fahrEncoderSchritteProUmdrehung);
}