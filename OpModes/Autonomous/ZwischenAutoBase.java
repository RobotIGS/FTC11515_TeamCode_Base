package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

public abstract class ZwischenAutoBase extends BaseAutonomous {
    public void schiessen() {
        hwMap.m_schiessen_l.setPower(-hwMap.schussgeschwindigkeit);
        hwMap.m_schiessen_r.setPower(hwMap.schussgeschwindigkeit);
        loop_wait(2000);
        hwMap.m_aufnehmen.setPower(1);
        hwMap.s_unten.setPower(-1);
        hwMap.s_oben.setPower(-1);
        loop_wait(8000);
        hwMap.s_kick.setPosition(hwMap.s_kick_kurzposition);
        loop_wait(3000);
        hwMap.s_kick.setPosition(hwMap.s_kick_dauerposition);
        hwMap.m_schiessen_l.setPower(0);
        hwMap.m_schiessen_r.setPower(0);
        hwMap.s_oben.setPower(0);
        hwMap.s_unten.setPower(0);
        hwMap.m_aufnehmen.setPower(0);
    }

    public void aufnehmen() {
        hwMap.navi.setSpeed(hwMap.aufnehm_geschwindigkeit, 0, 0);

        hwMap.m_aufnehmen.setPower(1);
        hwMap.s_unten.setPower(-1);
        hwMap.s_oben.setPower(-1);
        loop_wait(500);
        hwMap.s_oben.setPower(0); // erster Ball drin
        loop_wait(500);
        hwMap.s_unten.setPower(0); // zweiter Ball drin
        loop_wait(500);
        hwMap.s_unten.setPower(1); // dritter Ball drehen + zurückdrehen
        hwMap.s_oben.setPower(1);
        hwMap.m_aufnehmen.setPower(-1);
        loop_wait(50);
        hwMap.m_aufnehmen.setPower(0);
        loop_wait(500);
        hwMap.s_oben.setPower(0);
        hwMap.s_unten.setPower(0);

        hwMap.navi.setSpeed(0, 0, 0);
    }
}