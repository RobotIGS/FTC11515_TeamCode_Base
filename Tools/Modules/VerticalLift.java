package org.firstinspires.ftc.teamcode.Tools.Modules;

import com.qualcomm.robotcore.hardware.DcMotor;

public class VerticalLift extends SimpleLift {
    private final double length;
    private final float angle;

    /**
     * create a lift module which makes lift control simpler to use.
     *
     * @param motor        the motor used for the lift
     * @param steps_length the steps delta of the max and min position of the lift motor
     * @param neg_is_up    if negative steps is moving the lift upwards
     * @param length       length of the lift (delta min max) in cm
     * @param angle        the angle between the lift and the horizontal axes
     */
    public VerticalLift(DcMotor motor, int steps_length, boolean neg_is_up, double length, float angle) {
        super(motor, steps_length, neg_is_up);
        this.length = length;
        this.angle = angle;
    }

    /**
     * drive to height (kept in min max steps interval)
     *
     * @param height height vertical (start position = 0) in cm
     */
    public void setTargetHeight(int height) {
        // TODO: calculate the target position based on the length and the angle use setTargetPosition()
    }
}