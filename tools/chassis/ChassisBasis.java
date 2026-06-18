package org.firstinspires.ftc.teamcode.tools.chassis;

import android.annotation.SuppressLint;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.tools.HwMap;
import org.firstinspires.ftc.teamcode.tools.datentypen.Geschwindigkeit;
import org.firstinspires.ftc.teamcode.tools.datentypen.Position2D;
import org.firstinspires.ftc.teamcode.tools.datentypen.Rotation;

public abstract class ChassisBasis implements Chassis {
    private final DcMotorEx[] radMotoren;
    private final int[] radMotorSchritte;
    private final Rotation rotation;
    public IMU imu;
    protected double[] motorWerte;
    protected final RevHubOrientationOnRobot hubAusrichtung;
    protected Geschwindigkeit geschwindigkeit;
    protected final Position2D gefahreneDistanz;
    protected final double[] radGeschwindigkeiten;
    protected final int[] deltaRadMotorSchritte;
    private double startRotation;

    public ChassisBasis(int anzahlRaeder, RevHubOrientationOnRobot hubAusrichtung) {
        // Rotations-Einstellungen
        this.hubAusrichtung = hubAusrichtung;
        this.startRotation = 0;
        this.rotation = new Rotation(0.0);

        // Fahr-Einstellungen
        this.gefahreneDistanz = new Position2D();
        this.radMotoren = new DcMotorEx[anzahlRaeder];
        this.radGeschwindigkeiten = new double[anzahlRaeder];
        this.radMotorSchritte = new int[anzahlRaeder];
        this.deltaRadMotorSchritte = new int[anzahlRaeder];
    }

    @Override
    public void setMotorWerte(double[] motorWerte) {
        this.motorWerte = motorWerte;
    }

    @Override
    public void erstelleMotorArray(HardwareMap hwMap) {
        radMotoren[0] = hwMap.get(DcMotorEx.class, "front_left_motor");
        radMotoren[1] = hwMap.get(DcMotorEx.class, "front_right_motor");
        radMotoren[2] = hwMap.get(DcMotorEx.class, "back_left_motor");
        radMotoren[3] = hwMap.get(DcMotorEx.class, "back_right_motor");

        for (int i = 0; i < this.radMotoren.length; i++) {
            radGeschwindigkeiten[i] = 0.0;
            radMotoren[i].setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            radMotoren[i].setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            radMotorSchritte[i] = 0;
            deltaRadMotorSchritte[i] = 0;
        }

        // Motor Richtungen festlegen
        radMotoren[0].setDirection(DcMotorEx.Direction.REVERSE); // FL
        radMotoren[1].setDirection(DcMotorEx.Direction.FORWARD); // FR
        radMotoren[2].setDirection(DcMotorEx.Direction.FORWARD); // BL
        radMotoren[3].setDirection(DcMotorEx.Direction.REVERSE); // BR

        imu = hwMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(this.hubAusrichtung));
        imu.resetYaw();
    }

    private void setzeMotorGeschwindigkeiten() {
        for (int i = 0; i < radMotoren.length; i++) {
            radMotoren[i].setVelocity(radGeschwindigkeiten[i] * HwMap.MotorWerte.ticksProSekundeErrechnen(motorWerte));
        }
    }

    @Override
    public void setGeschwindigkeit(Geschwindigkeit geschwindigkeit) {
        this.geschwindigkeit = geschwindigkeit;
    }

    @Override
    public Position2D getGefahreneDistanz() {
        return gefahreneDistanz;
    }

    @Override
    public void stoppeMotoren() {
        setGeschwindigkeit(new Geschwindigkeit());
        setzeMotorGeschwindigkeiten();
    }

    private void updateMotorSchritte() {
        int schritte;
        for (int i = 0; i < deltaRadMotorSchritte.length; i++) {
            schritte = radMotoren[i].getCurrentPosition();
            deltaRadMotorSchritte[i] = schritte - radMotorSchritte[i];
            radMotorSchritte[i] = schritte;
        }
    }

    @Override
    public double getRotation() {
        return rotation.get();
    }

    @Override
    public void setStartRotation(double startRotation) {
        this.startRotation = -getRohRotation() + startRotation;
    }

    private double getRohRotation() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }

    private void berechneRotation() {
        rotation.set(getRohRotation() + startRotation);
    }

    @Override
    public void schritt() {
        berechneRotation();
        setzeMotorGeschwindigkeiten();
        updateMotorSchritte();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String debug() {
        StringBuilder ret = new StringBuilder("--- Chassis ---\n");
        ret.append(String.format("Gefahrene Distanz: x=%+2.2f y=%+2.2f\n", gefahreneDistanz.getX(), gefahreneDistanz.getY()));
        for (int i = 0; i < radMotoren.length; i++) {
            ret.append(String.format("Rad %d: v=%+1.2f  Schritte=%+5d  Delta Schritte=%+3d\n", i, radGeschwindigkeiten[i], radMotoren[i].getCurrentPosition(), deltaRadMotorSchritte[i]));
        }
        return ret.toString();
    }
}
