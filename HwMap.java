package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Tools.AccelerationProfile;
import org.firstinspires.ftc.teamcode.Tools.Chassis.Chassis;
import org.firstinspires.ftc.teamcode.Tools.Chassis.MecanumChassis;
import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;
import org.firstinspires.ftc.teamcode.Tools.FieldNavigation;
import org.firstinspires.ftc.teamcode.Tools.PidController;
import org.firstinspires.ftc.teamcode.Tools.Robot;

public class HwMap {
    /* PLACE YOUR CONSTANT VALUES DOWN BELOW*/
    // robot
    public Robot robot;
    public FieldNavigation navi;
    public Chassis chassis;

    /* PLACE YOUR HARDWARE INTERFACES DOWN BELOW */
    /* END SECTION */
    public DcMotor m_schiessen_l;
    public DcMotor m_schiessen_r;
    public DcMotor m_aufnehmen;
    public DcMotor m_hoch;

    public CRServo s_unten;
    public CRServo s_oben;
    public Servo s_stop;

    /* END SECTION */

    public void initialize(HardwareMap hardwareMap) {
        // chassis
        chassis = new MecanumChassis(15, 17, new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.RIGHT));
        chassis.populateMotorArray(hardwareMap); // uses hardwareMap.get(...) to get motor interfaces as defined in the used chassis class
        chassis.setRotation(0.0); // start rotation is 0 degrees
        chassis.setDrivingEncoderStepsPerRotation(384.5); // 435 RPM: 384.5 & 223 RPM: 751.8

        // field navigation
        navi = new FieldNavigation(new Position2D(0.0, 0.0), new PidController(0.0, 0.0, 0.0)); // start position is (0|0) & PID values for rotation
        navi.setSpeedNormal(1);
        navi.setSpeedSneak(0.6);
        navi.setSpeedDrehen(0.3);
        navi.setAccelerationProfile(new AccelerationProfile(50, 0)); // create an acceleration profile for better location resolution
        navi.setRotationAccuracy(1.5); // in Grad
        navi.setDrivingAccuracy(3.0); // in cm

        // get robot api object
        robot = new Robot(navi, chassis);

        /* INITIALIZE YOUR HARDWARE DOWN BELOW */
        m_schiessen_l = hardwareMap.get(DcMotor.class, "schiessen_l");
        m_schiessen_r = hardwareMap.get(DcMotor.class, "schiessen_r");
        m_aufnehmen = hardwareMap.get(DcMotor.class, "aufnehmen");
        m_hoch = hardwareMap.get(DcMotor.class, "hoch");

        s_unten = hardwareMap.get(CRServo.class, "s_unten");
        s_oben = hardwareMap.get(CRServo.class, "s_oben");
        s_stop = hardwareMap.get(Servo.class, "s_stop");
        /* END SECTION */
    }
}
