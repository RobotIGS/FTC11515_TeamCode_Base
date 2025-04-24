package org.firstinspires.ftc.teamcode.OpModes.Examples;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.OpModes.TeleOp.BaseTeleOp;

@TeleOp(name = "Tennisball", group = "FTC")
public class Tennisball extends BaseTeleOp {
    /* ADD VARIABLES ONLY USED IN FULL CONTROL */
    protected boolean drive_sneak = false; // flag for storing the current speed mode

    public DcMotor motor_schuss_L;
    public DcMotor motor_schuss_R;
    public DcMotor motor_lagerung;

    int wert = 2375;
    /* END SECTION */

    @Override
    public void initialize() {
        super.initialize();
        /* ADD CODE WHICH IS RUN ONCE WHEN INIT IS PRESSED */

        motor_schuss_L = hardwareMap.get(DcMotor.class, "schuss_l");
        motor_schuss_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor_schuss_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motor_schuss_R = hardwareMap.get(DcMotor.class, "schuss_r");
        motor_schuss_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor_schuss_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motor_lagerung = hardwareMap.get(DcMotor.class, "lagerung");
        motor_lagerung.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor_lagerung.setTargetPosition(0);
        motor_lagerung.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor_lagerung.setPower(1);

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
            motor_schuss_R.setPower(0.3);
            motor_schuss_L.setPower(-0.3);
            while (gamepad1.y) {}
        } else {
            motor_schuss_R.setPower(0);
            motor_schuss_L.setPower(0);
        }

        if (gamepad1.x) {
            motor_lagerung.setTargetPosition(motor_lagerung.getCurrentPosition() - wert);
            while (gamepad1.x) {}
        }

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
        telemetry.addData("motor_schuss_l", motor_schuss_L.getPower());
        telemetry.addData("motor_schuss_r", motor_schuss_R.getPower());
        telemetry.addData("motor_lagerung", motor_lagerung.getCurrentPosition());
        telemetry.addData("SNEAK", drive_sneak);

        /* END SECTION */
        telemetry.addLine();
        telemetry.addLine(hwMap.chassis.debug());
        telemetry.addLine(hwMap.navi.debug());
        telemetry.update();
    }
}