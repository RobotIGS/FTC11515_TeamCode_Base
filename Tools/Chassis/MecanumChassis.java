package org.firstinspires.ftc.teamcode.Tools.Chassis;

import org.firstinspires.ftc.teamcode.Tools.DTypes.Velocity;
import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;

/**
 * Robot:
 *
 * W0+----+W1
 *   |    |
 *   |    |
 * W2+----+W3
 *
 */

public class MecanumChassis extends ChassisBase {
    private final double WHEELDIAMETER = 10;
    private final double ONE_OVER_R = 1/(WHEELDIAMETER/2); //TODO check + add variable(r) + const
    private final double R_OVER_4 = (WHEELDIAMETER/2)/4;
    private final int lx = 1; // (a+b); // TODO a + b
    private final int ly = 1; // (a+b); // TODO a + b

    private final double[][] forwardMatrix = {
            {+1, -1, -(lx+ly)},
            {+1, +1, +(lx+ly)},
            {+1, +1, -(lx+ly)},
            {+1, -1, +(lx+ly)}
    };
    private final double[][] backwardMatrix = {
            {+1, +1, +1, +1},
            {-1, +1, +1, -1},
            {-1/(lx+ly), 1/(lx+ly), -1/(lx+ly), 1/(lx+ly)}
    };

    public MecanumChassis() {
        super(4);
    }

    @Override
    public void setVelocity(Velocity velocity) {
        double vm;
        super.setVelocity(velocity);
        // TODO: add source (vy inverted + right side inverted)
        // source : https://research.ijcaonline.org/volume113/number3/pxc3901586.pdf

        for (int i=0; i<4; i++) {
            wheelSpeeds[i] = ONE_OVER_R * (
                    forwardMatrix[i][0] *  velocity.getVX() +
                    forwardMatrix[i][1] * -velocity.getVY() +
                    forwardMatrix[i][2] *  velocity.getWZ()
            );
        }

        /*
        wheelSpeeds[0] =  (velocity.getVX() + velocity.getVY() - l * velocity.getWZ()) * ONE_OVER_R;
        wheelSpeeds[1] = -(velocity.getVX() - velocity.getVY() + l * velocity.getWZ()) * ONE_OVER_R; // negate because right
        wheelSpeeds[3] =  (velocity.getVX() - velocity.getVY() - l * velocity.getWZ()) * ONE_OVER_R;
        wheelSpeeds[2] = -(velocity.getVX() + velocity.getVY() + l * velocity.getWZ()) * ONE_OVER_R; // negate because right
    */
        // normalize the values
        vm = Math.max(Math.max(Math.abs(wheelSpeeds[0]), Math.abs(wheelSpeeds[1])),
                Math.max(Math.abs(wheelSpeeds[2]), Math.abs(wheelSpeeds[3])));
        wheelSpeeds[0] *= velocity.getAbsolute() / vm;
        wheelSpeeds[1] *= velocity.getAbsolute() / vm;
        wheelSpeeds[2] *= velocity.getAbsolute() / vm;
        wheelSpeeds[3] *= velocity.getAbsolute() / vm;

        // change rotation direction of the right motors
        wheelSpeeds[1] *= -1;
        wheelSpeeds[3] *= -1;
    }

    @Override
    public void step() {
        super.step();

        double dx = 0;
        double dy = 0;

        deltaWheelMotorSteps[1] *= -1;
        deltaWheelMotorSteps[3] *= -1;

        for (int i=0; i<4; i++) {
            dx += backwardMatrix[0][i] * deltaWheelMotorSteps[i];
            dy += backwardMatrix[1][i] * deltaWheelMotorSteps[i];
        }
        dx *= R_OVER_4;
        dy *= R_OVER_4;

        // calculate rotations
        dx /= 751.8;
        dy /= 751.8;

        // calculate distance
        dx *= 2 * Math.PI;
        dy *= -2 * Math.PI;

        drivenDistance = new Position2D(dx,dy);
    }
}
