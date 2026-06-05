package org.firstinspires.ftc.teamcode.OpModes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.OpModes.TeleOp.BaseTeleOp;


@TeleOp(name = "Test Motor & Servo", group = "TESTING")
public class TestMotorServo extends BaseTeleOp {

    public DcMotor motor1;
    public DcMotor motor2;
    public Servo servo1;
    public Servo servo2;
    public CRServo crservo1;

    boolean twoMotors = false;

    @Override
    public void initialize() {
        super.initialize();
        try {
            motor1 = hardwareMap.get(DcMotor.class, "motor1");
            motor2 = hardwareMap.get(DcMotor.class, "motor2");
            servo1 = hardwareMap.get(Servo.class, "servo1");
            servo2 = hardwareMap.get(Servo.class, "servo2");
            crservo1 = hardwareMap.get(CRServo.class, "crservo");
        } catch (Exception ignored){
        }
    }

    @Override
    public void runOnce() {
    }

    @Override
    public void runLoop() {
        motor1.setPower(gamepad1.left_stick_y);

        if (twoMotors) {
            motor2.setPower(gamepad1.left_stick_y);
        }

        if (isButtonPressed("gp1_b", gamepad1.b)) {
            twoMotors = !twoMotors;
        }

        crservo1.setPower(gamepad1.right_stick_y);

        if (gamepad1.dpad_down) {
            servo1.setPosition(servo1.getPosition() - 0.001);
            servo2.setPosition(servo2.getPosition() - 0.001);
        } else if (gamepad1.dpad_up) {
            servo1.setPosition(servo1.getPosition() + 0.001);
            servo2.setPosition(servo2.getPosition() + 0.001);
        }


        // information
        telemetry.addLine("left stick y: motor power");
        telemetry.addLine("right stick y: crservo power");
        telemetry.addLine("b: toggle motor 2");
        telemetry.addLine("dpad up/ down: servo");

        // motor information
        telemetry.addLine("motor information:");
        telemetry.addData("Speed", Math.abs(gamepad1.left_stick_y));
        telemetry.addData("Steps", motor1.getCurrentPosition());

        // servo information
        telemetry.addLine("servo information:");
        telemetry.addData("Value", servo1.getPosition());

        // cr-servo information
        telemetry.addLine("cr-servo information:");
        telemetry.addData("Value", crservo1.getPower());

        // update screen
        telemetry.update();
    }

    @Override
    public void end() {
    }
}