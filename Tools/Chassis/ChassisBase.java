package org.firstinspires.ftc.teamcode.Tools.Chassis;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Tools.DTypes.Velocity;
import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;

/*
vx

0.0+0.0
    | +0.0 |
    | +0.0 |
    |  0.0 |
+0.0+-----+ 0.0
 */

//TODO add l_x, l_y
public abstract class ChassisBase implements Chassis {
    protected BNO055IMU imu;
    protected BNO055IMU.Parameters imu_prameters;

    protected  Velocity velocity;
    protected Position2D drivenDistance;
    protected double[] wheelSpeeds;
    protected double[] wheelSpeedsFactors;
    private DcMotor[] wheelMotors;
    private int[] wheelMotorSteps;
    protected int[] deltaWheelMotorSteps;
    private float rotation_offset;
    private float rotation;
    private int rotation_axis;

    /**
     * create chassis
     */
    public ChassisBase(int numWheels) {
        imu_prameters = new BNO055IMU.Parameters();
        imu_prameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu_prameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu_prameters.calibrationDataFile = "BNO055IMUCalibration.jason";
        imu_prameters.loggingEnabled = true;
        imu_prameters.loggingTag = "IMU";
        imu_prameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        rotation_offset = 0;
        rotation = 0;
        rotation_axis = 1;

        drivenDistance = new Position2D();
        wheelMotors = new DcMotor[numWheels];
        wheelSpeeds = new double[numWheels];
        wheelSpeedsFactors = new double[numWheels];
        wheelMotorSteps = new int[numWheels];
        deltaWheelMotorSteps = new int[numWheels];
    }

    // TODO: javadoc
    public void populateMotorArray(HardwareMap hw_map) {
        for (int i = 0; i < this.wheelMotors.length; i++) {
            wheelMotors[i] = hw_map.get(DcMotor.class, String.format("wheelMotor_%d", i));
            wheelSpeeds[i] = 0.0;
            wheelSpeedsFactors[i] = 1.0;
            wheelMotors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            wheelMotors[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            wheelMotorSteps[i] = wheelMotors[i].getCurrentPosition();
            deltaWheelMotorSteps[i] = wheelMotorSteps[i];
        }

        imu = hw_map.get(BNO055IMU.class,"imu");
        imu.initialize(imu_prameters);
        setRotation(0.0f);
    }

    public void setFactor(int wheelIndex, double factor) {
        if (wheelIndex > 0 && wheelIndex < wheelSpeedsFactors.length)
            wheelSpeedsFactors[wheelIndex] = factor;
    }

    /**
     * set Motor powers
     */
    private void setMotorSpeeds() {
        for (int i = 0; i < wheelMotors.length; i++) {
            wheelMotors[i].setPower(wheelSpeeds[i] * wheelSpeedsFactors[i]);
        }
    }

    /**
     * set wheel speeds based on velocity
     * @param velocity robot velocity
     */
    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }

    /**
     * get driven distance since last step
     * @return driven distance
     */
    public Position2D getDrivenDistance() {
        return drivenDistance;
    }

    /**
     * stop chassis movement
     */
    public void stopMotors() {
        setVelocity(new Velocity());
        setMotorSpeeds();
    }

    /**
     * update info about delta steps of motors
     */
    private void updateMotorSteps() {
        int steps;
        for (int i=0; i<deltaWheelMotorSteps.length; i++) {
            steps = wheelMotors[i].getCurrentPosition();
            deltaWheelMotorSteps[i] = steps - wheelMotorSteps[i];
            wheelMotorSteps[i] = steps;
        }
    }

    public void setRotation(float rotation) {
        rotation_offset = -getRawRotation() + rotation;
    }

    public void setRotationAxis(int axis) {
        rotation_axis = axis;
    }

    public float getRotation() {
        return rotation;
    }

    /**
     * debug info
     */
    public String debug() {
        String ret = String.format(
                "--- Chassis Debug ---\nvelocity :: vx=%+1.2f vy=%+1.2f wz=%+1.2f\ndrivenDistance :: x=%+2.2f y=%+2.2f\nrotation :: %+3.2f\nrawation :: %+3.2f + %+3.2f\n",
                velocity.getVX(), velocity.getVY(), velocity.getWZ(), drivenDistance.getX(), drivenDistance.getY(), getRotation(), getRawRotation(), rotation_offset);

        // add wheel debug
        for (int i=0; i<wheelMotors.length; i++) {
            ret += String.format("Wheel %d :: v=%+1.2f  steps=%+5d  dstep=%+3d\n", i, wheelSpeeds[i], wheelMotors[i].getCurrentPosition(), deltaWheelMotorSteps[i]);
        }

        return ret;
    }

    private float getRawRotation() {
        switch(rotation_axis) {
            case 1: return imu.getAngularOrientation().firstAngle;
            case 2: return imu.getAngularOrientation().secondAngle;
            case 3: return imu.getAngularOrientation().thirdAngle;
            default: return 0.0f;
        }
    }

    private void calculateRotation() {
        rotation = getRawRotation();
        rotation += 180; // make positive
        rotation += rotation_offset; // add offset
        rotation %= 360;
        rotation -= 180;
    }

    /**
     * update
     */
    public void step() {
        calculateRotation();
        setMotorSpeeds();
        updateMotorSteps();
    }
}