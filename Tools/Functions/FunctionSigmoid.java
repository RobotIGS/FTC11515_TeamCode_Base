package org.firstinspires.ftc.teamcode.Tools.Functions;

public class FunctionSigmoid extends FunctionBase {
    private double r;
    private double n;
    private double a;
    private double k;
    private boolean acce;
    /**
     * f(x) = r - (e^(-(x-n)/a))^k
     * @param a shift of turning point and increase of slope
     * @param r shift on y-axis (1<=r<=2), ideal r = 1
     * @param n shift on x-axis
     * @param k change of slope and turning point stays the same
     * @param acce acceleration / deceleration
     */
    public FunctionSigmoid(double a, double r, double n, double k, boolean acce) {
        this.r = Math.min(2, Math.max(1,r));
        this.n = n;
        this.a = a > 0 ? a : 1;
        this.k = k > 2 ? k : 2;
        this.acce = acce;
    }

    public double apply(double x) {
        return (acce ? 1 : -1) * (r - Math.exp(Math.pow(-((x-n)/a),k))) + (acce ? 0 : 1);
    };
}