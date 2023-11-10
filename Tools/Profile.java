package org.firstinspires.ftc.teamcode.Tools;

import org.firstinspires.ftc.teamcode.Tools.Functions.FunctionBase;

// TODO: check if "System.currentTimeMillis" works

public class Profile {
    private boolean acceleration;
    private FunctionBase accelerationFunction;
    private FunctionBase decelerationFunction;
    private long start_time;

    /**
     * Profile used to accelerate and decelerate based on a function
     * @param acceFunc function used for acceleration
     * @param decelFunc function used for deceleration
     */
    public Profile(FunctionBase acceFunc, FunctionBase decelFunc) {
        acceleration = true;
        accelerationFunction = acceFunc;
        decelerationFunction = decelFunc;
        start_time = System.currentTimeMillis();
    }

    /**
     * set if acceleration or deceleration is used and reset start time
     * @param acce if acceleration is used
     */
    public void setDirection(boolean acce) {
        acceleration = acce;
        resetStarTime();
    }

    /**
     * set acceleration function
     * @param acceFunction acceleration function
     */
    public void setAccelerationFunction(FunctionBase acceFunction) {
        this.accelerationFunction = acceFunction;
    }

    /**
     * set deceleration function
     * @param decelFunction deceleration function
     */
    public void setDecelerationFunction(FunctionBase decelFunction) {
        this.decelerationFunction = decelFunction;
    }

    /**
     * reset start time
     */
    public void resetStarTime() {
        start_time = System.currentTimeMillis();
    }

    /**
     * apply functions on lap time in mills and return their values
     * @return some factor
     */
    public double step() {
        long t = System.currentTimeMillis() - start_time;
        if (t >= 0)
            return accelerationFunction.apply(t);
        return decelerationFunction.apply(t);
    }
}
