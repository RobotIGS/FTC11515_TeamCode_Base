package org.firstinspires.ftc.teamcode.OpModes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OpModes.TeleOp.BaseTeleOp;

@TeleOp(name = "Controller Test", group = "TESTING")
public class ControllerTest extends BaseTeleOp {
    @Override
    public void runOnce() {

    }

    @Override
    public void runLoop() {
        if (gamepad1.a || gamepad1.b || gamepad1.x || gamepad1.y ||
                gamepad1.dpad_down || gamepad1.dpad_up || gamepad1.dpad_left || gamepad1.dpad_right ||
                gamepad1.left_trigger > 0 || gamepad1.right_trigger > 0 ||
                gamepad1.left_bumper || gamepad1.right_bumper ||
                gamepad1.left_stick_x != 0 || gamepad1.left_stick_y != 0 ||
                gamepad1.right_stick_x != 0 || gamepad1.right_stick_y != 0) { // vorwärts
            hwMap.robot.setSpeed(
                    1,
                    0,
                    0);
        }
    }
}