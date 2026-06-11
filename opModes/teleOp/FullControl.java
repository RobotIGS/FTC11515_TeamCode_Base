package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.tools.HwMap;
import org.firstinspires.ftc.teamcode.tools.steuerung.Gegensteuern;

@TeleOp(name = "FullControl", group = "FTC")
public class FullControl extends BasisTeleOp {
    /* ADD VARIABLES ONLY USED IN FULL CONTROL */
    final Gegensteuern gegensteuernX = new Gegensteuern("X");
    final Gegensteuern gegensteuernY = new Gegensteuern("Y");
    double altVx;
    double altVy;

    // SEASON
    private enum AufsammelStatus {
        AUS, VOLL_AN, RAD_AUS, BODEN_AUS
    }

    private enum SchussStatus {
        AUS, AUFWAERMEN, ALLES_AN, STOPP
    }

    private AufsammelStatus statusAufsammeln = AufsammelStatus.AUS;
    private SchussStatus statusSchuss = SchussStatus.AUS;
    /* END SECTION */

    @Override
    public void initialisieren() {
        super.initialisieren();
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
        fahren();
        saison();
        hwMap.robot.schritt();
        telemetrie();
    }

    @Override
    public void fahren() {
        if (istTasteGedrueckt("gp1_lb", gamepad1.left_bumper)) {
            hwMap.navi.sneak = !hwMap.navi.sneak;
        }
        if (istTasteGedrueckt("gp1_rb", gamepad1.right_bumper)) {
            hwMap.navi.fahreGegensteuern = !hwMap.navi.fahreGegensteuern;
        }

        double vx = -gamepad1.left_stick_y * (hwMap.navi.sneak ? hwMap.navi.geschwindigkeitSneak : hwMap.navi.geschwindigkeitNormal);
        double vy = -gamepad1.right_stick_x * (hwMap.navi.sneak ? hwMap.navi.geschwindigkeitSneak : hwMap.navi.geschwindigkeitNormal);
        double vz = (gamepad1.left_trigger - gamepad1.right_trigger) * hwMap.navi.geschwindigkeitDrehen * (hwMap.navi.sneak ? hwMap.navi.geschwindigkeitSneak : hwMap.navi.geschwindigkeitNormal);

        hwMap.navi.setGeschwindigkeit(
                gegensteuernX.calculate(hwMap.navi.fahreGegensteuern, altVx, vx),
                gegensteuernY.calculate(hwMap.navi.fahreGegensteuern, altVy, vy),
                vz);

        altVx = vx;
        altVy = vy;
    }

    @Override
    public void telemetrie() {
        telemetry.addData("Sneak", hwMap.navi.sneak);
        telemetry.addData("Gegensteuern", hwMap.navi.fahreGegensteuern);
        telemetry.addData("Schussgeschwindigkeit", hwMap.geschSchuss);
        telemetry.addData("Status Aufsammeln", statusAufsammeln);
        telemetry.addData("Status Schuss", statusSchuss);
        telemetry.addLine();
        telemetry.addLine(hwMap.navi.debug());
        telemetry.addLine(hwMap.chassis.debug());
        telemetry.addLine(gegensteuernX.debug());
        telemetry.addLine(gegensteuernY.debug());
        telemetry.update();
    }

    @Override
    public void saison() {
        // Schussgeschwindigkeit
        if (istTasteGedrueckt("gp2_rb", gamepad2.right_bumper)) {
            hwMap.geschSchuss = Math.min(1.0, hwMap.geschSchuss + 0.05);
        }
        if (istTasteGedrueckt("gp2_lb", gamepad2.left_bumper)) {
            hwMap.geschSchuss = Math.max(0.4, hwMap.geschSchuss - 0.05);
        }

        // motor aufnehmen
        if (istTasteGedrueckt("gp2_b", gamepad2.b)) {
            if (hwMap.mAufnehmen.getPower() == 0) {
                hwMap.mAufnehmen.setPower(hwMap.geschAufnehmen);
            } else {
                hwMap.mAufnehmen.setPower(0);
            }
        }

        // motor schiessen
        if (istTasteGedrueckt("gp2_a", gamepad2.a)) {
            if (hwMap.mSchiessen.getPower() == 0) {
                hwMap.mSchiessen.setPower(hwMap.geschSchuss);
            } else {
                hwMap.mSchiessen.setPower(0);
            }
        }

        // one-klick aufsammeln
        if (istTasteGedrueckt("gp2_lt", gamepad2.left_trigger_pressed)) {
            switch (statusAufsammeln) {
                case AUS:
                    statusAufsammeln = AufsammelStatus.VOLL_AN;
                    hwMap.mAufnehmen.setPower(hwMap.geschAufnehmen);
                    hwMap.mBoden.setPower(-1);
                    hwMap.crsRad.setPower(1);
                    break;
                case VOLL_AN:
                    statusAufsammeln = AufsammelStatus.RAD_AUS;
                    hwMap.crsRad.setPower(0);
                    break;
                case RAD_AUS:
                    statusAufsammeln = AufsammelStatus.BODEN_AUS;
                    hwMap.mBoden.setPower(0);
                    break;
                case BODEN_AUS:
                    statusAufsammeln = AufsammelStatus.AUS;
                    hwMap.mAufnehmen.setPower(0);
                    break;
            }
        }

        // one-klick schiessen
        if (istTasteGedrueckt("gp2_rt", gamepad2.right_trigger_pressed)) {
            switch (statusSchuss) {
                case AUS:
                case STOPP: // Falls wir am Ende auf STOPP waren, fangen wir wieder bei AUFWAERMEN an
                    statusSchuss = SchussStatus.AUFWAERMEN;
                    hwMap.mSchiessen.setPower(hwMap.geschSchuss);
                    warteSchleife(2000);
                    hwMap.mHoch.setPower(1);
                    warteSchleife(1300);
                    hwMap.mHoch.setPower(0);
                    break;
                case AUFWAERMEN:
                    statusSchuss = SchussStatus.ALLES_AN;
                    hwMap.mSchiessen.setPower(hwMap.geschSchuss);
                    hwMap.mHoch.setPower(1);
                    hwMap.mBoden.setPower(-1);
                    hwMap.crsRad.setPower(1);
                    warteSchleife(800);
                    hwMap.mHoch.setPower(0);
                    break;
                case ALLES_AN:
                    statusSchuss = SchussStatus.STOPP;
                    hwMap.mSchiessen.setPower(hwMap.geschSchuss);
                    hwMap.mHoch.setPower(1);
                    warteSchleife(2000);
                    hwMap.mSchiessen.setPower(0);
                    hwMap.mHoch.setPower(0);
                    hwMap.mBoden.setPower(0);
                    hwMap.crsRad.setPower(0);
                    statusSchuss = SchussStatus.AUS;
                    break;
            }
        }
    }
}
