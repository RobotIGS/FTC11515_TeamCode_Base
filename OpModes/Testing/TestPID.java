package org.firstinspires.ftc.teamcode.OpModes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OpModes.TeleOp.BaseTeleOp;

@TeleOp(name = "Test PID", group = "TESTING")
public class TestPID extends BaseTeleOp {
    double[] pid_values = new double[3]; // p, i, d
    int selected = 0;

    @Override
    public void initialize() {
        super.initialize();
        pid_values[0] = hwMap.navi.rotationPidController.k_p;
        pid_values[1] = hwMap.navi.rotationPidController.k_i;
        pid_values[2] = hwMap.navi.rotationPidController.k_d;
    }


    @Override
    public void runOnce() {
    }

    @Override
    public void runLoop() {
        if (isButtonPressed("gp1_x", gamepad1.x)) {
            hwMap.robot.rotate(180, true);
        }

        if (isButtonPressed("gp1_y", gamepad1.y)) {
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
