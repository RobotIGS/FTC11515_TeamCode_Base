package org.firstinspires.ftc.teamcode.OpModes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.OpModes.TeleOp.BaseTeleOp;

@TeleOp
public class ClawTest extends BaseTeleOp {
    private Servo servo1;
    private Servo servo2;

    private double min;
    private double max;

    @Override
    public void initialize() {
        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo2 = hardwareMap.get(Servo.class, "servo2");

        min = 0;
        max = 0.2;
    }

    @Override
    public void run() {
        if (gamepad1.dpad_left) {
            servo1.setPosition(min);
            servo2.setPosition(max);
        }

        else if (gamepad1.dpad_right) {
            servo1.setPosition(max);
            servo2.setPosition(min);
        }

        if (gamepad1.left_bumper) {
            min += 0.1;
            while(gamepad1.left_bumper) {}
        } else if (gamepad1.left_trigger == 1) {
            min -= 0.1;
            while(gamepad1.left_trigger > 0) {}
        }

        if (gamepad1.right_bumper) {
            max += 0.1;
            while(gamepad1.right_bumper) {}
        } else if (gamepad1.right_trigger == 1) {
            max -= 0.1;
            while(gamepad1.right_trigger > 0) {}
        }

        telemetry.addData("min", min);
        telemetry.addData("max", max);
        telemetry.update();
    }
}
