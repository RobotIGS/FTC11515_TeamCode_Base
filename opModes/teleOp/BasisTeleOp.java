package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.tools.HwMap;

import java.util.List;

public abstract class BasisTeleOp extends LinearOpMode {
    List<LynxModule> alleHubs;

    protected HwMap hwMap;

    public void fahren() {
    }

    public void saison() {
    }

    public void telemetrie() {
    }

    public void initialisieren() { // Dies wird ausgeführt, wenn der Init-Button auf dem Driver Hub gedrückt wird
        alleHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : alleHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
            hub.clearBulkCache();
        }
    }

    public abstract void runOnce(); // Dies wird einmal ausgeführt, wenn der Play-Button auf dem Driver Hub gedrückt wird

    public abstract void runLoop(); // Dies wird in einer Schleife ausgeführt, wenn der Play-Button auf dem Driver Hub gedrückt wird

    public void beenden() { // Dies wird ausgeführt, nachdem die Schleife gestoppt wurde
        hwMap.navi.stoppen();
        hwMap.chassis.stoppeMotoren();
    }

    public void runOpMode() { // Diese interne Methode wird verwendet, um zu initialisieren und auszuführen
        initialisieren();
        waitForStart();
        runOnce();
        while (opModeIsActive()) {
            for (LynxModule hub : alleHubs) {
                hub.clearBulkCache();
            }
            runLoop();
        }
        beenden();
    }

    public void warteSchleife(int zeitInMs) {
        long start = System.currentTimeMillis();
        while ((System.currentTimeMillis() - start) < zeitInMs && opModeIsActive()) {
            fahren();
            hwMap.navi.schritt();
            telemetrie();
        }
    }

    private final java.util.Map<String, Boolean> tastenZustaende = new java.util.HashMap<>();

    protected boolean istTasteGedrueckt(String tastenId, boolean istGedrueckt) {
        boolean warGedrueckt = Boolean.TRUE.equals(tastenZustaende.getOrDefault(tastenId, false));
        tastenZustaende.put(tastenId, istGedrueckt);
        return istGedrueckt && !warGedrueckt;
    }
}
