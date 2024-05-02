package org.firstinspires.ftc.teamcode.Tools.Chassis;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Tools.DTypes.Rotation;
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
    protected BNO055IMU.Parameters imu_parameters;

    protected  Velocity velocity;
    protected Position2D drivenDistance;
    protected double[] wheelSpeeds;
    protected double[] wheelSpeedsFactors;
    private final DcMotor[] wheelMotors;
    private final int[] wheelMotorSteps;
    protected int[] deltaWheelMotorSteps;
    private double rotation_offset;
    private final Rotation rotation;
    private int rotation_axis;

    // set default capabilities for any chassis
    protected ChassisCapabilities capabilities;

    /**
     * create chassis
     */
    public ChassisBase(int numWheels) {
        capabilities = new ChassisCapabilities();

        // rotation stuff
        capabilities.setGetRotation(true);
        capabilities.setRotate(true);

        imu_parameters = new BNO055IMU.Parameters();
        imu_parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu_parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu_parameters.calibrationDataFile = "BNO055IMUCalibration.jason";
        imu_parameters.loggingEnabled = true;
        imu_parameters.loggingTag = "IMU";
        imu_parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        rotation_offset = 0;
        rotation = new Rotation(0.0);
        rotation_axis = 1;

        // drive stuff
        capabilities.setGetDrivenDistance(true);
        capabilities.setDriveForward(true);

        drivenDistance = new Position2D();
        wheelMotors = new DcMotor[numWheels];
        wheelSpeeds = new double[numWheels];
        wheelSpeedsFactors = new double[numWheels];
        wheelMotorSteps = new int[numWheels];
        deltaWheelMotorSteps = new int[numWheels];
    }

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
        imu.initialize(imu_parameters);
        setRotation(0.0f);
    }

    public void setWheelVelocityFactor(int wheelIndex, double factor) {
        if (wheelIndex > 0 && wheelIndex < wheelSpeedsFactors.length)
            wheelSpeedsFactors[wheelIndex] = factor;
    }

    private void setMotorSpeeds() {
        for (int i = 0; i < wheelMotors.length; i++) {
            wheelMotors[i].setPower(wheelSpeeds[i] * wheelSpeedsFactors[i]);
        }
    }

    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }

    public Position2D getDrivenDistance() {
        return drivenDistance;
    }

    public void stopMotors() {
        setVelocity(new Velocity());
        setMotorSpeeds();
    }

    private void updateMotorSteps() {
        int steps;
        for (int i=0; i<deltaWheelMotorSteps.length; i++) {
            steps = wheelMotors[i].getCurrentPosition();
            deltaWheelMotorSteps[i] = steps - wheelMotorSteps[i];
            wheelMotorSteps[i] = steps;
        }
    }

    public void setRotation(double rotation) {
        rotation_offset = -getRawRotation() + rotation;
    }

    public void setRotationAxis(int axis) {
        rotation_axis = axis;
    }

    public double getRotation() {
        return rotation.get();
    }

    public ChassisCapabilities getCapabilities() {
        return capabilities;
    }

    public String debug() {
        String ret = "--- Chassis Debug ---\n";
        if (velocity != null)
            ret += String.format("velocity :: vx=%+1.2f vy=%+1.2f wz=%+1.2f", velocity.getVX(), velocity.getVY(), velocity.getWZ());
        if (drivenDistance != null)
            ret += String.format(
                "drivenDistance :: x=%+2.2f y=%+2.2f", drivenDistance.getX(), drivenDistance.getY());
        ret += String.format("\nrotation :: %+3.2f\n", getRotation());

        // add wheel debug
        for (int i=0; i<wheelMotors.length; i++) {
            ret += String.format("Wheel %d :: v=%+1.2f  steps=%+5d  delta steps=%+3d\n", i, wheelSpeeds[i], wheelMotors[i].getCurrentPosition(), deltaWheelMotorSteps[i]);
        }

        return ret;
    }

    private float getRawRotation() {
        switch(rotation_axis) {
            case 1: return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle;
            case 2: return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).secondAngle;
            case 3: return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
            default: return 0.0f;
        }
    }

    private void calculateRotation() {
        rotation.set(getRawRotation() + rotation_offset);
    }

    public void step() {
        calculateRotation();
        setMotorSpeeds();
        updateMotorSteps();
    }
}