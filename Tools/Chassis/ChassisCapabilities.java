package org.firstinspires.ftc.teamcode.Tools.Chassis;

// TODO javadoc

public class ChassisCapabilities {
    private boolean rotate;
    private boolean drive_forward;
    private boolean drive_sideways;
    private boolean get_rotation;
    private boolean get_drivenDistance;

    /**
     * create capabilities structure for chassis
     */
    public ChassisCapabilities() {
        rotate = false;
        drive_forward = false;
        drive_sideways = false;
        get_rotation = false;
        get_drivenDistance = false;
    }

    // check capabilities
    public boolean getRotate() {
        return rotate;
    }

    // set capabilities
    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }

    public boolean getDriveForward() {
        return drive_forward;
    }

    public void setDriveForward(boolean drive_forward) {
        this.drive_forward = drive_forward;
    }

    public boolean getDriveSideways() {
        return drive_sideways;
    }

    public void setDriveSideways(boolean drive_sideways) {
        this.drive_sideways = drive_sideways;
    }

    public boolean getGetRotation() {
        return get_rotation;
    }

    public void setGetRotation(boolean get_rotation) {
        this.get_rotation = get_rotation;
    }

    public boolean getGetDrivenDistance() {
        return get_drivenDistance;
    }

    public void setGetDrivenDistance(boolean get_drivenDistance) {
        this.get_drivenDistance = get_drivenDistance;
    }
}