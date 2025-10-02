package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Tools.Gegensteuern;

@TeleOp(name = "FullControl", group = "FTC")
public class FullControl extends BaseTeleOp {
    /* ADD VARIABLES ONLY USED IN FULL CONTROL */
    public static int gegensteuern = 0;
    boolean drive_sneak = true; // flag for storing the current speed mode
    double alt_right_stick_x;
    double alt_left_stick_y;
    double alt_div_trigger;

    // SEASON
    boolean schiessen = false;
    boolean kette = false;
    /* END SECTION */

    @Override
    public void initialize() {
        super.initialize();
        /* ADD CODE WHICH IS RUN ONCE WHEN INIT IS PRESSED */

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

        /* END SECTION */

        /* DRIVING */
        if (gamepad1.left_bumper || gamepad1.right_bumper) {
            drive_sneak = !drive_sneak;
            while ((gamepad1.left_bumper || gamepad1.right_bumper) && opModeIsActive()) {
            }
        }
        hwMap.robot.setSpeed(
                -Gegensteuern.gegensteuern(alt_left_stick_y, gamepad1.left_stick_y) * (drive_sneak ? hwMap.speed_sneak : hwMap.speed_normal),
                -Gegensteuern.gegensteuern(alt_right_stick_x, gamepad1.right_stick_x) * (drive_sneak ? hwMap.speed_sneak : hwMap.speed_normal),
                Gegensteuern.gegensteuern(alt_div_trigger, (gamepad1.left_trigger - gamepad1.right_trigger)) *
                        (drive_sneak ? hwMap.speed_sneak : hwMap.speed_normal));

        // Werte speichern
        alt_right_stick_x = gamepad1.right_stick_x;
        alt_left_stick_y = gamepad1.left_stick_y;
        alt_div_trigger = (gamepad1.left_trigger - gamepad1.right_trigger);


        // SEASON
        // schiessen
        if (gamepad1.a) {
            schiessen = !schiessen;
            if (schiessen) {
                hwMap.m_schiessen_l.setPower(-0.5);
                hwMap.m_schiessen_r.setPower(0.5);
            } else {
                hwMap.m_schiessen_l.setPower(0);
                hwMap.m_schiessen_r.setPower(0);
            }
            while ((gamepad1.a) && opModeIsActive()) {
            }
        }
        // kette
        if (gamepad1.b) {
            kette = !kette;
            if (kette) {
                hwMap.m_kette.setPower(0.9);
            } else {
                hwMap.m_kette.setPower(0);
            }
            while ((gamepad1.b) && opModeIsActive()) {
            }
        }


        /* UPDATE THE ROBOT */
        hwMap.robot.step();

        /* ADD TELEMETRY FOR DRIVER DOWN BELOW */
        telemetry.addData("SNEAK", drive_sneak);
        telemetry.addData("GEGENSTEUERN", gegensteuern);
        telemetry.addLine();
        telemetry.addLine(hwMap.chassis.debug());
        telemetry.addLine(hwMap.navi.debug());
        telemetry.update();
        /* END SECTION */
    }
}