package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

public abstract class ZwischenAutoBase extends BaseAutonomous {
    public void schiessen() {
        hwMap.m_schiessen.setPower(-hwMap.gesch_schuss);
        loop_wait(2000);
        hwMap.m_aufnehmen.setPower(1);
        loop_wait(8000);
        hwMap.s_kick_seite.setPosition(hwMap.s_kick_boden_kurzposition);
        loop_wait(3000);
        hwMap.s_kick_seite.setPosition(hwMap.s_kick_boden_dauerposition);
        hwMap.m_schiessen.setPower(0);
        hwMap.m_aufnehmen.setPower(0);
    }

    public void aufnehmen() {
        hwMap.navi.setSpeed(hwMap.gesch_aufnehmen, 0, 0);
        hwMap.m_aufnehmen.setPower(1);
        loop_wait(500);
        loop_wait(500);
        loop_wait(500);
        hwMap.m_aufnehmen.setPower(-1);
        loop_wait(50);
        hwMap.m_aufnehmen.setPower(0);
        loop_wait(500);
        hwMap.navi.setSpeed(0, 0, 0);
    }
}