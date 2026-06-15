package org.firstinspires.ftc.teamcode.opModes.teleOp.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.opModes.teleOp.BasisTeleOp;
import org.firstinspires.ftc.teamcode.tools.HwMap;

@TeleOp(name = "Test PID", group = "TESTING")
public class TestPID extends BasisTeleOp {
    final double[] pid = new double[3]; // p, i, d
    int ausgewaehlt = 0;

    @Override
    public void initialisieren() {
        super.initialisieren();
        hwMap = new HwMap(hardwareMap);
        pid[0] = hwMap.navi.rotationsPidRegler.kP;
        pid[1] = hwMap.navi.rotationsPidRegler.kI;
        pid[2] = hwMap.navi.rotationsPidRegler.kD;
    }


    @Override
    public void runOnce() {
    }

    @Override
    public void runLoop() {
        if (istTasteGedrueckt("gp1_x", gamepad1.x)) {
            hwMap.navi.rotationsPidRegler.reset();
            hwMap.robot.drehen(90, true);
        }

        if (istTasteGedrueckt("gp1_y", gamepad1.y)) {
            ausgewaehlt += 1;
            if (ausgewaehlt > 2) {
                ausgewaehlt = 0;
            }
        }

        if (istTasteGedrueckt("gp1_lb", gamepad1.left_bumper)) {
            pid[ausgewaehlt] = Math.min(1.0, pid[ausgewaehlt] + 0.0001);
        }
        if (istTasteGedrueckt("gp1_rb", gamepad1.right_bumper)) {
            pid[ausgewaehlt] = Math.max(0, pid[ausgewaehlt] - 0.0001);
        }
        if (istTasteGedrueckt("gp1_lt", gamepad1.left_trigger_pressed)) {
            pid[ausgewaehlt] = Math.min(1.0, pid[ausgewaehlt] + 0.001);
        }
        if (istTasteGedrueckt("gp1_rt", gamepad1.right_trigger_pressed)) {
            pid[ausgewaehlt] = Math.max(0, pid[ausgewaehlt] - 0.001);
        }

        hwMap.navi.rotationsPidRegler.changeValues(pid[0], pid[1], pid[2]);

        hwMap.robot.schritt();
        telemetrie();
    }

    @Override
    public void telemetrie() {
        telemetry.addData("selected", new String[]{"p", "i", "d"}[ausgewaehlt]);
        telemetry.addData("p", pid[0]);
        telemetry.addData("i", pid[1]);
        telemetry.addData("d", pid[2]);
        telemetry.addData("value:", hwMap.navi.rotationsPidRegler.pidValue);
        telemetry.addData("last error:", hwMap.navi.rotationsPidRegler.lastError);
        telemetry.addLine("\n" + hwMap.navi.debug());
        telemetry.addLine(hwMap.chassis.debug());
        telemetry.update();
    }
}
