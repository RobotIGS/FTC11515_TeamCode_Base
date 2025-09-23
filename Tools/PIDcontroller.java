package org.firstinspires.ftc.teamcode.Tools;

public class PIDcontroller {
    double k_p;
    double k_i;
    double k_d;
    double integral;
    double last_error;
    double pid_value;

    /**
     * create a PID controller (see WIKIPEDIA PID controller)
     *
     * @param p proportional factor
     * @param i integral factor
     * @param d derivative factor
     */
    public PIDcontroller(double p, double i, double d) {
        this.k_p = p;
        this.k_i = i;
        this.k_d = d;
        this.integral = 0;
        this.last_error = 0;
        this.pid_value = 0;
    }

    /**
     * calculate the proportional part
     *
     * @param error the error
     * @return proportional part
     */
    private double P(double error) {
        return k_p * error;
    }

    /**
     * calculate and integrate the integral part
     *
     * @param error the error
     * @return integral part
     */
    private double I(double error) {
        integral += k_i * error;
        return integral;
    }

    /**
     * calculate the derivative part
     *
     * @param error the error
     * @return derivative part
     */
    private double D(double error) {
        return k_d * (error - last_error);
    }

    /**
     * reset PID controller
     */
    public void reset() {
        integral = 0.0;
        last_error = 0.0;
        pid_value = 0.0;
    }

    /**
     * update the PID controller and calculate the result
     *
     * @param error the system error
     * @return output of the PID controller
     */
    public double step(double error) {
        last_error = error; // remember the last error for later use
        pid_value = P(error) + I(error) + D(error); // sum up the three parts
        return Math.max(-1.0, Math.min(1.0, pid_value));
    }
}


