package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class BaseTeleOp extends LinearOpMode {
    public abstract void initialize();
    public abstract void run();

    public void runOpMode() {
        initialize();
        waitForStart();
        while (opModeIsActive())
            run();
    }
}
