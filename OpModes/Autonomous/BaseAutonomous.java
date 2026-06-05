package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Tools.HwMap;

import java.util.List;

public abstract class BaseAutonomous extends LinearOpMode {
    List<LynxModule> allHubs;
    public boolean isRed = true;
    protected HwMap hwMap;

    /**
     * this gets executed when pressing the init button on the phone / driver hub
     */
    public void initialize() {
        allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
            hub.clearBulkCache();
        }

        hwMap = new HwMap(hardwareMap);
    }

    /**
     * this gets executed when pressing the start button on the phone / driver hub
     */
    public void run() {
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }

    }

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

    public void telemetry() {
        telemetry.addLine(hwMap.navi.debug());
        telemetry.addLine(hwMap.chassis.debug());
        telemetry.addLine(hwMap.navi.getAccProfile().debug());
        telemetry.update();
    }

    public void loop_wait(int time_in_ms) {
        long start = System.currentTimeMillis();
        while ((System.currentTimeMillis() - start) < time_in_ms && opModeIsActive()) {
            for (LynxModule hub : allHubs) {
                hub.clearBulkCache();
            }
            hwMap.robot.step();
            telemetry();
        }
    }

    public void loop_while_driving() {
        while (opModeIsActive() && hwMap.navi.isDrivingToPosition()) {
            for (LynxModule hub : allHubs) {
                hub.clearBulkCache();
            }
            hwMap.robot.step();
            telemetry();
        }
    }
}
