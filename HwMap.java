package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Tools.AccelerationProfile;
import org.firstinspires.ftc.teamcode.Tools.Chassis.Chassis;
import org.firstinspires.ftc.teamcode.Tools.Chassis.MecanumChassis;
import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;
import org.firstinspires.ftc.teamcode.Tools.FieldNavigation;
import org.firstinspires.ftc.teamcode.Tools.PIDcontroller;
import org.firstinspires.ftc.teamcode.Tools.Robot;

public class HwMap {
    /* PLACE YOUR CONSTANT VALUES DOWN BELOW*/
    // Encoder Steps pro Rotation eines Fahrmotors
    // 435 RPM: 384.5
    // 223 RPM: 751.8
    public static final double driving_encoder_steps_per_rotation = 384.5;

    // driving speeds
    public final double speed_normal = 0.3;
    public final double speed_sneak = 0.1;
    // autonomous values
    public final double driving_accuracy = 1.5; // in cm
    public final double rotation_accuracy = 3.0; // in Grad

    // robot
    public Robot robot;
    public FieldNavigation navi;
    public Chassis chassis;

    /* PLACE YOUR HARDWARE INTERFACES DOWN BELOW */
    /* END SECTION */
    public DcMotor m_schiessen_l;
    public DcMotor m_schiessen_r;
    public DcMotor m_kette;

    /* END SECTION */

    /**
     * initialize the hardware
     *
     * @param hardwareMap just put the object "hardwareMap" in here
     */
    public void initialize(HardwareMap hardwareMap) {
        // get chassis
        chassis = new MecanumChassis(1, 1, new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.RIGHT));
        chassis.populateMotorArray(hardwareMap); // uses hardwareMap.get(...) to get motor interfaces as defined in the used chassis class
        chassis.setRotation(0.0); // start rotation is 0 degrees

        // get field navigator
        navi = new FieldNavigation(new Position2D(0.0, 0.0), new PIDcontroller(0.0, 0.0, 0.0)); // start position is (0|0) & PID values
        navi.setAccelerationProfile(new AccelerationProfile(50, 0)); // create an acceleration profile for better location resolution
        navi.setAutoVelFactor(this.speed_normal);
        navi.setRotationAccuracy(this.rotation_accuracy);
        navi.setDrivingAccuracy(this.driving_accuracy);
        navi.setKeepRotation(false);

        // get robot api object
        robot = new Robot(navi, chassis);

        /* INITIALIZE YOUR HARDWARE DOWN BELOW */
        m_schiessen_l = hardwareMap.get(DcMotor.class, "schiessen_l");
        m_schiessen_r = hardwareMap.get(DcMotor.class, "schiessen_r");
        m_kette = hardwareMap.get(DcMotor.class, "kette");
        /* END SECTION */
    }
}
