package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import static org.firstinspires.ftc.teamcode.Tools.FieldNavigation.plattenlänge;

import org.firstinspires.ftc.teamcode.Tools.Datatypes.Position2D;

public class AutoSchräg extends ZwischenAutoBase {
    @Override
    public void run() {
        hwMap.robot.drive_to_pos(new Position2D(plattenlänge, 0));
        loop_while_driving();
        schiessen();

        if (false) {
            hwMap.robot.rotate(isRed ? 130 : -130);
            loop_while_driving();
            hwMap.robot.drive_to_pos(new Position2D(plattenlänge * (isRed ? -1 : 1), 0));
            loop_while_driving();
            hwMap.robot.drive_to_pos(new Position2D(0, -5));
            loop_while_driving();
            aufnehmen();

            hwMap.robot.drive_to_pos(new Position2D(41, 0));
            loop_while_driving();
            hwMap.robot.rotate(isRed ? -130 : 130);
            loop_while_driving();
            hwMap.robot.drive_to_pos(new Position2D(plattenlänge * (isRed ? 1 : -1), 0));
            loop_while_driving();
            schiessen();

            // erste Ballladung fertig

            hwMap.robot.rotate(isRed ? 130 : -130);
            loop_while_driving();
            hwMap.robot.drive_to_pos(new Position2D(2 * plattenlänge * (isRed ? -1 : 1), 0));
            loop_while_driving();
            hwMap.robot.drive_to_pos(new Position2D(0, -5));
            loop_while_driving();
            aufnehmen();

            hwMap.robot.drive_to_pos(new Position2D(41, 0));
            loop_while_driving();
            hwMap.robot.rotate(isRed ? -130 : 130);
            loop_while_driving();
            hwMap.robot.drive_to_pos(new Position2D(2 * plattenlänge * (isRed ? 1 : -1), 0));
            loop_while_driving();
            schiessen();
        }
    }
}