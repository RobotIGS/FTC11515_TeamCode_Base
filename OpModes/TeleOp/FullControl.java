package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Tools.Gegensteuern;

@TeleOp(name = "FullControl", group = "FTC")
public class FullControl extends BaseTeleOp {
    /* ADD VARIABLES ONLY USED IN FULL CONTROL */
    protected boolean drive_sneak = true; // flag for storing the current speed mode
    double alt_left_stick_x;
    double alt_left_stick_y;
    double alt_div_trigger;
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
                -Gegensteuern.gegensteuern(alt_left_stick_x, gamepad1.left_stick_x) * (drive_sneak ? hwMap.speed_sneak : hwMap.speed_normal),
                Gegensteuern.gegensteuern(alt_div_trigger, (gamepad1.left_trigger - gamepad1.right_trigger)) *
                        (drive_sneak ? hwMap.speed_sneak : hwMap.speed_normal));

        // Werte speichern
        alt_left_stick_x = gamepad1.left_stick_x;
        alt_left_stick_y = gamepad1.left_stick_y;
        alt_div_trigger = (gamepad1.left_trigger - gamepad1.right_trigger);


        /* UPDATE THE ROBOT */
        hwMap.robot.step();

        /* ADD TELEMETRY FOR DRIVER DOWN BELOW */
        telemetry.addData("SNEAK", drive_sneak);
        telemetry.addLine();
        telemetry.addLine(hwMap.chassis.debug());
        telemetry.addLine(hwMap.navi.debug());
        telemetry.update();
        /* END SECTION */
    }
}