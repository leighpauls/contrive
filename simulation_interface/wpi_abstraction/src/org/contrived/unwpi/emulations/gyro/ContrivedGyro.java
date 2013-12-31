package org.contrived.unwpi.emulations.gyro;

import org.contrived.unwpi.InjectionController;
import org.contrived.unwpi.emulations.addresses.AnalogAddress;
import org.contrived.unwpi.simulation.SimulationModel;
import edu.wpi.first.wpilibj.SensorBase;

/**
 * Abstraction layer ofr {@link edu.wpi.first.wpilibj.Gyro}
 */
public abstract class ContrivedGyro {

    public static ContrivedGyro getInstance(int slot, int channel) {
        if (InjectionController.isEmulation()) {
            return SimulationModel.getInstance().getGyro(new AnalogAddress(slot, channel));
        } else {
            return new RealGyro(slot, channel);
        }
    }

    public static ContrivedGyro getInstance(int channel) {
        return getInstance(SensorBase.getDefaultAnalogModule(), channel);
    }

    public abstract void reset();
    public abstract double getAngle();
    public abstract void setSensitivity(double voltsPerDegreePerSecond);

}
