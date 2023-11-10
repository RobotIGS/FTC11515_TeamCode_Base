package org.firstinspires.ftc.teamcode.Tools.Modules;

import com.qualcomm.robotcore.hardware.DcMotor;

public class RopeLift {
    private DcMotor LiftMotor;
    private int steps_min;
    private int steps_max;
    private int steps_sum;

    public RopeLift() {
    }

    public void setPower(double power,boolean W) {
        if (W)
            LiftMotor.setPower(power);
        else {
            if (Math.max(LiftMotor.getCurrentPosition(), steps_min) == Math.min(LiftMotor.getCurrentPosition(), steps_max))
                LiftMotor.setPower(power);
            else
                setTargetPosition(((LiftMotor.getCurrentPosition() > steps_max) ? steps_max : steps_min), power);
            if (LiftMotor.getMode() != DcMotor.RunMode.RUN_USING_ENCODER)
                LiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
    public void setTargetPosition(int steps, double v) {
        steps_sum = (Math.min(steps_min + steps, steps_max));
        LiftMotor.setTargetPosition(steps_sum);

        if (LiftMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION)
            LiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        LiftMotor.setPower(v);
    }
    public void step() {
    }
}
