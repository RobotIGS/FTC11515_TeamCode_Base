package org.firstinspires.ftc.teamcode.tools;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.tools.chassis.Chassis;
import org.firstinspires.ftc.teamcode.tools.chassis.MecanumChassis;
import org.firstinspires.ftc.teamcode.tools.datentypen.Position2D;
import org.firstinspires.ftc.teamcode.tools.steuerung.BeschleunigungsProfil;
import org.firstinspires.ftc.teamcode.tools.steuerung.Navigation;
import org.firstinspires.ftc.teamcode.tools.steuerung.PidRegler;

public class HwMap {
    public final Roboter robot;
    public final Navigation navi;
    public final Chassis chassis;
    public final VoltageSensor batterieSpannungsSensor;

    private static class MotorSteps {
        public static final double M_435RPM = 384.5;
        public static final double M_223RPM = 751.8;
    }

    /* PLACE YOUR HARDWARE INTERFACES AND VALUES DOWN BELOW */
    public double geschwindigkeitSchuss = 0.7;
    public final double geschwindigkeitAufnehmen = -1.0;

    public final double KOPF_MAX_SPEED = 140.0; // Grad pro Sekunde

    public DcMotor mSchiessen;
    public DcMotor mInnen;
    public DcMotor mAufnehmen;
    public CRServo crsKopfDrehen;
    public Servo sRampeL;
    public Servo sRampeR;

    public HwMap(com.qualcomm.robotcore.hardware.HardwareMap hardwareMap) {
        // Typische FTC-Robotergröße: ~45cm x 45cm.
        // lx: Seitlicher Abstand von der Mitte zum Rad
        // ly: Vorwärtsabstand von der Mitte zum Rad

        // vx Vorwärtsgeschwindigkeit (+ => vorwärts)
        // vy Seitwärtsgeschwindigkeit (+ => links)
        // vz Rotationsgeschwindigkeit (+ => nach links drehen => mathematisch positiv)

        chassis = new MecanumChassis(17, 17, new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.LEFT));
        chassis.erstelleMotorArray(hardwareMap); // verwendet hardwareMap.get(...), um Motorschnittstellen zu erhalten, wie in der verwendeten Chassis-Klasse definiert
        chassis.setStartRotation(0.0);
        chassis.setEncoderSchritteProUmdrehung(MotorSteps.M_435RPM);

        navi = new Navigation(new Position2D(0.0, 0.0), new PidRegler(0.8, 0.0, 0.0));
        navi.setHalteRotation(false);
        navi.setGeschwindigkeitNormal(0.5);
        navi.setGeschwindigkeitSchleichend(0.3);
        navi.setGeschwindigkeitDrehen(1.0);
        navi.setGeschwindigkeitAuto(0.2);
        navi.setBeschleunigungsProfil(new BeschleunigungsProfil(20, 1)); // Beschleunigungsprofil für eine bessere Positionsbestimmung erstellen
        navi.setRotationsGenauigkeit(0.25); // in Grad
        navi.setFahrGenauigkeit(0.5); // in cm

        robot = new Roboter(navi, chassis);

        batterieSpannungsSensor = hardwareMap.voltageSensor.iterator().next();

        try {
            /* INITIALIZE YOUR HARDWARE DOWN BELOW */
            mSchiessen = hardwareMap.get(DcMotor.class, "m_schiessen");
            mAufnehmen = hardwareMap.get(DcMotor.class, "m_aufnehmen");
            mInnen = hardwareMap.get(DcMotor.class, "m_innen");

            crsKopfDrehen = hardwareMap.get(CRServo.class, "crs_kopf");

            sRampeL = hardwareMap.get(Servo.class, "s_rampe_l");
            sRampeR = hardwareMap.get(Servo.class, "s_rampe_r");
        } catch (Exception ignored) {
        }
    }

    public double getAnpassteSchussgeschwindigkeit() {
        double spannung = batterieSpannungsSensor.getVoltage();
        if (spannung < 1.0)
            return geschwindigkeitSchuss; // Schutz vor Division durch 0 oder unplausible Werte
        return Math.min(1.0, geschwindigkeitSchuss * (13.0 / spannung));
    }
}