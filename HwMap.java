package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Tools.Chassis.Chassis;
import org.firstinspires.ftc.teamcode.Tools.Chassis.MecanumChassis;
import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;
import org.firstinspires.ftc.teamcode.Tools.FieldNavigation;
import org.firstinspires.ftc.teamcode.Tools.AccelerationProfile;
import org.firstinspires.ftc.teamcode.Tools.PIDcontroller;
import org.firstinspires.ftc.teamcode.Tools.Robot;

public class HwMap {
    // robot
    public Robot robot;
    public FieldNavigation navi;
    public Chassis chassis;
    public AccelerationProfile accelerationProfile;

    /* PLACE YOUR HARDWARE INTERFACES DOWN BELOW */

    /* END SECTION */

    /* PLACE YOUR CONSTANT VALUES DOWN BELOW*/
    // driving speeds
    public final double speed_full = 1.0;
    public final double speed_normal = 0.8;
    public final double speed_sneak = 0.5;

    // autonomous values
    public final double driving_accuracy = 1.5;
    public final double rotation_accuracy = 3.0;
    /* END SECTION */

    /**
     * initialize the hardware
     * @param hardwareMap just put the object "hardwareMap" in here
     */
    public void initialize(HardwareMap hardwareMap) {
        // get chassis
        chassis = new MecanumChassis(2, 1, new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.FORWARD, RevHubOrientationOnRobot.UsbFacingDirection.LEFT)); // most likely your chassis is a mecanumwheel driven chassis
        chassis.populateMotorArray(hardwareMap); // uses hardwareMap.get(...) to get motor interfaces as defined in the used chassis class
        chassis.setRotation(0.0); // start rotation is 0 degrees

        // get field navigator
        navi = new FieldNavigation(new Position2D(0.0, 0.0), new PIDcontroller(0.001,0.001,0.0)); // start position is (0|0)
        this.accelerationProfile = new AccelerationProfile(50, 1); // create an acceleration profile for better location resolution
        navi.setProfile(accelerationProfile);
        navi.setAutoVelFactor(this.speed_normal);
        navi.setRotationAccuracy(this.rotation_accuracy);
        navi.setDrivingAccuracy(this.driving_accuracy);
        navi.setKeepRotation(false);

        // get robot api object
        robot = new Robot(navi, chassis);

        /* INITIALIZE YOUR HARDWARE DOWN BELOW */

        /* END SECTION */
    }
}
