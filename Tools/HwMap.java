package org.firstinspires.ftc.teamcode.Tools;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Tools.Chassis.Chassis;
import org.firstinspires.ftc.teamcode.Tools.Chassis.MecanumChassis;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Position2D;
import org.firstinspires.ftc.teamcode.Tools.Steuerung.AccelerationProfile;
import org.firstinspires.ftc.teamcode.Tools.Steuerung.PidController;

public class HwMap {
    /* PLACE YOUR CONSTANT VALUES DOWN BELOW*/
    // robot
    public Robot robot;
    public FieldNavigation navi;
    public Chassis chassis;

    // season
    public double schussgeschwindigkeit = 0.7;
    public double s_kick_kurzposition = 0.65;
    public double s_kick_dauerposition = 0.23;
    public double aufnehm_geschwindigkeit = -0.05;

    /* END SECTION */
    /* PLACE YOUR HARDWARE INTERFACES DOWN BELOW */
    public DcMotor m_schiessen_l;
    public DcMotor m_schiessen_r;
    public DcMotor m_aufnehmen;
    public DcMotor m_hoch;

    public CRServo s_unten;
    public CRServo s_oben;
    public Servo s_kick;

    /* END SECTION */

    public HwMap(HardwareMap hardwareMap) {
        // chassis
        chassis = new MecanumChassis(1, 1, new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.RIGHT));
        chassis.populateMotorArray(hardwareMap); // uses hardwareMap.get(...) to get motor interfaces as defined in the used chassis class
        chassis.setRotation(0.0); // set start rotation
        chassis.setDrivingEncoderStepsPerRotation(384.5); // 435 RPM: 384.5 & 223 RPM: 751.8

        // field navigation
        navi = new FieldNavigation(new Position2D(0.0, 0.0), new PidController(0.0, 0.0, 0.0));
        navi.setSpeedNormal(0.6);
        navi.setSpeedSneak(0.3);
        navi.setSpeedDrehen(0.4);
        navi.setAccelerationProfile(new AccelerationProfile(50, 0)); // create an acceleration profile for better location resolution
        navi.setRotationAccuracy(2.0); // in Grad
        navi.setDrivingAccuracy(2.0); // in cm

        // get robot api object
        robot = new Robot(navi, chassis);

        /* INITIALIZE YOUR HARDWARE DOWN BELOW */
        m_schiessen_l = hardwareMap.get(DcMotor.class, "schiessen_l");
        m_schiessen_r = hardwareMap.get(DcMotor.class, "schiessen_r");
        m_aufnehmen = hardwareMap.get(DcMotor.class, "aufnehmen");
        m_hoch = hardwareMap.get(DcMotor.class, "hoch");

        s_unten = hardwareMap.get(CRServo.class, "s_unten");
        s_oben = hardwareMap.get(CRServo.class, "s_oben");
        s_kick = hardwareMap.get(Servo.class, "s_stop");
        /* END SECTION */
    }
}
