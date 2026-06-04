package org.firstinspires.ftc.teamcode.Tools.Chassis;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

import org.firstinspires.ftc.teamcode.Tools.Datatypes.Position2D;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Velocity;

// based on https://research.ijcaonline.org/volume113/number3/pxc3901586.pdf
public class MecanumChassis extends ChassisBase {
    private final double WHEELDIAMETER = 10.4; // wheel diameter in centimeters
    private final double ONE_OVER_R = 1 / (WHEELDIAMETER / 2);

    private double[][] forwardMatrix;
    private double[][] backwardMatrix;

    /**
     * get mecanum chassis
     *
     * @param lx the sideways distance between wheel center and robot center
     * @param ly the forwards distance between wheel center and robot center
     */
    public MecanumChassis(int lx, int ly, RevHubOrientationOnRobot hubOrientation) {
        super(4, hubOrientation);

        // allow sideways motion
        capabilities.setDriveSideways(true);

        // TODO: stimmt diese matrix
        forwardMatrix = new double[][]{
                {+1, -1, -(lx + ly)},
                {+1, +1, +(lx + ly)},
                {+1, +1, -(lx + ly)},
                {+1, -1, +(lx + ly)}
        };
        backwardMatrix = new double[][]{
                {+1, +1, +1, +1},
                {-1, +1, +1, -1},
                {(double) -1 / (lx + ly),
                        (double) 1 / (lx + ly),
                        (double) -1 / (lx + ly),
                        (double) 1 / (lx + ly)}
        };
    }

    @Override
    public void setVelocity(Velocity velocity) {
        super.setVelocity(velocity);

        // perform the calculation based on the matrix above
        for (int i = 0; i < 4; i++) {
            wheelSpeeds[i] = ONE_OVER_R * (
                    forwardMatrix[i][0] * velocity.getVX() +
                            forwardMatrix[i][1] * -velocity.getVY() +
                            forwardMatrix[i][2] * velocity.getWZ()
            );
        }

        // normalize the values [-1.0; 1.0]
        double vm = Math.max(Math.max(Math.abs(wheelSpeeds[0]), Math.abs(wheelSpeeds[1])), Math.max(Math.abs(wheelSpeeds[2]), Math.abs(wheelSpeeds[3])));
        wheelSpeeds[0] *= velocity.getAbsolute() / vm;
        wheelSpeeds[1] *= velocity.getAbsolute() / vm;
        wheelSpeeds[2] *= velocity.getAbsolute() / vm;
        wheelSpeeds[3] *= velocity.getAbsolute() / vm;
    }

    @Override
    public void step() {
        super.step();

        double dx = 0;
        double dy = 0;

        // calculate steps
        for (int i = 0; i < 4; i++) {
            dx += backwardMatrix[0][i] * deltaWheelMotorSteps[i];
            dy += backwardMatrix[1][i] * deltaWheelMotorSteps[i];
        }
        dx /= 4;
        dy /= 4;

        // calculate rotations
        dx /= this.driving_encoder_steps_per_rotation;
        dy /= this.driving_encoder_steps_per_rotation;

        // calculate distance
        dx *= 2 * Math.PI * (WHEELDIAMETER / 2);
        dy *= -2 * Math.PI * (WHEELDIAMETER / 2);

        drivenDistance = new Position2D(dx, dy);
    }
}
