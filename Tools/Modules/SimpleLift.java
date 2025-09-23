package org.firstinspires.ftc.teamcode.Tools.Modules;

import com.qualcomm.robotcore.hardware.DcMotor;

public class SimpleLift {
    private final DcMotor motor;
    private final int gravity_delta_position;
    private final boolean steps_negative_is_up;
    private int start_position;
    private int end_position;
    private double idle_power;

    /**
     * create a lift module which makes lift control simpler to use.
     *
     * @param motor        the motor used for the lift
     * @param steps_length the steps delta of the max and min position of the lift motor
     * @param neg_is_up    if negative steps is moving the lift upwards
     */
    public SimpleLift(DcMotor motor, int steps_length, boolean neg_is_up) {
        this.motor = motor;
        gravity_delta_position = -442;
        steps_negative_is_up = neg_is_up;
        idle_power = 0.3;

        // set current position as start position and calculate the end position
        setCurrentAsStartPosition();
        setDeltaStepsStartEnd(steps_length);

        // set start position as target and set power => hold start position
        motor.setTargetPosition(start_position);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(0.3);
    }

    /**
     * sets the current position as the start (min) position
     */
    public void setCurrentAsStartPosition() {
        int delta = start_position - motor.getCurrentPosition();
        start_position -= delta;
        end_position -= delta;
    }

    /**
     * sets the steps delta (length of the lift in steps)
     *
     * @param delta the steps delta of the max and min position of the lift motor
     */
    public void setDeltaStepsStartEnd(int delta) {
        end_position = start_position + (steps_negative_is_up ? -delta : delta);
    }

    /**
     * get current position of the lift in steps
     *
     * @return the position of the lift relative to the start position and positive = upwards
     */
    public int getCurrentPosition() {
        return (steps_negative_is_up ? -1 : 1) * (motor.getCurrentPosition() - start_position);
    }

    /**
     * returns if the lift is currently busy
     *
     * @return true/false if the lift is busy
     */
    public boolean isBusy() {
        return motor.isBusy();
    }

    /**
     * sets the target position
     *
     * @param target steps value relative to the start position (positive = upwards, value gets checked before usage)
     */
    public void setTargetPosition(int target) {
        // calculate target position for the motor while keeping the value between end and start value
        target = Math.max(0, Math.min(target, Math.abs(end_position - start_position)));
        if (steps_negative_is_up)
            target = -target;
        target += start_position;
        motor.setTargetPosition(target);
        if (motor.getMode() != DcMotor.RunMode.RUN_TO_POSITION)
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(idle_power);
    }

    /**
     * set power to the lift (kept in the min max steps interval)
     *
     * @param power [-1.0:1.0] positive is upwards
     */
    public void setPower(double power) {
        if (power != 0.0) {
            if (
                    (getCurrentPosition() <= gravity_delta_position && power < 0) ||
                            (getCurrentPosition() >= Math.abs(end_position - start_position) && power > 0)
            )
                motor.setPower(0.0);

            else {
                if (motor.getMode() != DcMotor.RunMode.RUN_USING_ENCODER)
                    motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motor.setPower(steps_negative_is_up ? -power : power);
            }
        } else if (motor.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            if ((getCurrentPosition()) <= 0) {
                motor.setTargetPosition(start_position);
            } else if ((getCurrentPosition()) >= Math.abs(end_position - start_position)) {
                motor.setTargetPosition(end_position);
            } else {
                motor.setTargetPosition(motor.getCurrentPosition());
            }
            motor.setPower(idle_power);
        }
    }

    /**
     * set power of lift motor without keeping the lift in the set interval
     *
     * @param power power of the lift motor
     */
    public void setRawPower(double power) {
        if (motor.getMode() != DcMotor.RunMode.RUN_USING_ENCODER)
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setPower(steps_negative_is_up ? -power : power);
    }

    /**
     * set idle power
     *
     * @param power idle power
     */
    public void setIdlePower(double power) {
        idle_power = power;
    }
}
