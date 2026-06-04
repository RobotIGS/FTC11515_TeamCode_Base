package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import static org.firstinspires.ftc.teamcode.Tools.FieldNavigation.PLATTENLAENGE;

import org.firstinspires.ftc.teamcode.Tools.Datatypes.Position2D;

public class AutoGerade extends ZwischenAutoBase {
    @Override
    public void run() {
        super.run();

        hwMap.robot.drive_to_pos(new Position2D(-4 * PLATTENLAENGE, -100));
        loop_while_driving();
        schiessen();

        if (false) {
            hwMap.robot.rotate(isRed ? -70 : 70);
            loop_while_driving();
            hwMap.robot.drive_to_pos(new Position2D(0, PLATTENLAENGE * (isRed ? 1 : -1)));
            loop_while_driving();
            aufnehmen();

            hwMap.robot.drive_to_pos(new Position2D(-PLATTENLAENGE, PLATTENLAENGE * (isRed ? -1 : 1)));
            loop_while_driving();
            hwMap.robot.rotate(isRed ? 70 : -70);
            loop_while_driving();
            schiessen();
        }
    }
}
