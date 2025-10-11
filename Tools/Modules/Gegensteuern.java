package org.firstinspires.ftc.teamcode.Tools.Modules;

import android.annotation.SuppressLint;

import java.util.ArrayList;

public class Gegensteuern {
    String richtung = "";

    int gegensteuern = 0;
    long bremszeit = 0;
    double durchschnitt = 0;
    double rückgabe = 0;
    long time = 0;
    ArrayList<Double> liste = new ArrayList<Double>();

    public Gegensteuern (String richtung) {
        this.richtung = richtung;
    }

    public double gegensteuern(double steuer_alt, double steuer_neu) {
        rückgabe = steuer_neu;
        liste.add(steuer_neu);
        if (liste.size() > 100) {
            liste.remove(0);
        }

        durchschnitt = liste.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        bremszeit = Math.abs(Math.round(500 * durchschnitt));

        if (steuer_neu == 0 && Math.abs(steuer_alt) > 0) {
            gegensteuern++;
            time = System.currentTimeMillis() + bremszeit;
        }

        if (time > System.currentTimeMillis()) {
            if (durchschnitt > 0) {
                rückgabe = -1;
            } else {
                rückgabe = 1;
            }
        }

        return rückgabe;
    }

    @SuppressLint("DefaultLocale")
    public String debug() {
        String ret = "--- Gegensteuern Debug " + this.richtung + " ---\n";
        ret += String.format("gegensteuern : %d\n", gegensteuern);
        ret += String.format("durchschnitt : %+1.2f\n", durchschnitt);
        ret += String.format("bremszeit : %d\n", bremszeit);
        ret += String.format("rückgabe : %+1.1f\n", rückgabe);
        return ret;
    }
}

