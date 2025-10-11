package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Tools.Modules.Gegensteuern;

@TeleOp(name = "FullControl", group = "FTC")
public class FullControl extends BaseTeleOp {
    /* ADD VARIABLES ONLY USED IN FULL CONTROL */
    boolean drive_sneak = true; // flag for storing the current speed mode

    // SEASON
    boolean m_schiessen = false;
    boolean m_kette = false;
    boolean s_unten = false;
    boolean s_oben = false;

    double alt_right_stick_x;
    double alt_left_stick_y;
    double alt_div_trigger;

    int phase_aufsammeln = 0;
    long time_loswerden = 0;

    double schussgeschwindigkeit = 0.683;
    double s_stop_kurz_position = 0.7;
    double s_stop_dauer_position = 0.23;

    Gegensteuern gegensteuern_X = new Gegensteuern("X");
    Gegensteuern gegensteuern_Y = new Gegensteuern("Y");
    Gegensteuern gegensteuern_Z = new Gegensteuern("Z");
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
                -gegensteuern_X.gegensteuern(alt_left_stick_y, gamepad1.left_stick_y) * (drive_sneak ? hwMap.speed_sneak : hwMap.speed_normal),
                -gegensteuern_Y.gegensteuern(alt_right_stick_x, gamepad1.right_stick_x) * (drive_sneak ? hwMap.speed_sneak : hwMap.speed_normal),
                gegensteuern_Z.gegensteuern(alt_div_trigger, (gamepad1.left_trigger - gamepad1.right_trigger)) *
                        (drive_sneak ? hwMap.speed_sneak : hwMap.speed_normal));

        // SEASON
        // Werte speichern
        alt_right_stick_x = gamepad1.right_stick_x;
        alt_left_stick_y = gamepad1.left_stick_y;
        alt_div_trigger = (gamepad1.left_trigger - gamepad1.right_trigger);

        // motor schiessen
        if (gamepad1.a) {
            m_schiessen = !m_schiessen;
            if (m_schiessen) {
                hwMap.m_schiessen_l.setPower(-schussgeschwindigkeit);
                hwMap.m_schiessen_r.setPower(schussgeschwindigkeit);
            } else {
                hwMap.m_schiessen_l.setPower(0);
                hwMap.m_schiessen_r.setPower(0);
            }
            while ((gamepad1.a) && opModeIsActive()) {
            }
        }
        // motor kette
        if (gamepad1.b) {
            m_kette = !m_kette;
            if (m_kette) {
                hwMap.m_kette.setPower(1);
            } else {
                hwMap.m_kette.setPower(0);
            }
            while ((gamepad1.b) && opModeIsActive()) {
            }
        }
        // servo unten
        if (gamepad1.x) {
            s_unten = !s_unten;
            if (s_unten) {
                hwMap.s_unten.setPower(-1);
            } else {
                hwMap.s_unten.setPower(0);
            }
            while ((gamepad1.x) && opModeIsActive()) {
            }
        }
        // servo oben
        if (gamepad1.y) {
            s_oben = !s_oben;
            if (s_oben) {
                hwMap.s_oben.setPower(-1);
            } else {
                hwMap.s_oben.setPower(0);
            }
            while ((gamepad1.y) && opModeIsActive()) {
            }
        }
        // servo stop
        if (gamepad1.dpad_left) {
            hwMap.s_stop.setPosition(s_stop_kurz_position);
            while (gamepad1.dpad_left && opModeIsActive()) {
            }
        } else if (hwMap.s_stop.getPosition() == s_stop_kurz_position) {
            sleep(200);
            hwMap.s_stop.setPosition(s_stop_dauer_position);
            while (gamepad1.dpad_left && opModeIsActive()) {
            }
        }

        // durchklicken aufsammeln
        if (gamepad1.dpad_up) {
            phase_aufsammeln += 1;
            if (phase_aufsammeln > 3) {
                phase_aufsammeln = 0;
            }
            if (phase_aufsammeln == 1) {
                hwMap.m_kette.setPower(1);
                hwMap.s_unten.setPower(-1);
                hwMap.s_oben.setPower(-1);
            } else if (phase_aufsammeln == 2) {
                hwMap.s_oben.setPower(0);
            } else if (phase_aufsammeln == 3) {
                hwMap.s_oben.setPower(0);
                hwMap.s_unten.setPower(0);
            } else {
                hwMap.s_oben.setPower(0);
                hwMap.s_unten.setPower(0);
                hwMap.m_kette.setPower(0);
            }
            while ((gamepad1.dpad_up) && opModeIsActive()) {
            }
        }

        // one-klick loswerden
        if (gamepad1.dpad_down) {
            time_loswerden = System.currentTimeMillis();
            while ((gamepad1.dpad_down) && opModeIsActive()) {
            }
        }
        if (System.currentTimeMillis() > time_loswerden + 7000) {
            if (System.currentTimeMillis() < time_loswerden + 8000) {
                hwMap.m_schiessen_l.setPower(0);
                hwMap.m_schiessen_r.setPower(0);
                hwMap.s_oben.setPower(0);
                hwMap.s_unten.setPower(0);
                hwMap.m_kette.setPower(0);
            }
        } else if (System.currentTimeMillis() > time_loswerden + 1000) {
            hwMap.m_schiessen_l.setPower(-schussgeschwindigkeit);
            hwMap.m_schiessen_r.setPower(schussgeschwindigkeit);
            hwMap.m_kette.setPower(1);
            hwMap.s_unten.setPower(-1);
            hwMap.s_oben.setPower(-1);
        } else if (System.currentTimeMillis() > time_loswerden) {
            hwMap.m_schiessen_l.setPower(-schussgeschwindigkeit);
            hwMap.m_schiessen_r.setPower(schussgeschwindigkeit);
        }


        /* UPDATE THE ROBOT */
        hwMap.robot.step();

        /* ADD TELEMETRY FOR DRIVER DOWN BELOW */
        telemetry.addData("SNEAK", drive_sneak);
        telemetry.addData("s_stop", hwMap.s_stop.getPosition());
        telemetry.addLine();
        telemetry.addLine(gegensteuern_X.debug());
        telemetry.addLine(gegensteuern_Y.debug());
        telemetry.addLine(gegensteuern_Z.debug());
        telemetry.addLine(hwMap.chassis.debug());
        telemetry.addLine(hwMap.navi.debug());
        telemetry.update();
        /* END SECTION */
    }
}