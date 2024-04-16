package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.HwMap;
import org.firstinspires.ftc.teamcode.Tools.Profile;

public abstract class BaseAutonomous extends LinearOpMode {
    protected HwMap hwMap; // hardware map
    protected Profile accelerationProfile;

    /**
     * get the alliance color
     * @return return if the alliance color is red
     */
    public boolean isRed() {
        return true;
    }

    /**
     * this gets executed when pressing the init button on the phone / driver hub
     */
    public void initialize() {
        // initialize the hardware map
        hwMap = new HwMap();
        hwMap.initialize(hardwareMap);

        // create an acceleration profile for better location resolution
        this.accelerationProfile = new Profile(100);

        /* OVERWRITE VALUES SET BY hwMap.initialize() DOWN BELOW */
        hwMap.navi.setProfile(accelerationProfile);
        hwMap.navi.setAutoVelFactor(0.3);
        hwMap.navi.setRotation_accuracy(3.0f);
        hwMap.navi.setDriving_accuracy(1.5);
        hwMap.navi.setKeepRotation(true);
        /* END SECTION */
    };

    /**
     * this gets executed when pressing the start button on the phone / driver hub
     */
    public abstract void run();

    /**
     * this gets executed at the end
     */
    public void end() {
        hwMap.robot.stop();
    }

    /* this internal methode is used to run initialize and run */
    public void runOpMode() {
        initialize();
        waitForStart();
        run();
        end();
    }
}
