package org.firstinspires.ftc.teamcode.opModes.autonomous;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.tools.HwMap;

import java.util.List;

public abstract class BasisAutonomous extends LinearOpMode {
    List<LynxModule> alleHubs;
    public boolean istRot = true;
    protected HwMap hwMap;

    public void initialisieren() {
        alleHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : alleHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
            hub.clearBulkCache();
        }

        hwMap = new HwMap(hardwareMap);
    }

    public void starten() {
        for (LynxModule hub : alleHubs) {
            hub.clearBulkCache();
        }
    }

    public void beenden() {
        hwMap.robot.stoppen();
    }

    public void runOpMode() {
        initialisieren();
        waitForStart();
        starten();
        beenden();
    }

    public void telemetrie() {
        telemetry.addLine(hwMap.navi.debug());
        telemetry.addLine(hwMap.chassis.debug());
        telemetry.addLine(hwMap.navi.getBeschleunigungsProfil().debug());
        telemetry.update();
    }

    public void schleifeWarten(int zeitInMs) {
        long start = System.currentTimeMillis();
        while ((System.currentTimeMillis() - start) < zeitInMs && opModeIsActive()) {
            for (LynxModule hub : alleHubs) {
                hub.clearBulkCache();
            }
            hwMap.robot.schritt();
            telemetrie();
        }
    }

    public void schleifeFahren() {
        while (opModeIsActive() && hwMap.navi.isPositionsfahren()) {
            for (LynxModule hub : alleHubs) {
                hub.clearBulkCache();
            }
            hwMap.robot.schritt();
            telemetrie();
        }
    }
}
