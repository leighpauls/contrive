package org.contrived.unwpi.emulations.gyro;

/**
 * Implementation of an emulated gyro
 */
public class EmulationGyro {
    private double mAngle;
    private double mSensitivity;

    public EmulationGyro() {
        mAngle = 0;
        mSensitivity = 0;
    }

    public EmulationGyroDelegate getInstance() {
        return new EmulationGyroDelegate();
    }

    public void updateSensor(double voltage, double deltaTime) {
        mAngle += mSensitivity * voltage * deltaTime;
    }

    public class EmulationGyroDelegate extends ContrivedGyro {
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
