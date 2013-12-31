package com.leighpauls.unwpi.emulations.gyro;

import com.leighpauls.unwpi.InjectionController;
import com.leighpauls.unwpi.emulations.addresses.AnalogAddress;
import com.leighpauls.unwpi.simulation.SimulationModel;
import edu.wpi.first.wpilibj.SensorBase;

/**
 * Abstraction layer ofr {@link edu.wpi.first.wpilibj.Gyro}
 */
public abstract class AbstractGyro {

    public static AbstractGyro getInstance(int slot, int channel) {
        if (InjectionController.isEmulation()) {
            return SimulationModel.getInstance().getGyro(new AnalogAddress(slot, channel));
        } else {
            return new RealGyro(slot, channel);
        }
    }

    public static AbstractGyro getInstance(int channel) {
        return getInstance(SensorBase.getDefaultAnalogModule(), channel);
    }

    public abstract void reset();
    public abstract double getAngle();
    public abstract void setSensitivity(double voltsPerDegreePerSecond);

}
