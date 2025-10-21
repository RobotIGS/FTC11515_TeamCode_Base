package org.firstinspires.ftc.teamcode.Tools;

import android.annotation.SuppressLint;

import java.util.ArrayDeque;

public class Gegensteuern {
    final int MAX_BREMSZEIT = 750;
    String richtung;

    long bremszeit = 0;
    double durchschnitt = 0;
    double endwert = 0;
    long bremszeit_ende = 0;
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
        if (liste.size() > 10) {
            summe -= liste.removeFirst();
        }

        durchschnitt = summe / liste.size();
        bremszeit = Math.round(MAX_BREMSZEIT * Math.pow(durchschnitt, 2));

        if (Math.abs(steuer_neu) < 0.02 && steuer_alt != 0) {
            bremszeit_ende = System.currentTimeMillis() + bremszeit;
        } else {
            bremszeit_ende = System.currentTimeMillis();
        }

        if (bremszeit_ende > System.currentTimeMillis()) {
            endwert = - Math.max(Math.min(2 * durchschnitt, 1), -1);
        }
        return endwert;
    }

    @SuppressLint("DefaultLocale")
    public String debug() {
        String ret = "--- Gegensteuern Debug " + this.richtung + " ---\n";
        ret += String.format("durchschnitt : %+1.4f\n", durchschnitt);
        ret += String.format("bremszeit : %+1.4f s\n", (double) bremszeit / 1000);
        ret += String.format("rückgabe : %+1.4f\n", endwert);
        return ret;
    }
}

