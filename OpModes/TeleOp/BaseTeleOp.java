package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.HwMap;

public abstract class BaseTeleOp extends LinearOpMode {
    protected HwMap hwMap; // hardware map

    /**
     * this gets executed when pressing the init button on the phone / driver hub
     */
    public void initialize() {
        // initialize the hardware map
        hwMap = new HwMap();
        hwMap.initialize(hardwareMap);

        /* OVERWRITE VALUES SET BY hwMap.initialize() DOWN BELOW */
        /* END SECTION */
    }

    /**
     * this gets executed once when play button is pressed on the phone / driver hub
     */
    public abstract void runOnce();

    /**
     * this gets executed in a loop when the play button is pressed on the phone / driver hub
     */
    public abstract void runLoop();

    /**
     * this gets executed after the loop was stopped
     */
    public void end() {
        hwMap.robot.stop();
    }

    /* this internal methode is used to run initialize and run */
    public void runOpMode() {
        initialize();
        waitForStart();
        runOnce();
        while (opModeIsActive())
            runLoop();
        end();
    }
}