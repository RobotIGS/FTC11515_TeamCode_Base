package org.firstinspires.ftc.teamcode.Tools;

import org.firstinspires.ftc.teamcode.OpModes.TeleOp.FullControl;

import java.util.ArrayList;

public class Gegensteuern {
    public static double gegensteuern(double steuer_alt, double steuer_neu) {
        long time = 0;
        double rückgabe = steuer_neu;
        ArrayList<Double> liste = new ArrayList<Double>();
        liste.add(steuer_neu);
        if (liste.size() > 100) {
            liste.remove(0);
        }

        if (Math.abs(steuer_neu) == 0 && Math.abs(steuer_alt) > Math.abs(steuer_neu)) {
            FullControl.gegensteuern++;
            if (steuer_alt > 0) {
                rückgabe = -1;
            } else {
                rückgabe = 1;
            }
        }
        if (time > 0) {

        }
        return rückgabe;
    }
}