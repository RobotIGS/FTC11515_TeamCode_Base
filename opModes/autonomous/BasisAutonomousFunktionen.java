package org.firstinspires.ftc.teamcode.opModes.autonomous;

public abstract class BasisAutonomousFunktionen extends BasisAutonomous {

    public void starten() {
        super.starten();
    }

    public void schiessen() {
        hwMap.mSchiessen.setPower(hwMap.geschSchuss);
        schleifeWarten(2000);
        hwMap.mHoch.setPower(1);
        schleifeWarten(1300);
        hwMap.mHoch.setPower(0);
        schleifeWarten(2000);

        hwMap.sKickSeite.setPosition(hwMap.sKickSeiteKurzposition);
        hwMap.mHoch.setPower(1);
        hwMap.mBoden.setPower(-1);
        hwMap.crsRad.setPower(1);
        schleifeWarten(1300);
        hwMap.sKickSeite.setPosition(hwMap.sKickSeiteDauerposition);
        hwMap.mHoch.setPower(0);
        schleifeWarten(2000);

        hwMap.mAufnehmen.setPower(hwMap.geschAufnehmen);
        hwMap.sKickBoden.setPosition(hwMap.sKickBodenKurzposition);
        hwMap.mHoch.setPower(1);
        schleifeWarten(10000);
        hwMap.sKickBoden.setPosition(hwMap.sKickBodenDauerposition);
        hwMap.mSchiessen.setPower(0);
        hwMap.mHoch.setPower(0);
        hwMap.mBoden.setPower(0);
        hwMap.crsRad.setPower(0);
        hwMap.mAufnehmen.setPower(0);
        schleifeWarten(500);
    }

    public void aufnehmen() {
        hwMap.navi.setGeschwindigkeit(1, 0, 0);

        hwMap.mAufnehmen.setPower(hwMap.geschAufnehmen);
        hwMap.mBoden.setPower(-1);
        hwMap.crsRad.setPower(1);
        schleifeWarten(100);
        hwMap.crsRad.setPower(0);
        schleifeWarten(100);
        hwMap.mBoden.setPower(0);
        schleifeWarten(100);
        hwMap.mAufnehmen.setPower(0);

        hwMap.navi.setGeschwindigkeit(0, 0, 0);
    }
}
