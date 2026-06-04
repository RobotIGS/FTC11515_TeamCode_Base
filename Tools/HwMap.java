package org.firstinspires.ftc.teamcode.Tools;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.Tools.Chassis.Chassis;
import org.firstinspires.ftc.teamcode.Tools.Chassis.MecanumChassis;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Position2D;
import org.firstinspires.ftc.teamcode.Tools.Steuerung.AccelerationProfile;
import org.firstinspires.ftc.teamcode.Tools.Steuerung.FieldNavigation;
import org.firstinspires.ftc.teamcode.Tools.Steuerung.PidController;

public class HwMap {
    /* PLACE YOUR CONSTANT VALUES DOWN BELOW*/
    // robot
    public Robot robot;
    public FieldNavigation navi;
    public Chassis chassis;
    public VoltageSensor batteryVoltageSensor;

    /* PLACE YOUR HARDWARE INTERFACES AND VALUES DOWN BELOW */
    public double gesch_schuss = 0.7;
    public double gesch_aufnehmen = -1.0;
    public double s_kick_boden_kurzposition = 0.9;
    public double s_kick_boden_dauerposition = 0.55;

    public double s_kick_seite_kurzposition = 0.7;
    public double s_kick_seite_dauerposition = 0.15;

    public DcMotor m_schiessen;
    public DcMotor m_boden;
    public DcMotor m_aufnehmen;
    public DcMotor m_hoch;

    public Servo s_kick_seite;
    public Servo s_kick_boden;
    public CRServo crs_rad;
    /* END SECTION */

    public HwMap(HardwareMap hardwareMap) {
        // chassis
        // Typical FTC robot size: ~45cm x 45cm. 
        // lx: sideways distance from center to wheel (e.g. 18cm)
        // ly: forward distance from center to wheel (e.g. 15cm)

        // vx forward speed (+ => forward)
        // vy sideways speed (+ => left)
        // wz rotation speed (+ => turn left => mathematisch positiv)

        chassis = new MecanumChassis(17, 17, new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));
        chassis.populateMotorArray(hardwareMap); // uses hardwareMap.get(...) to get motor interfaces as defined in the used chassis class
        chassis.setStartRotation(0.0); // set start rotation
        chassis.setDrivingEncoderStepsPerRotation(384.5); // 435RPM: 384.5 & 223RPM: 751.8

        // field navigation
        navi = new FieldNavigation(new Position2D(0.0, 0.0), new PidController(0.02, 0.0, 0.001));
        navi.setKeepRotation(true);
        navi.setSpeedNormal(0.5);
        navi.setSpeedSneak(0.3);
        navi.setSpeedDrehen(1.0);
        navi.setSpeedAuto(0.2);
        navi.setAccelerationProfile(new AccelerationProfile(25, 1)); // create an acceleration profile for better location resolution
        navi.setRotationAccuracy(2.0); // in Grad
        navi.setDrivingAccuracy(2.0); // in cm

        robot = new Robot(navi, chassis);

        // Stromspannung
        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().hasNext() ? hardwareMap.voltageSensor.iterator().next() : null;

        /* INITIALIZE YOUR HARDWARE DOWN BELOW */
        m_schiessen = hardwareMap.get(DcMotor.class, "schiessen");
        m_aufnehmen = hardwareMap.get(DcMotor.class, "aufnehmen");
        m_boden = hardwareMap.get(DcMotor.class, "boden");
        m_hoch = hardwareMap.get(DcMotor.class, "hoch");

        s_kick_boden = hardwareMap.get(Servo.class, "s_boden");
        s_kick_seite = hardwareMap.get(Servo.class, "seite");
        crs_rad = hardwareMap.get(CRServo.class, "rad");
        /* END SECTION */
    }
}
