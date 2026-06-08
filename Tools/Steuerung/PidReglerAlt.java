package org.firstinspires.ftc.teamcode.Tools.Steuerung;

public class PidReglerAlt {
    public double integral;
    public double last_error;
    public double pid_value;
    public double k_p;
    public double k_i;
    public double k_d;
    private long last_time;

    /**
     * create a PID controller
     *
     * @param p proportional factor
     * @param i integral factor
     * @param d derivative factor
     */
    public PidReglerAlt(double p, double i, double d) {
        this.k_p = p;
        this.k_i = i;
        this.k_d = d;
        this.integral = 0;
        this.last_error = 0;
        this.pid_value = 0;
        this.last_time = 0;
    }

    public void change_values(double p, double i, double d) {
        this.k_p = p;
        this.k_i = i;
        this.k_d = d;
    }

    /**
     * reset PID controller
     */
    public void reset() {
        integral = 0.0;
        last_error = 0.0;
        pid_value = 0.0;
        last_time = 0;
    }

    /**
     * update the PID controller and calculate the result
     *
     * @param error the system error
     * @return output of the PID controller
     */
    public double step(double error) {
        error /= 180;
        long current_time = System.currentTimeMillis();
        double dt = (last_time == 0) ? 0 : (current_time - last_time) / 1000.0;
        last_time = current_time;

        // P part
        double p_part = k_p * error;

        // I part (with dt)
        integral += k_i * error * dt;
        integral = Math.max(-1.0, Math.min(1.0, integral)); // Anti-Windup

        // D part (with dt)
        double d_part = 0;
        if (dt > 0) {
            d_part = k_d * (error - last_error) / dt;
        }

        pid_value = p_part + integral + d_part;
        last_error = error; // remember the last error for later use
        return Math.max(-1.0, Math.min(1.0, pid_value));
    }
}


