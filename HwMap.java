package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.Tools.Chassis.Chassis;
import org.firstinspires.ftc.teamcode.Tools.Chassis.MecanumChassis;
import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;
import org.firstinspires.ftc.teamcode.Tools.FieldNavigation;
import org.firstinspires.ftc.teamcode.Tools.Modules.SimpleLift;
import org.firstinspires.ftc.teamcode.Tools.Robot;

public class HwMap {
    // robot
    public Robot robot;
    public FieldNavigation navi;
    public Chassis chassis;

    /* PLACE YOUR HARDWARE INTERFACES DOWN BELOW */

    /* END SECTION */

    /* PLACE YOUR CONSTANT VALUES DOWN BELOW*/

    /* END SECTION */

    /**
     * initialize the hardware
     * @param hardwareMap just put the object "hardwareMap" in here
     */
    public void initialize(HardwareMap hardwareMap) {
        // get chassis
        chassis = new MecanumChassis(); // most likely your chassis is a mecanumwheel driven chassis
        chassis.setRotationAxis(1); /* change this if needed : the value can be obtained with OpModes.Testing.GyroTest */
        chassis.populateMotorArray(hardwareMap); // uses hardwareMap.get(...) to get motor interfaces as defined in the used chassis class
        chassis.setRotation(0.0f); // start rotation is 0 degrees

        // get field navigator
        navi = new FieldNavigation(new Position2D(0.0, 0.0)); // start position is (0|0)

        // get robot api object
        robot = new Robot(navi, chassis);

        /* INITIALIZE YOUR HARDWARE DOWN BELOW */

        /* END SECTION */
    }
}