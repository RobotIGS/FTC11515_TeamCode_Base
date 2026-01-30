package org.firstinspires.ftc.teamcode.OpModes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OpModes.TeleOp.BaseTeleOp;
import org.firstinspires.ftc.teamcode.Tools.HwMap;

@TeleOp(name = "PID Konfigurator", group = "TESTING")
public class PIDKonfigurator extends BaseTeleOp {
    protected HwMap hwMap;
    double[] pid_values = new double[3]; // p, i, d
    int selected = 0;

    @Override
    public void initialize() {
        hwMap = new HwMap(hardwareMap);
    }


    @Override
    public void runOnce() {
    }

    @Override
    public void runLoop() {
        if (gamepad1.x) {
            hwMap.robot.rotate(180);
            loop_while_driving();
        }

        if (gamepad1.y) {
            selected += 1;
            if (selected > 2) {
                selected = 0;
            }
        }

        if (gamepad2.right_bumper) {
            pid_values[selected] = Math.min(1.0, pid_values[selected] + 0.001);
        }
        if (gamepad2.left_bumper) {
            pid_values[selected] = Math.max(0, pid_values[selected] - 0.001);
        }

        hwMap.navi.rotationPidController.change_values(pid_values[0], pid_values[1], pid_values[2]);

        hwMap.robot.step();
        telemetry();
    }

    @Override
    public void telemetry() {
        telemetry.addData("p", pid_values[0]);
        telemetry.addData("i", pid_values[1]);
        telemetry.addData("d", pid_values[2]);
        telemetry.update();
    }
}
