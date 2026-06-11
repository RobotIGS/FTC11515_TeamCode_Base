package org.firstinspires.ftc.teamcode.tools.chassis;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

import org.firstinspires.ftc.teamcode.tools.datentypen.Geschwindigkeit;

// basierend auf https://research.ijcaonline.org/volume113/number3/pxc3901586.pdf
public class MecanumChassis extends ChassisBasis {
    private final double RAD_DURCHMESSER = 10.4; // Raddurchmesser in Zentimetern

    private final double[][] geschwindigkeitsMatrix;
    private final double[][] distanzMatrix;

    /**
     * @param lx der seitliche Abstand zwischen Radmitte und Robotermitte
     * @param ly der vorwärts gerichtete Abstand zwischen Radmitte und Robotermitte
     */
    public MecanumChassis(int lx, int ly, RevHubOrientationOnRobot hubAusrichtung) {
        super(4, hubAusrichtung);

        geschwindigkeitsMatrix = new double[][]{
                {+1, -1, +(lx + ly)},
                {+1, +1, -(lx + ly)},
                {+1, +1, +(lx + ly)},
                {+1, -1, -(lx + ly)}
        };
        distanzMatrix = new double[][]{
                {+1, +1, +1, +1},
                {-1, +1, +1, -1},
                {(double) +1 / (lx + ly),
                        (double) -1 / (lx + ly),
                        (double) +1 / (lx + ly),
                        (double) -1 / (lx + ly)}
        };
    }

    @Override
    public void setGeschwindigkeit(Geschwindigkeit geschwindigkeit) {
        super.setGeschwindigkeit(geschwindigkeit);

        double EINS_DURCH_R = 1 / (RAD_DURCHMESSER / 2);

        // Berechnung basierend auf der obigen Matrix
        for (int i = 0; i < 4; i++) {
            radGeschwindigkeiten[i] = EINS_DURCH_R * (
                    geschwindigkeitsMatrix[i][0] * geschwindigkeit.getVX() +
                            geschwindigkeitsMatrix[i][1] * geschwindigkeit.getVY() +
                            geschwindigkeitsMatrix[i][2] * geschwindigkeit.getVZ()
            );
        }

        // Normalisierung der Werte [-1.0; 1.0]
        double vm = Math.max(Math.max(Math.abs(radGeschwindigkeiten[0]), Math.abs(radGeschwindigkeiten[1])), Math.max(Math.abs(radGeschwindigkeiten[2]), Math.abs(radGeschwindigkeiten[3])));

        radGeschwindigkeiten[0] *= geschwindigkeit.getBetrag() / vm;
        radGeschwindigkeiten[1] *= geschwindigkeit.getBetrag() / vm;
        radGeschwindigkeiten[2] *= geschwindigkeit.getBetrag() / vm;
        radGeschwindigkeiten[3] *= geschwindigkeit.getBetrag() / vm;
    }

    @Override
    public void schritt() {
        super.schritt();

        double dx = 0;
        double dy = 0;

        // Berechne Schritte
        for (int i = 0; i < 4; i++) {
            dx += distanzMatrix[0][i] * deltaRadMotorSchritte[i];
            dy += distanzMatrix[1][i] * deltaRadMotorSchritte[i];
        }
        dx /= 4;
        dy /= 4;

        // Berechne Umdrehungen
        dx /= this.encoderSchritteProUmdrehung;
        dy /= this.encoderSchritteProUmdrehung;

        // Berechne Distanz
        dx *= 2 * Math.PI * (RAD_DURCHMESSER / 2);
        dy *= 2 * Math.PI * (RAD_DURCHMESSER / 2);

        gefahreneDistanz.set(dx, dy);
    }
}
