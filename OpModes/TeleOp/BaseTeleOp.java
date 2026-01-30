package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Tools.HwMap;

public abstract class BaseTeleOp extends LinearOpMode {
    protected HwMap hwMap; // hardware map

    public void driving() {
    }

    public void season() {
    }

    public void telemetry() {
    }

    public void initialize() { // this gets executed when pressing the init button on the driver hub
        // initialize the hardware map
        hwMap = new HwMap(hardwareMap);

        /* OVERWRITE VALUES SET BY hwMap.initialize() DOWN BELOW */
        /* END SECTION */
    }

    public abstract void runOnce(); // this gets executed once when play button is pressed on the driver hub

    public abstract void runLoop(); // this gets executed in a loop when the play button is pressed on the driver hub

    public void end() { // this gets executed after the loop was stopped
        hwMap.robot.stop();
    }

    public void runOpMode() { // this internal methode is used to run initialize and run
        initialize();
        waitForStart();
        runOnce();
        while (opModeIsActive())
            runLoop();
        end();
    }

    public void loop_wait(int time_in_ms) {
        long start = System.currentTimeMillis();
        while ((System.currentTimeMillis() - start) < time_in_ms && opModeIsActive()) {
            driving();
            hwMap.robot.step();
            telemetry();
        }
    }

    public void loop_while_driving() {
        while (opModeIsActive() && hwMap.navi.isDrivingToPosition()) {
            hwMap.robot.step();
        }
    }
}