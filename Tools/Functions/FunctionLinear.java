package org.firstinspires.ftc.teamcode.Tools.Functions;

public class FunctionLinear extends FunctionBase {
    private double a;
    private double b;
    private boolean acce;

    /**
     * f(x) = a*x + b
     * @param a slope of the function
     * @param b starting value
     * @param acce acceleration / deceleration
     */

    public FunctionLinear(double a, double b, boolean acce) {
        this.a = a;
        this.b = b;
        this.acce = acce;
    }

    public double apply(double x)
    {
        return (acce ? 1: -1) * a*x + b;
    }
}
	
