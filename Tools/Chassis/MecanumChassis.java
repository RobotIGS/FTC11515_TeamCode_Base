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
    private final double WHEELDIAMETER = 10; // wheel diameter in centimeters
    private final double ONE_OVER_R = 1/(WHEELDIAMETER/2);
    private final double R_OVER_4 = (WHEELDIAMETER/2)/4;
    private int lx = 1;
    private int ly = 1;

    // based on https://research.ijcaonline.org/volume113/number3/pxc3901586.pdf
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

    /**
     * get mecanum chassis
     * @param lx the sideways distance between wheel center and robot center
     * @param ly the forwards distance between wheel center and robot center
      */
    public MecanumChassis(int lx, int ly) {
        super(4);

        // allow sideways motion
        capabilities.setDriveSideways(true);

        // save lx and ly
        this.lx = lx;
        this.ly = ly;
    }

    /**
     * get squared mecanum chassis
     */
    public MecanumChassis() {
        this(1,1);
    }

    @Override
    public void setVelocity(Velocity velocity) {
        super.setVelocity(velocity);

        // perform the calculation based on the matrix above
        for (int i=0; i<4; i++) {
            wheelSpeeds[i] = ONE_OVER_R * (
                    forwardMatrix[i][0] *  velocity.getVX() +
                    forwardMatrix[i][1] * -velocity.getVY() +
                    forwardMatrix[i][2] *  velocity.getWZ()
            );
        }

        // normalize the values ( in [-1.0;1.0])
        double vm = Math.max(Math.max(Math.abs(wheelSpeeds[0]), Math.abs(wheelSpeeds[1])),
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
