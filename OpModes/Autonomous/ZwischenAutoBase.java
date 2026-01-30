package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

public abstract class ZwischenAutoBase extends BaseAutonomous {
    public void schiessen() {
        hwMap.m_schiessen.setPower(hwMap.gesch_schuss);
        loop_wait(2000);
        hwMap.m_hoch.setPower(1);
        loop_wait(1300);
        hwMap.m_hoch.setPower(0);
        loop_wait(2000);

        hwMap.m_hoch.setPower(1);
        hwMap.m_boden.setPower(-1);
        hwMap.crs_rad.setPower(1);
        loop_wait(1300);
        hwMap.m_hoch.setPower(0);
        loop_wait(2000);

        hwMap.m_hoch.setPower(1);
        loop_wait(1300);
        hwMap.m_schiessen.setPower(0);
        hwMap.m_hoch.setPower(0);
        hwMap.m_boden.setPower(0);
        hwMap.crs_rad.setPower(0);
    }

    public void aufnehmen() {
        hwMap.navi.setSpeed(1, 0, 0);

        hwMap.m_aufnehmen.setPower(hwMap.gesch_aufnehmen);
        hwMap.m_boden.setPower(-1);
        hwMap.crs_rad.setPower(1);
        loop_wait(100);
        hwMap.crs_rad.setPower(0);
        loop_wait(100);
        hwMap.m_boden.setPower(0);
        loop_wait(100);
        hwMap.m_aufnehmen.setPower(0);

        hwMap.navi.setSpeed(0, 0, 0);
    }
}