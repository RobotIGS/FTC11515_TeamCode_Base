package org.firstinspires.ftc.teamcode.opModes.teleOp.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.opModes.teleOp.BasisTeleOp;
import org.firstinspires.ftc.teamcode.tools.HwMap;

@TeleOp(name = "Test Controller", group = "TESTING")
public class TestController extends BasisTeleOp {
    @Override
    public void initialisieren() {
        super.initialisieren();
        hwMap = new HwMap(hardwareMap);
    }

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
                gamepad1.right_stick_x != 0 || gamepad1.right_stick_y != 0) {
            hwMap.navi.setGeschwindigkeit(
                    1,
                    0,
                    0);
        }
    }
}