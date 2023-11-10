package org.firstinspires.ftc.teamcode.OpModes.Testing;

import org.firstinspires.ftc.teamcode.OpModes.TeleOp.BaseTeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Date;


enum MODE {
    MODE_MANUAL,
    MODE_AUTO_ONE_DIRECTION
}

// TODO all ig
@TeleOp(name="Test One Motor ('testing_motor')", group="testing")
@Disabled
public class MotorTest extends BaseTeleOp {
    DcMotor motor;
    MODE mode;

    double speed;
    double max_speed;

    boolean enabled;

    boolean a;
    boolean b;
    long l;

    @Override
    public void initialize() {
        motor = hardwareMap.get(DcMotor.class, "testing_motor");
        mode = MODE.MODE_MANUAL;

        max_speed = 1.0;

        enabled = false;
    }

    @Override
    public void run() {
        /* inputs */

        // toggle enabled
        if (gamepad1.a) {
            enabled = !enabled;
            while (gamepad1.a) {}
        }

        // set max_speed
        if (gamepad1.dpad_up)
            max_speed = Math.abs(gamepad1.right_stick_y);

        // switch mode
        if (gamepad1.left_bumper && gamepad1.right_bumper) {
            int i;
            while (opModeIsActive()) {
                i = mode.ordinal();
                telemetry.addLine("select mode...(dpad up/down, dpad right)");
                for (MODE m: MODE.values()) {
                    telemetry.addLine(String.format("%s%s", m.name(), m==mode?" -- SELECTED":""));
                }

                // enter mode
                if (gamepad1.dpad_right)
                    break;

                // select up
                if (gamepad1.dpad_up && i > 1) {
                    i--;
                    while (gamepad1.dpad_up) {}
                    mode = MODE.values()[i];
                }

                // select down
                else if (gamepad1.dpad_down && i < MODE.values().length-1) {
                    i++;
                    while (gamepad1.dpad_down) {}
                }
            }
        }

        // modes
        switch (mode) {
            case MODE_MANUAL:
                speed = -gamepad1.left_trigger + gamepad1.right_trigger;
                break;
            case MODE_AUTO_ONE_DIRECTION:
                speed = 1.0;
                break;
            default:
                break;
        }

        if (gamepad1.a) {
            a = !a;
            while (gamepad1.a) {}
        }
        if (a) {
            if (new Date().getTime() - 1500 >= l) {
                l = new Date().getTime();
                b = !b;
            }
            speed = b ? 0.5 : -0.5;
        }
        else
            speed = -gamepad1.left_trigger + gamepad1.right_trigger;

        // set speed
        motor.setPower(speed * max_speed);

        // intro
        telemetry.addLine("This code is for testing ONE motor");
        telemetry.addData("Mode", mode.name());
        telemetry.addLine();

        // switch mode + usage guide for mode

        // motor information
        telemetry.addLine("motor information:");
        telemetry.addData("Speed", "%f (max = %f)", speed, max_speed);
        telemetry.addData("Steps", motor.getCurrentPosition());

        // update screen
        telemetry.update();
    }
}
