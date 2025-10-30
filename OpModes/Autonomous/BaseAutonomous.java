package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.HwMap;

public abstract class BaseAutonomous extends LinearOpMode {
    protected HwMap hwMap; // hardware map

    /**
     * get the alliance color
     *
     * @return return if the alliance color is red
     */
    public boolean isRed() {
        return true;
    }

    public void driving_update() {
        while (opModeIsActive() && hwMap.navi.isDrivingToPosition()) {
            hwMap.robot.step();
            telemetry.addLine(hwMap.navi.debug());
            telemetry.addLine(hwMap.chassis.debug());
            telemetry.addLine(hwMap.navi.getAccProfile().debug());
            telemetry.update();
        }
    }

    /**
     * this gets executed when pressing the init button on the phone / driver hub
     */
    public void initialize() {
        // initialize the hardware map
        hwMap = new HwMap();
        hwMap.initialize(hardwareMap);

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
     * this internal methode is used to run initialize and run
     */
    public void runOpMode() {
        initialize();
        waitForStart();
        run();
        end();
    }
}
