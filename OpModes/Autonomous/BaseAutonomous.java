package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Tools.HwMap;

public abstract class BaseAutonomous extends LinearOpMode {
    public boolean isRed = true;
    protected HwMap hwMap;

    public void loop_driving_update() {
        while (opModeIsActive() && hwMap.navi.isDrivingToPosition()) {
            hwMap.robot.step();
            telemetry.addLine(hwMap.navi.debug());
            telemetry.addLine(hwMap.chassis.debug());
            telemetry.addLine(hwMap.navi.getAccProfile().debug());
            telemetry.update();
        }
    }

    public void loop_wait(int time_in_ms) {
        long start = System.currentTimeMillis();
        while ((System.currentTimeMillis() - start) < time_in_ms && opModeIsActive()) {
        }
    }

    /**
     * this gets executed when pressing the init button on the phone / driver hub
     */
    public void initialize() {
        // initialize the hardware map
        hwMap = new HwMap(hardwareMap);

        /* OVERWRITE VALUES SET BY hwMap.initialize() */
        /* END SECTION */
    }

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

    /**
     * this internal methode is used to run the OpMode
     */
    public void runOpMode() {
        initialize();
        waitForStart();
        run();
        end();
    }
}
