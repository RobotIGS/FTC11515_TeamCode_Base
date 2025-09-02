package org.firstinspires.ftc.teamcode.Tools;

public class PIDcontroller {
    private final double k_p;
    private final double k_i;
    private final double k_d;
    private double integral;
    private double last_error;
    double pid_value;

    /**
     * create a PID controller (see WIKIPEDIA PID controller)
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
     * @param error the error
     * @return proportional part
     */
    private double P(double error) {
        return k_p * error;
    }

    /**
     * calculate and integrate the integral part
     * @param error the error
     * @return integral part
     */
    private double I(double error) {
        integral += k_i * error;
        return integral;
    }

    /**
     * calculate the derivative part
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
     * @param error the system error
     * @return output of the PID controller
     */
    public double step(double error) {
        pid_value = P(error) + I(error) + D(error); // sum up the three parts
        last_error = error; // remember the last error for later use
        /*if (Math.abs(pid_value) > 1) {
            pid_value = Math.max(Math.min(pid_value, 1), -1);
            integral = 1;
        }*/
        return pid_value;
    }
}


