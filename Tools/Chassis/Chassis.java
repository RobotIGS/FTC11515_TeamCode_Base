package org.firstinspires.ftc.teamcode.Tools.Chassis;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Tools.Datatypes.Position2D;
import org.firstinspires.ftc.teamcode.Tools.Datatypes.Velocity;

/**
 * every Chassis needs to implement this interface
 */
public interface Chassis {
    /**
     * get hardware interfaces
     *
     * @param hw_map the hw_map
     */
    void populateMotorArray(HardwareMap hw_map);

    /**
     * set a velocity factor for the given wheel (~ software gearbox ; this enables the usage of different motors)
     *
     * @param wheelIndex the index of the wheel
     * @param factor     the velocity factor
     */
    void setWheelVelocityFactor(int wheelIndex, double factor);

    /**
     * set the wheel rotation velocities accordingly to the given velocity
     *
     * @param velocity the chassis velocity
     */
    void setVelocity(Velocity velocity);

    /**
     * get the driven distance when supported by the chassis
     *
     * @return delta in position since last step or (0|0)
     */
    Position2D getDrivenDistance();

    /**
     * get the rotation of the chassis on the filed if supported by the chassis
     *
     * @return the rotation of the chassis or 0
     */
    double getRotation();

    /**
     * set the rotation of the chassis on the field
     *
     * @param rotation the rotation in [0;360]
     */
    void setRotation(double rotation);

    /**
     * get the capabilities of the chassis
     *
     * @return capabilities
     */
    ChassisCapabilities getCapabilities();

    /**
     * stop all motor movements
     */
    void stopMotors();

    /**
     * get some debug infos for the chassis
     *
     * @return a string containing the debug infos
     */
    String debug();

    /**
     * update states, recalculate rotation and distance driven/etc
     */
    void step();

    void setDrivingEncoderStepsPerRotation(double driving_encoder_steps_per_rotation);
}
