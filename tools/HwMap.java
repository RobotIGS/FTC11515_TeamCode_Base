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
import org.firstinspires.ftc.teamcode.tools.steuerung.FeldNavigation;
import org.firstinspires.ftc.teamcode.tools.steuerung.PidRegler;

public class HwMap {
    // robot
    public final Roboter robot;
    public final FeldNavigation navi;
    public final Chassis chassis;
    public final VoltageSensor batterieSpannungsSensor;

    /* PLACE YOUR HARDWARE INTERFACES AND VALUES DOWN BELOW */
    public double geschSchuss = 0.7;
    public final double geschAufnehmen = -1.0;
    public final double sKickBodenKurzposition = 0.9;
    public final double sKickBodenDauerposition = 0.55;

    public final double sKickSeiteKurzposition = 0.7;
    public final double sKickSeiteDauerposition = 0.15;

    public DcMotor mSchiessen;
    public DcMotor mBoden;
    public DcMotor mAufnehmen;
    public DcMotor mHoch;

    public Servo sKickSeite;
    public Servo sKickBoden;
    public CRServo crsRad;

    public HwMap(com.qualcomm.robotcore.hardware.HardwareMap hardwareMap) {
        // chassis
        // Typical FTC robot size: ~45cm x 45cm.
        // lx: sideways distance from center to wheel
        // ly: forward distance from center to wheel

        // vx forward speed (+ => forward)
        // vy sideways speed (+ => left)
        // wz rotation speed (+ => turn left => mathematisch positiv)

        chassis = new MecanumChassis(17, 17, new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.LEFT));
        chassis.erstelleMotorArray(hardwareMap); // uses hardwareMap.get(...) to get motor interfaces as defined in the used chassis class
        chassis.setStartRotation(0.0); // set start rotation
        chassis.setEncoderSchritteProUmdrehung(384.5); // 435RPM: 384.5 & 223RPM: 751.8

        // field navigation
        navi = new FeldNavigation(new Position2D(0.0, 0.0), new PidRegler(0.8, 0.0, 0.0));
        navi.setHalteRotation(true);
        navi.setGeschwindigkeitNormal(0.5);
        navi.setGeschwindigkeitSchleichend(0.3);
        navi.setGeschwindigkeitDrehen(1.0);
        navi.setGeschwindigkeitAuto(0.2);
        navi.setBeschleunigungsProfil(new BeschleunigungsProfil(20, 1)); // create an acceleration profile for better location resolution
        navi.setRotationsGenauigkeit(0.25); // in Grad
        navi.setFahrGenauigkeit(0.5); // in cm

        robot = new Roboter(navi, chassis);

        // Stromspannung
        batterieSpannungsSensor = hardwareMap.voltageSensor.iterator().hasNext() ? hardwareMap.voltageSensor.iterator().next() : null;

        try {
            /* INITIALIZE YOUR HARDWARE DOWN BELOW */
            mSchiessen = hardwareMap.get(DcMotor.class, "schiessen");
            mAufnehmen = hardwareMap.get(DcMotor.class, "aufnehmen");
            mBoden = hardwareMap.get(DcMotor.class, "boden");
            mHoch = hardwareMap.get(DcMotor.class, "hoch");

            sKickBoden = hardwareMap.get(Servo.class, "s_boden");
            sKickSeite = hardwareMap.get(Servo.class, "seite");
            crsRad = hardwareMap.get(CRServo.class, "rad");
        } catch (Exception ignored) {
        }
    }
}
