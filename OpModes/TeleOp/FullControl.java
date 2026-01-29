package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Tools.Steuerung.Gegensteuern;

@TeleOp(name = "FullControl", group = "FTC")
public class FullControl extends BaseTeleOp {
    /* ADD VARIABLES ONLY USED IN FULL CONTROL */
    Gegensteuern gegensteuern_X = new Gegensteuern("X");
    Gegensteuern gegensteuern_Y = new Gegensteuern("Y");
    double alt_vx;
    double alt_vy;
    // SEASON
    private int phase_aufsammeln = 0;
    private int phase_loswerden = 0;
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
        driving();
        season();
        hwMap.robot.step();
        telemetry();
    }

    @Override
    public void driving () {
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
        double vy = gamepad1.left_stick_x * (hwMap.navi.drive_sneak ? hwMap.navi.speed_sneak : hwMap.navi.speed_normal);
        double vz = (gamepad1.left_trigger - gamepad1.right_trigger) * hwMap.navi.speed_drehen * (hwMap.navi.drive_sneak ? 0.75 : 1);

        hwMap.navi.setSpeed(
                gegensteuern_X.calculate(hwMap.navi.drive_gegensteuern, alt_vx, vx),
                gegensteuern_Y.calculate(hwMap.navi.drive_gegensteuern, alt_vy, vy),
                vz);

        alt_vx = vx;
        alt_vy = vy;
    }

    @Override
    public void telemetry() {
        telemetry.addData("SNEAK", hwMap.navi.drive_sneak);
        telemetry.addData("GEGENSTEUERN", hwMap.navi.drive_gegensteuern);
        telemetry.addData("SCHUSS GESCHW", hwMap.gesch_schuss);
        telemetry.addData("boden", hwMap.s_kick_boden.getPosition());
        telemetry.addData("seite", hwMap.s_kick_seite.getPosition());
        telemetry.addLine();
        telemetry.addLine(hwMap.navi.debug());
        telemetry.addLine(hwMap.chassis.debug());
        telemetry.addLine(gegensteuern_X.debug());
        telemetry.addLine(gegensteuern_Y.debug());
        telemetry.update();
    }

    @Override
    public void season() {
        // Schussgeschwindigkeit
        if (gamepad1.right_bumper) {
            hwMap.gesch_schuss = Math.min(1.0, hwMap.gesch_schuss + 0.05);
            while ((gamepad2.right_bumper) && opModeIsActive()) {
            }
        }
        if (gamepad1.left_bumper) {
            hwMap.gesch_schuss = Math.max(0.4, hwMap.gesch_schuss - 0.05);
            while ((gamepad2.left_bumper) && opModeIsActive()) {
            }
        }

        // motor aufnehmen
        if (gamepad2.b) {
            if (hwMap.m_aufnehmen.getPower() == 0) {
                hwMap.m_aufnehmen.setPower(hwMap.gesch_aufnehmen);
            } else {
                hwMap.m_aufnehmen.setPower(0);
            }
            while ((gamepad2.b) && opModeIsActive()) {
            }
        }

        // motor schiessen
        if (gamepad2.a) {
            if (hwMap.m_schiessen.getPower() == 0) {
                hwMap.m_schiessen.setPower(hwMap.gesch_schuss);
            } else {
                hwMap.m_schiessen.setPower(0);
            }
            while ((gamepad2.a) && opModeIsActive()) {
            }
        }

        // motor hoch
        if (gamepad2.x) {
            hwMap.m_hoch.setPower(hwMap.m_hoch.getPower() == 0 ? 1 : 0);
            while ((gamepad2.x) && opModeIsActive()) {
            }
        }
        // motor boden + transport servo
        if (gamepad2.y) {
            hwMap.crs_rad.setPower(hwMap.m_boden.getPower() == 0 ? 1 : 0);
            hwMap.m_boden.setPower(hwMap.m_boden.getPower() == 0 ? -1 : 0);
            while ((gamepad2.y) && opModeIsActive()) {
            }
        }

        // servo kick seite
        if (false) {
            hwMap.s_kick_boden.setPosition(hwMap.s_kick_boden.getPosition() + 0.05);
            while (gamepad2.left_trigger != 0 && opModeIsActive()) {
            }
        }
        if (false) {
            hwMap.s_kick_boden.setPosition(hwMap.s_kick_boden.getPosition() - 0.05);
            while (gamepad2.right_trigger != 0 && opModeIsActive()) {
            }
        }

        // servo kick seite
        if (gamepad2.dpad_up) {
            hwMap.s_kick_seite.setPosition(hwMap.s_kick_seite_kurzposition);
            loop_wait(500);
            hwMap.s_kick_seite.setPosition(hwMap.s_kick_seite_dauerposition);
            while (gamepad2.dpad_up && opModeIsActive()) {}
        }

        // servo kick boden
        if (gamepad2.dpad_down) {
            hwMap.s_kick_boden.setPosition(hwMap.s_kick_boden_kurzposition);
            loop_wait(500);
            hwMap.s_kick_boden.setPosition(hwMap.s_kick_boden_dauerposition);
            while (gamepad2.dpad_down && opModeIsActive()) {}
        }


        // one-klick aufsammeln
        if (false) {
            phase_aufsammeln++;
            if (phase_aufsammeln > 1) {
                phase_aufsammeln = 0;
            }
            if (phase_aufsammeln == 1) {
                hwMap.m_aufnehmen.setPower(1);
                hwMap.m_boden.setPower(1);
            } else if (phase_aufsammeln == 0) {
                hwMap.m_aufnehmen.setPower(0);
                hwMap.m_boden.setPower(0);
            }
            while ((gamepad2.dpad_down) && opModeIsActive()) {
            }
        }

        // one-klick schiessen
        if (gamepad2.right_trigger != 0) {
            hwMap.m_schiessen.setPower(hwMap.gesch_schuss);
            loop_wait(2000);
            hwMap.m_hoch.setPower(1);
            loop_wait(1300);
            hwMap.m_boden.setPower(-1);
            hwMap.crs_rad.setPower(1);
            loop_wait(5000);
            hwMap.m_schiessen.setPower(0);
            hwMap.m_hoch.setPower(0);
            hwMap.m_boden.setPower(0);
            hwMap.crs_rad.setPower(0);

            while ((gamepad2.right_trigger != 0) && opModeIsActive()) {
            }

            phase_loswerden++;
            if (phase_loswerden > 3) {
                phase_loswerden = 0;
            }
            if (phase_loswerden == 1) {
                hwMap.m_schiessen.setPower(hwMap.gesch_schuss);
                loop_wait(2000);
                hwMap.m_boden.setPower(1);
                loop_wait(1300);
                hwMap.m_aufnehmen.setPower(0);
            } else if (phase_loswerden == 2) {
                hwMap.m_aufnehmen.setPower(1);
                loop_wait(2000);
                hwMap.m_aufnehmen.setPower(0);
                hwMap.m_schiessen.setPower(hwMap.gesch_schuss);
            } else if (phase_loswerden == 3) {
                hwMap.s_kick_seite.setPosition(hwMap.s_kick_boden_kurzposition);
                loop_wait(1000);
                hwMap.s_kick_seite.setPosition(hwMap.s_kick_boden_dauerposition);
                hwMap.m_schiessen.setPower(0);
                hwMap.m_aufnehmen.setPower(0);
            }
        }
    }
}