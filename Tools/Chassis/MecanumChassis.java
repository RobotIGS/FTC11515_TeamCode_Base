package org.firstinspires.ftc.teamcode.Tools.Chassis;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

import org.firstinspires.ftc.teamcode.Tools.Datatypes.Velocity;

// basierend auf https://research.ijcaonline.org/volume113/number3/pxc3901586.pdf
public class MecanumChassis extends ChassisBase {
    private final double RAD_DURCHMESSER = 10.4; // Raddurchmesser in Zentimetern
    private final double EINS_DURCH_R = 1 / (RAD_DURCHMESSER / 2);

    private double[][] vorwaertsMatrix;
    private double[][] rueckwaertsMatrix;

    /**
     * Erstellt ein Mecanum-Chassis
     *
     * @param lx der seitliche Abstand zwischen Radmitte und Robotermitte
     * @param ly der vorwärts gerichtete Abstand zwischen Radmitte und Robotermitte
     */
    public MecanumChassis(int lx, int ly, RevHubOrientationOnRobot hubAusrichtung) {
        super(4, hubAusrichtung);

        vorwaertsMatrix = new double[][]{
                {+1, -1, -(lx + ly)},
                {+1, +1, +(lx + ly)},
                {+1, +1, -(lx + ly)},
                {+1, -1, +(lx + ly)}
        };
        rueckwaertsMatrix = new double[][]{
                {+1, +1, +1, +1},
                {-1, +1, +1, -1},
                {(double) -1 / (lx + ly),
                        (double) 1 / (lx + ly),
                        (double) -1 / (lx + ly),
                        (double) 1 / (lx + ly)}
        };
    }

    @Override
    public void setVelocity(Velocity geschwindigkeit) {
        super.setVelocity(geschwindigkeit);

        // Berechnung basierend auf der obigen Matrix
        for (int i = 0; i < 4; i++) {
            radGeschwindigkeiten[i] = EINS_DURCH_R * (
                    vorwaertsMatrix[i][0] * geschwindigkeit.getVX() +
                            vorwaertsMatrix[i][1] * geschwindigkeit.getVY() +
                            vorwaertsMatrix[i][2] * geschwindigkeit.getWZ()
            );
        }

        // Normalisierung der Werte [-1.0; 1.0]
        double vm = Math.max(Math.max(Math.abs(radGeschwindigkeiten[0]), Math.abs(radGeschwindigkeiten[1])), Math.max(Math.abs(radGeschwindigkeiten[2]), Math.abs(radGeschwindigkeiten[3])));

        radGeschwindigkeiten[0] *= geschwindigkeit.getAbsolute() / vm;
        radGeschwindigkeiten[1] *= geschwindigkeit.getAbsolute() / vm;
        radGeschwindigkeiten[2] *= geschwindigkeit.getAbsolute() / vm;
        radGeschwindigkeiten[3] *= geschwindigkeit.getAbsolute() / vm;
    }

    @Override
    public void step() {
        super.step();

        double dx = 0;
        double dy = 0;

        // Berechne Schritte
        for (int i = 0; i < 4; i++) {
            dx += rueckwaertsMatrix[0][i] * deltaRadMotorSchritte[i];
            dy += rueckwaertsMatrix[1][i] * deltaRadMotorSchritte[i];
        }
        dx /= 4;
        dy /= 4;

        // Berechne Umdrehungen
        dx /= this.fahrEncoderSchritteProUmdrehung;
        dy /= this.fahrEncoderSchritteProUmdrehung;

        // Berechne Distanz
        dx *= 2 * Math.PI * (RAD_DURCHMESSER / 2);
        dy *= 2 * Math.PI * (RAD_DURCHMESSER / 2);

        gefahreneDistanz.set(dx, dy);
    }
}