package org.contrived.unwpi.emulations.gyro;

/**
 * Implementation of an emulated gyro
 */
public class ContrivedGyro {
    private double mAngle;
    private double mSensitivity;

    public ContrivedGyro() {
        mAngle = 0;
        mSensitivity = 0.007;
    }

    
    public void updateSensor(double voltage, double deltaTime) {
        mAngle += voltage * deltaTime / mSensitivity;
    }

    public ContrivedGyroDelegate getInstance() {
        return new ContrivedGyroDelegate();
    }

    public class ContrivedGyroDelegate {
        public void reset() {
            mAngle = 0;
        }

        public double getAngle() {
            return mAngle;
        }

        public void setSensitivity(double voltsPerDegreePerSecond) {
            mSensitivity = voltsPerDegreePerSecond;
        }
    }
}
