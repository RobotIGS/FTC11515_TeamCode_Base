package org.firstinspires.ftc.teamcode.Tools.Chassis;

import android.annotation.SuppressLint;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Position2D;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Rotation;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Velocity;

public abstract class ChassisBase implements Chassis {
    private final DcMotor[] wheelMotors;
    private final int[] wheelMotorSteps;
    private final Rotation rotation;
    public IMU imu;
    protected double driving_encoder_steps_per_rotation;
    protected RevHubOrientationOnRobot hubOrientationOnRobot;
    protected Velocity velocity;
    protected Position2D drivenDistance;
    protected double[] wheelSpeeds;
    protected int[] deltaWheelMotorSteps;
    protected ChassisCapabilities capabilities;
    private double rotation_offset;

    public ChassisBase(int numWheels, RevHubOrientationOnRobot huborientation) {
        this.capabilities = new ChassisCapabilities();

        // rotation stuff
        this.capabilities.setGetRotation(true);
        this.capabilities.setRotate(true);
        this.hubOrientationOnRobot = huborientation;
        this.rotation_offset = 0;
        this.rotation = new Rotation(0.0);

        // drive stuff
        this.capabilities.setGetDrivenDistance(true);
        this.capabilities.setDriveForward(true);
        this.drivenDistance = new Position2D();
        this.wheelMotors = new DcMotor[numWheels];
        this.wheelSpeeds = new double[numWheels];
        this.wheelMotorSteps = new int[numWheels];
        this.deltaWheelMotorSteps = new int[numWheels];
    }

    public void setDrivingEncoderStepsPerRotation(double driving_encoder_steps_per_rotation) {
        this.driving_encoder_steps_per_rotation = driving_encoder_steps_per_rotation;
    }

    public void populateMotorArray(HardwareMap hw_map) {
        wheelMotors[0] = hw_map.get(DcMotor.class, "front_left_motor");
        wheelMotors[1] = hw_map.get(DcMotor.class, "front_right_motor");
        wheelMotors[2] = hw_map.get(DcMotor.class, "back_left_motor");
        wheelMotors[3] = hw_map.get(DcMotor.class, "back_right_motor");

        for (int i = 0; i < this.wheelMotors.length; i++) {
            wheelSpeeds[i] = 0.0;
            wheelMotors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            wheelMotors[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            wheelMotorSteps[i] = wheelMotors[i].getCurrentPosition();
            deltaWheelMotorSteps[i] = wheelMotorSteps[i];
        }

        imu = hw_map.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(this.hubOrientationOnRobot));
        imu.resetYaw();
        setRotation(0.0);
    }

    private void setMotorSpeeds() {
        for (int i = 0; i < wheelMotors.length; i++) {
            wheelMotors[i].setPower(wheelSpeeds[i]);
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
        for (int i = 0; i < deltaWheelMotorSteps.length; i++) {
            steps = wheelMotors[i].getCurrentPosition();
            deltaWheelMotorSteps[i] = steps - wheelMotorSteps[i];
            wheelMotorSteps[i] = steps;
        }
    }

    public double getRotation() {
        return rotation.get();
    }

    public void setRotation(double rotation) {
        rotation_offset = -getRawRotation() + rotation;
    }

    public ChassisCapabilities getCapabilities() {
        return capabilities;
    }

    private float getRawRotation() {
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        return (float) orientation.getYaw(AngleUnit.DEGREES);
    }

    private void calculateRotation() {
        rotation.set(getRawRotation() + rotation_offset);
    }

    public void step() {
        calculateRotation();
        setMotorSpeeds();
        updateMotorSteps();
    }

    @SuppressLint("DefaultLocale")
    public String debug() {
        String ret = "--- Chassis Debug ---\n";
        if (velocity != null)
            ret += String.format("velocity: vx=%+1.2f vy=%+1.2f wz=%+1.2f\n", velocity.getVX(), velocity.getVY(), velocity.getWZ());
        if (drivenDistance != null)
            ret += String.format("driven distance: x=%+2.2f y=%+2.2f\n", drivenDistance.getX(), drivenDistance.getY());

        // add wheel debug
        for (int i = 0; i < wheelMotors.length; i++) {
            ret += String.format("Wheel %d: v=%+1.2f  steps=%+5d  delta steps=%+3d\n", i, wheelSpeeds[i], wheelMotors[i].getCurrentPosition(), deltaWheelMotorSteps[i]);
        }

        return ret;
    }
}