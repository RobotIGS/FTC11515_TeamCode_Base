package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import static org.firstinspires.ftc.teamcode.Tools.FieldNavigation.plattenlänge;

import org.firstinspires.ftc.teamcode.Tools.Datatypes.Position2D;

public class AutoGerade extends ZwischenAutoBase {
    @Override
    public void run() {
        hwMap.robot.drive_to_pos(new Position2D(4 * plattenlänge, plattenlänge * (isRed ? 1 : -1)));
        loop_while_driving();
        hwMap.robot.rotate(isRed ? -20 : 20);
        loop_while_driving();
        schiessen();

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
