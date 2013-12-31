package com.leighpauls.unwpi.emulations.gyro;

import edu.wpi.first.wpilibj.Gyro;

/**
 * Real instance wrapper for {@link edu.wpi.first.wpilibj.Gyro}
 */
public class RealGyro extends AbstractGyro {
    private final Gyro mGyro;

    public RealGyro(int slot, int channel) {
        super();
        mGyro = new Gyro(slot, channel);
    }

    public void reset() {
        mGyro.reset();
    }

    public double getAngle() {
        return mGyro.getAngle();
    }

    public void setSensitivity(double voltsPerDegreePerSecond) {
        mGyro.setSensitivity(voltsPerDegreePerSecond);
    }
}
