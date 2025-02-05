package org.firstinspires.ftc.teamcode.OpModes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.OpModes.TeleOp.BaseTeleOp;


@TeleOp(name = "Motor & Servo Test", group = "TESTING")
public class MotorServoTest extends BaseTeleOp {
    /**
     * HW-Map: testing
     * Servo: Port 1
     * Servo 2: Port 3
     * CR-Servo (dreht sich unendlich): Port 2
     * Motor: Port 1
     * Motor 2: Port 2
     */

    public DcMotor motor;
    public DcMotor motor2;
    public Servo servo;
    public Servo servo2;
    public CRServo crservo;

    boolean zwei_motoren = false;

    @Override
    public void initialize() {
        motor = hardwareMap.get(DcMotor.class,"motor");
        motor2 = hardwareMap.get(DcMotor.class,"motor2");
        servo = hardwareMap.get(Servo.class,"servo");
        servo2 = hardwareMap.get(Servo.class,"servo2");
        crservo = hardwareMap.get(CRServo.class,"crservo");
    }

    @Override
    public void runOnce() {
    }

    @Override
    public void runLoop() {
        motor.setPower(gamepad1.left_stick_y);

        if (zwei_motoren) {
            motor2.setPower(gamepad1.left_stick_y);
        }

        if (gamepad1.b) {
            zwei_motoren = !zwei_motoren;
            while (gamepad1.b) {}
        }

        crservo.setPower(gamepad1.right_stick_y);

        if (gamepad1.dpad_down) {
            servo.setPosition(servo.getPosition() - 0.001);
            servo2.setPosition(servo.getPosition() - 0.001);
        }
        else if (gamepad1.dpad_up) {
            servo.setPosition(servo.getPosition() + 0.001);
            servo2.setPosition(servo.getPosition() + 0.001);
        }


        // motor information
        telemetry.addLine("motor information:");
        telemetry.addData("Speed", Math.abs(gamepad1.left_stick_y));
        telemetry.addData("Steps", motor.getCurrentPosition());

        // servo information
        telemetry.addLine("servo information:");
        telemetry.addData("Value", servo.getPosition());

        // cr-servo information
        telemetry.addLine("cr-servo information:");
        telemetry.addData("Value", crservo.getPower());

        // update screen
        telemetry.update();
    }

    @Override
    public void end() {}
}