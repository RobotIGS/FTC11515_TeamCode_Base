package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Tools.Gegensteuern;

@TeleOp(name = "FullControl", group = "FTC")
public class FullControl extends BaseTeleOp {
    /* ADD VARIABLES ONLY USED IN FULL CONTROL */
    Gegensteuern gegensteuern_X = new Gegensteuern("X");
    Gegensteuern gegensteuern_Y = new Gegensteuern("Y");

    // SEASON
    boolean m_schiessen = false;
    boolean m_aufnehmen = false;
    boolean s_unten = false;
    boolean s_oben = false;

    double alt_vx;
    double alt_vy;

    int phase_aufsammeln = 0;
    long time_loswerden = 0;

    double schussgeschwindigkeit = 0.683;
    double s_stop_kurz_position = 0.7;
    double s_stop_dauer_position = 0.23;
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
        /* DRIVING */
        if (gamepad1.left_bumper) {
            hwMap.navi.drive_sneak = !hwMap.navi.drive_sneak;
            while ((gamepad1.left_bumper) && opModeIsActive()) {
            }
        }
        if (gamepad1.right_bumper) {
            hwMap.navi.drive_gegensteuern = !hwMap.navi.drive_gegensteuern;
            while (gamepad1.right_bumper && opModeIsActive()) {
            }
        }

        double vx = -gamepad1.left_stick_y * (hwMap.navi.drive_sneak ? hwMap.navi.speed_sneak : hwMap.navi.speed_normal);
        double vy = -gamepad1.right_stick_x * (hwMap.navi.drive_sneak ? hwMap.navi.speed_sneak : hwMap.navi.speed_normal);
        double vz = (gamepad1.left_trigger - gamepad1.right_trigger) * (hwMap.navi.drive_sneak ? hwMap.navi.speed_sneak : hwMap.navi.speed_normal);

        hwMap.robot.setSpeed(
                gegensteuern_X.gegensteuern(hwMap.navi.drive_gegensteuern, alt_vx, vx),
                gegensteuern_Y.gegensteuern(hwMap.navi.drive_gegensteuern, alt_vy, vy),
                vz);

        alt_vx = vx;
        alt_vy = vy;

        /* SEASON (ADD OTHER HARDWARE CONTROLS DOWN BELOW) */
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
        // motor aufnehmen
        if (gamepad1.b) {
            m_aufnehmen = !m_aufnehmen;
            if (m_aufnehmen) {
                hwMap.m_aufnehmen.setPower(1);
            } else {
                hwMap.m_aufnehmen.setPower(0);
            }
            while ((gamepad1.b) && opModeIsActive()) {
            }
        }
        // motor hoch
        if (gamepad1.dpad_right) {
            hwMap.m_hoch.setPower(1);
            while ((gamepad1.dpad_right) && opModeIsActive()) {
            }
            hwMap.m_hoch.setPower(0);
        } else if (gamepad1.dpad_left) {
            hwMap.m_hoch.setPower(-1);
            while ((gamepad1.dpad_left) && opModeIsActive()) {
            }
            hwMap.m_hoch.setPower(0);
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
//        if (gamepad1.dpad_left) {
//            hwMap.s_stop.setPosition(s_stop_kurz_position);
//            while (gamepad1.dpad_left && opModeIsActive()) {
//            }
//        } else if (hwMap.s_stop.getPosition() == s_stop_kurz_position) {
//            sleep(200);
//            hwMap.s_stop.setPosition(s_stop_dauer_position);
//            while (gamepad1.dpad_left && opModeIsActive()) {
//            }
//        }

        // one-klick aufsammeln
        if (gamepad1.dpad_up) {
            phase_aufsammeln += 1;
            if (phase_aufsammeln > 3) {
                phase_aufsammeln = 0;
            }
            if (phase_aufsammeln == 1) {
                hwMap.m_aufnehmen.setPower(1);
                hwMap.s_unten.setPower(-1);
                hwMap.s_oben.setPower(-1);
            } else if (phase_aufsammeln == 2) {
                hwMap.s_oben.setPower(0);
            } else if (phase_aufsammeln == 3) {
                hwMap.s_oben.setPower(0);
                hwMap.s_unten.setPower(0);
            } else {
                hwMap.m_aufnehmen.setPower(0);
                hwMap.s_unten.setPower(1);
                hwMap.s_oben.setPower(1);
                sleep(500);
                hwMap.s_oben.setPower(0);
                hwMap.s_unten.setPower(0);
                hwMap.m_aufnehmen.setPower(0);
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
            if (System.currentTimeMillis() < time_loswerden + 7500) {
                hwMap.m_schiessen_l.setPower(0);
                hwMap.m_schiessen_r.setPower(0);
                hwMap.s_oben.setPower(0);
                hwMap.s_unten.setPower(0);
                hwMap.m_aufnehmen.setPower(0);
            }
        } else if (System.currentTimeMillis() > time_loswerden + 5500) {
            hwMap.s_stop.setPosition(s_stop_kurz_position);
            sleep(200);
            hwMap.s_stop.setPosition(s_stop_dauer_position);
        } else if (System.currentTimeMillis() > time_loswerden + 1500) {
            hwMap.m_aufnehmen.setPower(1);
            hwMap.s_unten.setPower(-1);
            hwMap.s_oben.setPower(-1);
        } else if (System.currentTimeMillis() > time_loswerden) {
            hwMap.m_schiessen_l.setPower(-schussgeschwindigkeit);
            hwMap.m_schiessen_r.setPower(schussgeschwindigkeit);
        }


        /* UPDATE THE ROBOT */
        hwMap.robot.step();

        /* ADD TELEMETRY FOR DRIVER DOWN BELOW */
        telemetry.addData("SNEAK", hwMap.navi.drive_sneak);
        telemetry.addData("GEGENSTEUERN", hwMap.navi.drive_gegensteuern);
        telemetry.addLine();
        telemetry.addLine(gegensteuern_X.debug());
        telemetry.addLine(gegensteuern_Y.debug());
        telemetry.addLine(hwMap.chassis.debug());
        telemetry.addLine(hwMap.navi.debug());
        telemetry.update();
        /* END SECTION */
    }
}