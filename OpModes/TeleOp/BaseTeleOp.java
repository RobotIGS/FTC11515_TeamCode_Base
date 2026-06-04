package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Tools.HwMap;

import java.util.List;

public abstract class BaseTeleOp extends LinearOpMode {
    List<LynxModule> allHubs;

    protected HwMap hwMap; // hardware map

    public void driving() {
    }

    public void season() {
    }

    public void telemetry() {
    }

    public void initialize() { // this gets executed when pressing the init button on the driver hub
        allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        hwMap = new HwMap(hardwareMap);
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
        while (opModeIsActive()) {
            for (LynxModule hub : allHubs) {
                hub.clearBulkCache();
            }
            runLoop();
        }
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

    private java.util.Map<String, Boolean> buttonStates = new java.util.HashMap<>();

    protected boolean isButtonPressed(String buttonId, boolean isPressed) {
        boolean wasPressed = Boolean.TRUE.equals(buttonStates.getOrDefault(buttonId, false));
        buttonStates.put(buttonId, isPressed);
        return isPressed && !wasPressed;
    }
}