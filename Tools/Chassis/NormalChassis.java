package org.firstinspires.ftc.teamcode.Tools.Chassis;

import org.firstinspires.ftc.teamcode.Tools.DTypes.Velocity;
import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;

/**
 * Robot:
 *
 * W0+----+W1
 *   |    |
 *   |    |
 * W3+----+W2
 *
 */

public class NormalChassis extends ChassisBase {
    private final double WHEELDIAMETER = 9.35; // wheel diameter in centimeters
    private final double ONE_OVER_R = 1/(WHEELDIAMETER/2);
    private final double R_OVER_4 = (WHEELDIAMETER/2)/4;

    public NormalChassis() {
        super(4);
    }

    @Override
    public void setVelocity(Velocity velocity) {
        wheelSpeeds[0] = -velocity.getVX() - velocity.getWZ();
        wheelSpeeds[1] =  velocity.getVX() - velocity.getWZ();
        wheelSpeeds[2] =  velocity.getVX() - velocity.getWZ();
        wheelSpeeds[3] = -velocity.getVX() - velocity.getWZ();
    }

    @Override
    public void step() {
        super.step();

        // calculate driven distance
        double dx = -deltaWheelMotorSteps[0] + deltaWheelMotorSteps[1] + deltaWheelMotorSteps[2] - deltaWheelMotorSteps[3];

        dx *= R_OVER_4;

        // calculate rotations
        dx /= 1478.4;

        // calculate distance
        dx *= 2 * Math.PI;

        drivenDistance = new Position2D(dx,0.0);
    }
}
