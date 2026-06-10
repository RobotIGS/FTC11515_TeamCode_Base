package org.firstinspires.ftc.teamcode.OpModes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OpModes.TeleOp.BaseTeleOp;
import org.firstinspires.ftc.teamcode.Tools.HwMap;

@TeleOp(name = "Test PID", group = "TESTING")
public class TestPID extends BaseTeleOp {
    double[] pid_values = new double[3]; // p, i, d
    int selected = 0;

    @Override
    public void initialize() {
        super.initialize();
        hwMap = new HwMap(hardwareMap);
        pid_values[0] = hwMap.navi.rotationPidRegler.kP;
        pid_values[1] = hwMap.navi.rotationPidRegler.kI;
        pid_values[2] = hwMap.navi.rotationPidRegler.kD;
    }


    @Override
    public void runOnce() {
    }

    @Override
    public void runLoop() {
        if (isButtonPressed("gp1_x", gamepad1.x)) {
            hwMap.navi.rotationPidRegler.reset();
            hwMap.robot.rotate(90, true);
        }

        if (isButtonPressed("gp1_y", gamepad1.y)) {
            selected += 1;
            if (selected > 2) {
                selected = 0;
            }
        }

        if (isButtonPressed("gp1_lb", gamepad1.left_bumper)) {
            pid_values[selected] = Math.min(1.0, pid_values[selected] + 0.0001);
        }
        if (isButtonPressed("gp1_rb", gamepad1.right_bumper)) {
            pid_values[selected] = Math.max(0, pid_values[selected] - 0.0001);
        }
        if (isButtonPressed("gp1_lt", gamepad1.left_trigger_pressed)) {
            pid_values[selected] = Math.min(1.0, pid_values[selected] + 0.001);
        }
        if (isButtonPressed("gp1_rt", gamepad1.right_trigger_pressed)) {
            pid_values[selected] = Math.max(0, pid_values[selected] - 0.001);
        }

        hwMap.navi.rotationPidRegler.changeValues(pid_values[0], pid_values[1], pid_values[2]);

        hwMap.robot.step();
        telemetry();
    }

    @Override
    public void telemetry() {
        telemetry.addData("selected", new String[]{"p", "i", "d"}[selected]);
        telemetry.addData("p", pid_values[0]);
        telemetry.addData("i", pid_values[1]);
        telemetry.addData("d", pid_values[2]);
        telemetry.addData("value:", hwMap.navi.rotationPidRegler.pidValue);
        telemetry.addData("last error:", hwMap.navi.rotationPidRegler.lastError);
        telemetry.addLine("\n" + hwMap.navi.debug());
        telemetry.addLine(hwMap.chassis.debug());
        telemetry.update();
    }
}
