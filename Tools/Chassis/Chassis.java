package org.firstinspires.ftc.teamcode.Tools.Chassis;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Tools.DTypes.Velocity;
import org.firstinspires.ftc.teamcode.Tools.DTypes.Position2D;

public interface Chassis {
	void populateMotorArray(HardwareMap hw_map);
	void setFactor(int wheelIndex, double factor);
	void setVelocity(Velocity velocity);
	void setRotation(double rotation);
	void setRotationAxis(int axis);
	Position2D getDrivenDistance();
	double getRotation();
	void stopMotors();
	String debug();
	void step();
}
