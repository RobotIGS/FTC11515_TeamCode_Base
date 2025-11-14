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
    private boolean m_aufnehmen = false;
    private boolean m_schiessen = false;
    private boolean s_unten = false;
    private boolean s_oben = false;
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
        double vy = gamepad1.left_stick_x * (hwMap.navi.drive_sneak ? hwMap.navi.speed_sneak : hwMap.navi.speed_normal);
        double vz = (gamepad1.left_trigger - gamepad1.right_trigger) * hwMap.navi.speed_drehen * (hwMap.navi.drive_sneak ? 0.75 : 1);

        hwMap.robot.setSpeed(
                gegensteuern_X.calculate(hwMap.navi.drive_gegensteuern, alt_vx, vx),
                gegensteuern_Y.calculate(hwMap.navi.drive_gegensteuern, alt_vy, vy),
                vz);

        alt_vx = vx;
        alt_vy = vy;

        season();

        /* UPDATE THE ROBOT */
        hwMap.robot.step();

        /* ADD TELEMETRY FOR DRIVER DOWN BELOW */
        telemetry.addData("SNEAK", hwMap.navi.drive_sneak);
        telemetry.addData("GEGENSTEUERN", hwMap.navi.drive_gegensteuern);
        telemetry.addData("SCHUSS GESCHW", hwMap.schussgeschwindigkeit);
        telemetry.addLine();
        telemetry.addLine(gegensteuern_X.debug());
        telemetry.addLine(gegensteuern_Y.debug());
        telemetry.addLine(hwMap.chassis.debug());
        telemetry.addLine(hwMap.navi.debug());
        telemetry.update();
        /* END SECTION */
    }

    void season() {
        if (gamepad2.right_bumper) {
            hwMap.schussgeschwindigkeit = Math.min(1.0, hwMap.schussgeschwindigkeit + 0.1);
            while ((gamepad2.right_bumper) && opModeIsActive()) {
            }
        }
        if (gamepad2.left_bumper && hwMap.schussgeschwindigkeit > 0.4) {
            hwMap.schussgeschwindigkeit = Math.max(0.0, hwMap.schussgeschwindigkeit - 0.1);
            while ((gamepad2.left_bumper) && opModeIsActive()) {
            }
        }

        // motor schiessen
        if (gamepad2.a) {
            m_schiessen = !m_schiessen;
            if (m_schiessen) {
                hwMap.m_schiessen_l.setPower(-hwMap.schussgeschwindigkeit);
                hwMap.m_schiessen_r.setPower(hwMap.schussgeschwindigkeit);
            } else {
                hwMap.m_schiessen_l.setPower(0);
                hwMap.m_schiessen_r.setPower(0);
            }
            while ((gamepad2.a) && opModeIsActive()) {
            }
        }
        // motor aufnehmen
        if (gamepad2.b) {
            m_aufnehmen = !m_aufnehmen;
            if (m_aufnehmen) {
                hwMap.m_aufnehmen.setPower(1);
            } else {
                hwMap.m_aufnehmen.setPower(0);
            }
            while ((gamepad2.b) && opModeIsActive()) {
            }
        }
        // motor hoch
        if (gamepad2.dpad_right) {
            hwMap.m_hoch.setPower(1);
            while ((gamepad2.dpad_right) && opModeIsActive()) {
            }
            hwMap.m_hoch.setPower(0);
        } else if (gamepad2.dpad_left) {
            hwMap.m_hoch.setPower(-1);
            while ((gamepad2.dpad_left) && opModeIsActive()) {
            }
            hwMap.m_hoch.setPower(0);
        }

        // servo unten
        if (gamepad2.x) {
            s_unten = !s_unten;
            if (s_unten) {
                hwMap.s_unten.setPower(-1);
            } else {
                hwMap.s_unten.setPower(0);
            }
            while ((gamepad2.x) && opModeIsActive()) {
            }
        }
        // servo oben
        if (gamepad2.y) {
            s_oben = !s_oben;
            if (s_oben) {
                hwMap.s_oben.setPower(-1);
            } else {
                hwMap.s_oben.setPower(0);
            }
            while ((gamepad2.y) && opModeIsActive()) {
            }
        }
        // servo kick
        if (gamepad2.left_trigger != 0) {
            hwMap.s_kick.setPosition(hwMap.s_kick_kurzposition);
            while (gamepad2.left_trigger != 0 && opModeIsActive()) {
            }
        } else if (hwMap.s_kick.getPosition() == hwMap.s_kick_kurzposition) {
            loop_wait(250);
            hwMap.s_kick.setPosition(hwMap.s_kick_dauerposition);
        }

        // one-klick aufsammeln
        if (gamepad2.dpad_down) {
            phase_aufsammeln++;
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
                hwMap.s_unten.setPower(0);
            } else if (phase_aufsammeln == 0) {
                hwMap.s_unten.setPower(1);
                hwMap.s_oben.setPower(1);
                hwMap.m_aufnehmen.setPower(-1);
                loop_wait(50);
                hwMap.m_aufnehmen.setPower(0);
                loop_wait(500);
                hwMap.s_oben.setPower(0);
                hwMap.s_unten.setPower(0);
            }
            while ((gamepad2.dpad_down) && opModeIsActive()) {
            }
        }

        // one-klick schiessen
        if (gamepad2.dpad_up) {
            phase_loswerden++;
            if (phase_loswerden > 3) {
                phase_loswerden = 0;
            }
            if (phase_loswerden == 1) {
                hwMap.m_schiessen_l.setPower(-hwMap.schussgeschwindigkeit);
                hwMap.m_schiessen_r.setPower(hwMap.schussgeschwindigkeit);
                loop_wait(2000);
                hwMap.m_aufnehmen.setPower(1);
                hwMap.s_unten.setPower(-1);
                hwMap.s_oben.setPower(-1);
                loop_wait(1300);
                hwMap.m_aufnehmen.setPower(0);
                hwMap.s_unten.setPower(0);
                hwMap.s_oben.setPower(0);
            } else if (phase_loswerden == 2) {
                hwMap.m_aufnehmen.setPower(1);
                hwMap.s_unten.setPower(-1);
                hwMap.s_oben.setPower(-1);
                loop_wait(1300);
                hwMap.m_aufnehmen.setPower(0);
                hwMap.s_unten.setPower(0);
                hwMap.s_oben.setPower(0);
                hwMap.m_schiessen_l.setPower(-hwMap.schussgeschwindigkeit * 0.8); // letzter Ball braucht weniger Kraft
                hwMap.m_schiessen_r.setPower(hwMap.schussgeschwindigkeit * 0.8);
            } else if (phase_loswerden == 3) {
                hwMap.s_kick.setPosition(hwMap.s_kick_kurzposition);
                loop_wait(1000);
                hwMap.s_kick.setPosition(hwMap.s_kick_dauerposition);
                hwMap.m_schiessen_l.setPower(0);
                hwMap.m_schiessen_r.setPower(0);
                hwMap.s_oben.setPower(0);
                hwMap.s_unten.setPower(0);
                hwMap.m_aufnehmen.setPower(0);
            }
            while ((gamepad2.dpad_up) && opModeIsActive()) {
            }
        }
    }
}