package org.firstinspires.ftc.teamcode.tools.datentypen;

public class Rotation {
    private double rotation;

    // [-180; 180]
    // POSITIVE ROTATION: nach links
    // NEGATIVE ROTATION: nach rechts

    public Rotation(double rotation) {
        this.rotation = rotation;
    }

    public double get() {
        return rotation;
    }

    public void set(double rotation) {
        this.rotation = rotation;
        normalisieren();
    }

    // make sure that the rotation is in  [-180;180]
    public void normalisieren() {
        this.rotation = ((this.rotation + 180) % 360);
        if (this.rotation < 0) {
            this.rotation += 360;
        }
        this.rotation -= 180;
    }

    public void addieren(double rotation) {
        this.rotation += rotation; // add delta rotation
        normalisieren();
    }
}
