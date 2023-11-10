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
    public NormalChassis() {
        super(4);
    }

    @Override
    public void setVelocity(Velocity velocity) {
        // TODO: rotate
        wheelSpeeds[0] = -velocity.getVX() - velocity.getWZ();
        wheelSpeeds[1] =  velocity.getVX() - velocity.getWZ();
        wheelSpeeds[2] =  velocity.getVX() - velocity.getWZ();
        wheelSpeeds[3] = -velocity.getVX() - velocity.getWZ();
    }

    private void calculateDrivenDistance() {
        //drivenDistance;
        //deltaWheelMotorSteps;
    }

    @Override
    public void step() {
        super.step();

        // calculate driven distance


        drivenDistance = new Position2D(0.0,0.0);
    }
}
