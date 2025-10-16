package org.firstinspires.ftc.teamcode.Tools.Chassis;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;
import org.firstinspires.ftc.teamcode.Tools.DTypes.Velocity;

/**
 * Robot:
 * W0+----+W1
 * |        |
 * |        |
 * W3+----+W2
 */

public class NormalChassis extends ChassisBase {
    private final double WHEELDIAMETER = 9.15; // wheel diameter in centimeters

    public NormalChassis(RevHubOrientationOnRobot hubOrientation) {
        super(4, hubOrientation);
    }

    @Override
    public void setVelocity(Velocity velocity) {
        wheelSpeeds[0] = -velocity.getVX() - velocity.getWZ();
        wheelSpeeds[1] = velocity.getVX() - velocity.getWZ();
        wheelSpeeds[2] = velocity.getVX() - velocity.getWZ();
        wheelSpeeds[3] = -velocity.getVX() - velocity.getWZ();
    }

    @Override
    public void step() {
        super.step();

        // calculate driven steps
        double dx = -deltaWheelMotorSteps[0] + deltaWheelMotorSteps[1] + deltaWheelMotorSteps[2] - deltaWheelMotorSteps[3];

        dx /= 4;

        // calculate rotations
        dx /= this.driving_encoder_steps_per_rotation;

        // calculate distance
        dx *= 2 * Math.PI * (WHEELDIAMETER / 2);

        drivenDistance = new Position2D(dx, 0.0);
    }
}
