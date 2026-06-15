package org.firstinspires.ftc.teamcode.opModes.teleOp.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.opModes.teleOp.BasisTeleOp;


@TeleOp(name = "Test Motor & Servo", group = "TESTING")
public class TestMotorServo extends BasisTeleOp {

    public DcMotor motor1;
    public DcMotor motor2;
    public Servo servo1;
    public Servo servo2;
    public CRServo crservo1;

    boolean zweiMotoren = false;
    boolean zweiServos = false;

    @Override
    public void initialisieren() {
        super.initialisieren();
        try {
            motor1 = hardwareMap.get(DcMotor.class, "motor1");
            motor2 = hardwareMap.get(DcMotor.class, "motor2");
            servo1 = hardwareMap.get(Servo.class, "servo1");
            servo2 = hardwareMap.get(Servo.class, "servo2");
            crservo1 = hardwareMap.get(CRServo.class, "crservo");
        } catch (Exception ignored) {
        }

        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void runOnce() {
    }

    @Override
    public void runLoop() {
        motor1.setPower(gamepad1.left_stick_y);

        if (zweiMotoren) {
            motor2.setPower(gamepad1.left_stick_y);
        }

        if (istTasteGedrueckt("gp1_b", gamepad1.b)) {
            zweiMotoren = !zweiMotoren;
        }

        if (istTasteGedrueckt("gp1_a", gamepad1.a)) {
            zweiServos = !zweiServos;
        }

        crservo1.setPower(gamepad1.right_stick_y);

        if (gamepad1.dpad_down) {
            servo1.setPosition(servo1.getPosition() - 0.001);
            if (zweiServos) servo2.setPosition(servo2.getPosition() - 0.001);
        } else if (gamepad1.dpad_up) {
            servo1.setPosition(servo1.getPosition() + 0.001);
            if (zweiServos) servo2.setPosition(servo2.getPosition() + 0.001);
        }


        // information
        telemetry.addLine("left stick y: motor power");
        telemetry.addLine("dpad up/ down: servo position");
        telemetry.addLine("right stick y: crservo power");
        telemetry.addLine("a: toggle servo 2");
        telemetry.addLine("b: toggle motor 2");


        // motor information
        telemetry.addLine("\nmotors:");
        telemetry.addData("Motor 2", zweiMotoren);
        telemetry.addData("Speed", Math.abs(gamepad1.left_stick_y));
        telemetry.addData("Steps", motor1.getCurrentPosition());

        // servo information
        telemetry.addLine("\nservos:");
        telemetry.addData("Servo 2", zweiServos);
        telemetry.addData("Value", servo1.getPosition());

        // cr-servo information
        telemetry.addLine("\ncr-servo:");
        telemetry.addData("Value", crservo1.getPower());

        // update screen
        telemetry.update();
    }

    @Override
    public void beenden() {
    }
}
