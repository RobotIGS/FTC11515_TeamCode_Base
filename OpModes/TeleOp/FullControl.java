package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Tools.HwMap;
import org.firstinspires.ftc.teamcode.Tools.Steuerung.Gegensteuern;

@TeleOp(name = "FullControl", group = "FTC")
public class FullControl extends BaseTeleOp {
    /* ADD VARIABLES ONLY USED IN FULL CONTROL */
    Gegensteuern gegensteuernX = new Gegensteuern("X");
    Gegensteuern gegensteuernY = new Gegensteuern("Y");
    double altVx;
    double altVy;

    // SEASON
    private enum AufsammelStatus {
        AUS, VOLL_AN, RAD_AUS, BODEN_AUS
    }

    private enum SchussStatus {
        AUS, AUFWAERMEN, ALLES_AN, STOPP
    }

    private AufsammelStatus aufsammelStatus = AufsammelStatus.AUS;
    private SchussStatus schussStatus = SchussStatus.AUS;
    /* END SECTION */

    @Override
    public void initialize() {
        super.initialize();
        hwMap = new HwMap(hardwareMap);
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
    public void driving() {
        if (isButtonPressed("gp1_lb", gamepad1.left_bumper)) {
            hwMap.navi.driveSneak = !hwMap.navi.driveSneak;
        }
        if (isButtonPressed("gp1_rb", gamepad1.right_bumper)) {
            hwMap.navi.driveGegensteuern = !hwMap.navi.driveGegensteuern;
        }

        double vx = -gamepad1.left_stick_y * (hwMap.navi.driveSneak ? hwMap.navi.speedSneak : hwMap.navi.speedNormal);
        double vy = -gamepad1.left_stick_x * (hwMap.navi.driveSneak ? hwMap.navi.speedSneak : hwMap.navi.speedNormal);
        double vz = -(gamepad1.left_trigger - gamepad1.right_trigger) * hwMap.navi.speedDrehen * (hwMap.navi.driveSneak ? hwMap.navi.speedSneak : hwMap.navi.speedNormal);

        hwMap.navi.setSpeed(
                gegensteuernX.calculate(hwMap.navi.driveGegensteuern, altVx, vx),
                gegensteuernY.calculate(hwMap.navi.driveGegensteuern, altVy, vy),
                vz);

        altVx = vx;
        altVy = vy;
    }

    @Override
    public void telemetry() {
        telemetry.addData("SNEAK", hwMap.navi.driveSneak);
        telemetry.addData("GEGENSTEUERN", hwMap.navi.driveGegensteuern);
        telemetry.addData("Schussgeschwindigkeit", hwMap.geschSchuss);
        telemetry.addData("Status Aufsammeln", aufsammelStatus);
        telemetry.addData("Status Loswerden", schussStatus);
        telemetry.addLine();
        telemetry.addLine(hwMap.navi.debug());
        telemetry.addLine(hwMap.chassis.debug());
        telemetry.addLine(gegensteuernX.debug());
        telemetry.addLine(gegensteuernY.debug());
        telemetry.update();
    }

    @Override
    public void season() {
        // Schussgeschwindigkeit
        if (isButtonPressed("gp2_rb", gamepad2.right_bumper)) {
            hwMap.geschSchuss = Math.min(1.0, hwMap.geschSchuss + 0.05);
        }
        if (isButtonPressed("gp2_lb", gamepad2.left_bumper)) {
            hwMap.geschSchuss = Math.max(0.4, hwMap.geschSchuss - 0.05);
        }

        // motor aufnehmen
        if (isButtonPressed("gp2_b", gamepad2.b)) {
            if (hwMap.m_aufnehmen.getPower() == 0) {
                hwMap.m_aufnehmen.setPower(hwMap.geschAufnehmen);
            } else {
                hwMap.m_aufnehmen.setPower(0);
            }
        }

        // motor schiessen
        if (isButtonPressed("gp2_a", gamepad2.a)) {
            if (hwMap.m_schiessen.getPower() == 0) {
                hwMap.m_schiessen.setPower(hwMap.geschSchuss);
            } else {
                hwMap.m_schiessen.setPower(0);
            }
        }

        // one-klick aufsammeln
        if (isButtonPressed("gp2_lt", gamepad2.left_trigger_pressed)) {
            switch (aufsammelStatus) {
                case AUS:
                    aufsammelStatus = AufsammelStatus.VOLL_AN;
                    hwMap.m_aufnehmen.setPower(hwMap.geschAufnehmen);
                    hwMap.m_boden.setPower(-1);
                    hwMap.crs_rad.setPower(1);
                    break;
                case VOLL_AN:
                    aufsammelStatus = AufsammelStatus.RAD_AUS;
                    hwMap.crs_rad.setPower(0);
                    break;
                case RAD_AUS:
                    aufsammelStatus = AufsammelStatus.BODEN_AUS;
                    hwMap.m_boden.setPower(0);
                    break;
                case BODEN_AUS:
                    aufsammelStatus = AufsammelStatus.AUS;
                    hwMap.m_aufnehmen.setPower(0);
                    break;
            }
        }

        // one-klick schiessen
        if (isButtonPressed("gp2_rt", gamepad2.right_trigger_pressed)) {
            switch (schussStatus) {
                case AUS:
                case STOPP: // Falls wir am Ende auf STOPP waren, fangen wir wieder bei AUFWAERMEN an
                    schussStatus = SchussStatus.AUFWAERMEN;
                    hwMap.m_schiessen.setPower(hwMap.geschSchuss);
                    loop_wait(2000);
                    hwMap.m_hoch.setPower(1);
                    loop_wait(1300);
                    hwMap.m_hoch.setPower(0);
                    break;
                case AUFWAERMEN:
                    schussStatus = SchussStatus.ALLES_AN;
                    hwMap.m_schiessen.setPower(hwMap.geschSchuss);
                    hwMap.m_hoch.setPower(1);
                    hwMap.m_boden.setPower(-1);
                    hwMap.crs_rad.setPower(1);
                    loop_wait(800);
                    hwMap.m_hoch.setPower(0);
                    break;
                case ALLES_AN:
                    schussStatus = SchussStatus.STOPP;
                    hwMap.m_schiessen.setPower(hwMap.geschSchuss);
                    hwMap.m_hoch.setPower(1);
                    loop_wait(2000);
                    hwMap.m_schiessen.setPower(0);
                    hwMap.m_hoch.setPower(0);
                    hwMap.m_boden.setPower(0);
                    hwMap.crs_rad.setPower(0);
                    schussStatus = SchussStatus.AUS;
                    break;
            }
        }
    }
}
