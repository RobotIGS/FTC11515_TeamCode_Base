package org.firstinspires.ftc.teamcode.Tools;

import android.annotation.SuppressLint;

import java.util.ArrayDeque;

public class Gegensteuern {
    final int MAX_BREMSZEIT = 500;
    String richtung;

    long bremszeit = 0;
    double durchschnitt = 0;
    double endwert = 0;
    long time = 0;
    ArrayDeque<Double> liste = new ArrayDeque<>();
    double summe = 0;

    public Gegensteuern(String richtung) {
        this.richtung = richtung;
    }

    public double gegensteuern(boolean geg, double steuer_alt, double steuer_neu) {
        if (!geg) {
            return steuer_neu;
        }

        endwert = steuer_neu;

        liste.add(steuer_neu);
        summe += steuer_neu;
        if (liste.size() > 100) {
            summe -= liste.removeFirst();
        }

        durchschnitt = !liste.isEmpty() ? summe / liste.size() : 0;
        bremszeit = Math.round(MAX_BREMSZEIT * Math.pow(durchschnitt, 2));

        if (steuer_neu == 0 && steuer_alt != 0) {
            liste.clear();
            time = System.currentTimeMillis() + bremszeit;
        } else if (steuer_neu != 0) {
            liste.clear();
            time = System.currentTimeMillis();
        }

        if (time > System.currentTimeMillis()) {
            if (durchschnitt > 0) {
                endwert = -1;
            } else {
                endwert = 1;
            }
        }
        return endwert;
    }

    @SuppressLint("DefaultLocale")
    public String debug() {
        String ret = "--- Gegensteuern Debug " + this.richtung + " ---\n";
        ret += String.format("durchschnitt : %+1.2f\n", durchschnitt);
        ret += String.format("bremszeit : %+1.4f s\n", (double) bremszeit / 1000);
        ret += String.format("rückgabe : %+1.1f\n", endwert);
        return ret;
    }
}

