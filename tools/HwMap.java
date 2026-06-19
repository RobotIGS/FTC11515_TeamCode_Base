package org.firstinspires.ftc.teamcode.tools;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.tools.chassis.Chassis;
import org.firstinspires.ftc.teamcode.tools.chassis.MecanumChassis;
import org.firstinspires.ftc.teamcode.tools.datentypen.Position2D;
import org.firstinspires.ftc.teamcode.tools.steuerung.BeschleunigungsProfil;
import org.firstinspires.ftc.teamcode.tools.steuerung.Navigation;
import org.firstinspires.ftc.teamcode.tools.steuerung.PidRegler;

public class HwMap {
    public final Navigation navi;
    public final Chassis chassis;
    public final VoltageSensor batterieSpannungsSensor;

    public static class MotorWerte {
        // jeder Motor mit seinen Rotationen pro Minute (RPM) und seinen Encoder Steps pro Umdrehung
        public static final double[] M_30_RPM = new double[]{30, 5281.1};
        public static final double[] M_117_RPM = new double[]{117, 1425.1};
        public static final double[] M_223_RPM = new double[]{223, 751.8};
        public static final double[] M_435_RPM = new double[]{435.0, 384.5};
        public static final double[] M_6000_RPM = new double[]{6000, 28};

        public static double ticksProSekundeErrechnen(double[] werte) {
            return (werte[0] / 60.0) * werte[1];
        }
    }

    /* PLACE YOUR HARDWARE INTERFACES AND VALUES DOWN BELOW */
    public double geschwindigkeitSchuss = 0.5;

    public final double KOPF_MAX_SPEED = 130.0; // Grad pro Sekunde

    public DcMotorEx mSchiessen;
    public DcMotorEx mInnen;
    public DcMotorEx mAufnehmen;
    public DcMotorEx mInnenMond;
    public CRServo crsKopfDrehen;
    public CRServo crsRampeL;
    public CRServo crsRampeR;

    public HwMap(com.qualcomm.robotcore.hardware.HardwareMap hardwareMap) {
        // Typische FTC-Robotergröße: ~45cm x 45cm.
        // lx: Seitlicher Abstand von der Mitte zum Rad
        // ly: Vorwärtsabstand von der Mitte zum Rad

        // vx Vorwärtsgeschwindigkeit (+ => vorwärts)
        // vy Seitwärtsgeschwindigkeit (+ => links)
        // vz Rotationsgeschwindigkeit (+ => nach links drehen => mathematisch positiv)

        chassis = new MecanumChassis(17, 17, new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD, RevHubOrientationOnRobot.UsbFacingDirection.UP));
        chassis.erstelleMotorArray(hardwareMap);
        chassis.setStartRotation(0.0);
        chassis.setMotorWerte(MotorWerte.M_435_RPM);

        navi = new Navigation(chassis, new Position2D(0.0, 0.0), new PidRegler(0.55, 0.01, 0.0));
        navi.setHalteRotation(false);
        navi.setGeschwindigkeitNormal(0.5);
        navi.setGeschwindigkeitSchleichend(0.3);
        navi.setGeschwindigkeitDrehen(1.0);
        navi.setGeschwindigkeitAuto(0.75);
        navi.setBeschleunigungsProfil(new BeschleunigungsProfil(30, 1)); // Beschleunigungsprofil für eine bessere Positionsbestimmung erstellen
        navi.setRotationsGenauigkeit(1.0); // in Grad
        navi.setFahrGenauigkeit(0.5); // in cm

        batterieSpannungsSensor = hardwareMap.voltageSensor.iterator().next();

        /* INITIALIZE YOUR HARDWARE DOWN BELOW */
        mSchiessen = hardwareMap.get(DcMotorEx.class, "m_schiessen");
        mAufnehmen = hardwareMap.get(DcMotorEx.class, "m_aufnehmen");
        mInnen = hardwareMap.get(DcMotorEx.class, "m_innen");
        mInnenMond = hardwareMap.get(DcMotorEx.class, "m_innen_mond");

        mSchiessen.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        mSchiessen.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        crsKopfDrehen = hardwareMap.get(CRServo.class, "crs_kopf");
        crsRampeL = hardwareMap.get(CRServo.class, "crs_rampe_l");
        crsRampeR = hardwareMap.get(CRServo.class, "crs_rampe_r");
    }
}