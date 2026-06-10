package org.firstinspires.ftc.teamcode.Tools.Chassis;

import android.annotation.SuppressLint;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Position2D;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Rotation;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Velocity;

public abstract class ChassisBase implements Chassis {
    private final DcMotor[] radMotoren;
    private final int[] radMotorSchritte;
    private final Rotation rotation;
    public IMU imu;
    protected double fahrEncoderSchritteProUmdrehung;
    protected RevHubOrientationOnRobot hubAusrichtungAmRobot;
    protected Velocity geschwindigkeit;
    protected Position2D gefahreneDistanz;
    protected double[] radGeschwindigkeiten;
    protected int[] deltaRadMotorSchritte;
    private double startRotation;

    public ChassisBase(int anzahlRaeder, RevHubOrientationOnRobot hubAusrichtung) {
        // Rotations-Einstellungen
        this.hubAusrichtungAmRobot = hubAusrichtung;
        this.startRotation = 0;
        this.rotation = new Rotation(0.0);

        // Fahr-Einstellungen
        this.gefahreneDistanz = new Position2D();
        this.radMotoren = new DcMotor[anzahlRaeder];
        this.radGeschwindigkeiten = new double[anzahlRaeder];
        this.radMotorSchritte = new int[anzahlRaeder];
        this.deltaRadMotorSchritte = new int[anzahlRaeder];
    }

    public void setDrivingEncoderStepsPerRotation(double fahrEncoderSchritteProUmdrehung) {
        this.fahrEncoderSchritteProUmdrehung = fahrEncoderSchritteProUmdrehung;
    }

    public void populateMotorArray(HardwareMap hwMap) {
        radMotoren[0] = hwMap.get(DcMotor.class, "front_left_motor");
        radMotoren[1] = hwMap.get(DcMotor.class, "front_right_motor");
        radMotoren[2] = hwMap.get(DcMotor.class, "back_left_motor");
        radMotoren[3] = hwMap.get(DcMotor.class, "back_right_motor");

        for (int i = 0; i < this.radMotoren.length; i++) {
            radGeschwindigkeiten[i] = 0.0;
            radMotoren[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            radMotoren[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            radMotorSchritte[i] = 0;
            deltaRadMotorSchritte[i] = 0;
        }

        // Motor Richtungen festelegen
        radMotoren[0].setDirection(DcMotorSimple.Direction.REVERSE); // FL
        radMotoren[1].setDirection(DcMotorSimple.Direction.FORWARD); // FR
        radMotoren[2].setDirection(DcMotorSimple.Direction.FORWARD); // BL
        radMotoren[3].setDirection(DcMotorSimple.Direction.REVERSE); // BR

        imu = hwMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(this.hubAusrichtungAmRobot));
        imu.resetYaw();
    }

    private void setMotorSpeeds() {
        for (int i = 0; i < radMotoren.length; i++) {
            radMotoren[i].setPower(radGeschwindigkeiten[i]);
        }
    }

    public void setVelocity(Velocity geschwindigkeit) {
        this.geschwindigkeit = geschwindigkeit;
    }

    public Position2D getDrivenDistance() {
        return gefahreneDistanz;
    }

    public void stopMotors() {
        setVelocity(new Velocity());
        setMotorSpeeds();
    }

    private void updateMotorSteps() {
        int schritte;
        for (int i = 0; i < deltaRadMotorSchritte.length; i++) {
            schritte = radMotoren[i].getCurrentPosition();
            deltaRadMotorSchritte[i] = schritte - radMotorSchritte[i];
            radMotorSchritte[i] = schritte;
        }
    }

    public double getRotation() {
        return rotation.get();
    }

    public void setStartRotation(double startRotation) {
        this.startRotation = -getRawRotation() + startRotation;
    }

    private double getRawRotation() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }

    private void calculateRotation() {
        rotation.set(getRawRotation() + startRotation);
    }

    public void step() {
        calculateRotation();
        setMotorSpeeds();
        updateMotorSteps();
    }

    @SuppressLint("DefaultLocale")
    public String debug() {
        String ret = "--- Chassis Debug ---\n";
        if (geschwindigkeit != null)
            ret += String.format("Geschwindigkeit: vx=%+1.2f vy=%+1.2f wz=%+1.2f\n", geschwindigkeit.getVX(), geschwindigkeit.getVY(), geschwindigkeit.getWZ());
        if (gefahreneDistanz != null)
            ret += String.format("Gefahrene Distanz: x=%+2.2f y=%+2.2f\n", gefahreneDistanz.getX(), gefahreneDistanz.getY());

        // Rad-Debug hinzufügen
        for (int i = 0; i < radMotoren.length; i++) {
            ret += String.format("Rad %d: v=%+1.2f  Schritte=%+5d  Delta Schritte=%+3d\n", i, radGeschwindigkeiten[i], radMotoren[i].getCurrentPosition(), deltaRadMotorSchritte[i]);
        }

        return ret;
    }
}