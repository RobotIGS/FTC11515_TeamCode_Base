package org.firstinspires.ftc.teamcode.OpModes.Examples;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.OpModes.TeleOp.BaseTeleOp;

@TeleOp(name = "Fußball", group = "FTC")
public class Fußball extends BaseTeleOp {
    /* ADD VARIABLES ONLY USED IN FULL CONTROL */
    protected boolean drive_sneak = false; // flag for storing the current speed mode

    public DcMotor motor_schuss;
    public int wert = 5000;
    /* END SECTION */

    @Override
    public void initialize() {
        super.initialize();
        /* ADD CODE WHICH IS RUN ONCE WHEN INIT IS PRESSED */

        motor_schuss = hardwareMap.get(DcMotor.class, "schuss");
        motor_schuss.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor_schuss.setTargetPosition(0);
        motor_schuss.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor_schuss.setPower(1);

        /* END SECTION */
    }

    @Override
    public void runOnce() {
        /* ADD CODE WHICH IS RUN ONCE WHEN PLAY IS PRESSED */

        /* END SECTION */
    }

    @Override
    public void runLoop() {
        /* ADD OTHER HARDWARE CONTROLS DOWN BELOW */

        if (gamepad1.y) {
            motor_schuss.setTargetPosition(motor_schuss.getCurrentPosition() + wert);
            while (gamepad1.y) {}
        }

//        if (gamepad2.left_trigger > 0) {
//            motor_schuss.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            motor_schuss.setPower(gamepad2.left_trigger);
//        } else {
//            motor_schuss.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            motor_schuss.setPower(1);
//        }

        /* END SECTION */

        /* DRIVING */
        if (gamepad1.left_bumper || gamepad1.right_bumper) {
            drive_sneak = !drive_sneak;
            while ((gamepad1.left_bumper || gamepad1.right_bumper) && opModeIsActive()) {
            }
        }
        if (gamepad1.a) { // rückwarts
            hwMap.robot.setSpeed(
                    -1 * (drive_sneak ? hwMap.speed_sneak : hwMap.speed_full),
                    -gamepad1.right_stick_x * (drive_sneak ? hwMap.speed_sneak : hwMap.speed_full),
                    (gamepad1.left_trigger - gamepad1.right_trigger) *
                            (drive_sneak ? hwMap.speed_sneak : hwMap.speed_full));
        } else if (gamepad1.b) { // vorwärts
            hwMap.robot.setSpeed(
                    1 * (drive_sneak ? hwMap.speed_sneak : hwMap.speed_full),
                    -gamepad1.right_stick_x * (drive_sneak ? hwMap.speed_sneak : hwMap.speed_full),
                    (gamepad1.left_trigger - gamepad1.right_trigger) *
                            (drive_sneak ? hwMap.speed_sneak : hwMap.speed_full));
        } else {
            hwMap.robot.setSpeed(
                    -gamepad1.left_stick_y * (drive_sneak ? hwMap.speed_sneak : hwMap.speed_full),
                    -gamepad1.right_stick_x * (drive_sneak ? hwMap.speed_sneak : hwMap.speed_full),
                    (gamepad1.left_trigger - gamepad1.right_trigger) *
                            (drive_sneak ? hwMap.speed_sneak : hwMap.speed_full));
        }


        /* UPDATE THE ROBOT */
        hwMap.robot.step();

        /* ADD TELEMETRY FOR DRIVER DOWN BELOW */
        telemetry.addData("motor_schuss", motor_schuss.getCurrentPosition());
        telemetry.addData("motor_schuss", motor_schuss.getPower());
        telemetry.addData("SNEAK", drive_sneak);

        /* END SECTION */
        telemetry.addLine();
        telemetry.addLine(hwMap.chassis.debug());
        telemetry.addLine(hwMap.navi.debug());
        telemetry.update();
    }
}