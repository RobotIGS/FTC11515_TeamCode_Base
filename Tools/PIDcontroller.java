package org.firstinspires.ftc.teamcode.Tools;

//TODO: Javadoc

public class PIDcontroller {
    private double k_p;
    private double k_i;
    private double k_d;
    private double integral;
    private double last_error;
    private double pid_value;

    public PIDcontroller(double p, double i, double d) {
        this.k_p = p;
        this.k_i = i;
        this.k_d = d;
        this.integral = 0;
        this.last_error = 0;
        this.pid_value = 0;
    }

    private double P(double error) {
        return k_p * error;
    }

    //TODO Check order
    private double I(double error) {
        integral += k_i * error;
        return integral;
    }

    private double D(double error) {
        return k_d * (error - last_error);
    }

    public double step(double error) {
        pid_value = P(error) + I(error) + D(error);
        last_error = error;
        return pid_value;
    }
}


