package org.firstinspires.ftc.teamcode.Tools;

public class Gegensteuern {
    public static double gegensteuern(double steuer_alt, double steuer_neu) {
        if (Math.abs(steuer_neu) < 0.2 && Math.abs(steuer_alt) > Math.abs(steuer_neu)) {
            if (steuer_alt > 0) {
                return -1;
            } else {
                return 1;
            }
        }
        return steuer_neu;
    }
}
