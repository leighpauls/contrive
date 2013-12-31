package org.contrived.unwpi.emulations.victor;

import org.contrived.unwpi.InjectionController;
import org.contrived.unwpi.emulations.addresses.PwmAddress;
import org.contrived.unwpi.simulation.SimulationModel;
import edu.wpi.first.wpilibj.SensorBase;

/**
 * Abstraction layer for {@link edu.wpi.first.wpilibj.Victor}
 */
public abstract class ContrivedVictor {
    public static ContrivedVictor getInstance(final int channel) {
        return getInstance(SensorBase.getDefaultDigitalModule(), channel);
    }
    public static ContrivedVictor getInstance(int slot, int channel) {
        if (InjectionController.isEmulation()) {
            return SimulationModel.getInstance().getVictor(new PwmAddress(slot, channel));
        } else {
            return new RealVictor(slot, channel);
        }
    }

    abstract public void set(double power);
    abstract public double get();
    // TODO: pidWrite
}
