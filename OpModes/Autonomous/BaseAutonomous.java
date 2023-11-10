package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class BaseAutonomous extends LinearOpMode {
    /*
     * initialize the OpMode (Autonomous)
     */
    public abstract void initialize();

    /*
     * this methode runs the code
     */
    public abstract void run();

    /*
     * this internal methode is used to run initialize and run
     */
    public void runOpMode() {
        initialize();
        waitForStart();
        run();
    }
}
