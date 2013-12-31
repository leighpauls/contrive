package com.leighpauls.unwpi.emulations.victor;

import com.leighpauls.unwpi.InjectionController;
import com.leighpauls.unwpi.emulations.addresses.PwmAddress;
import com.leighpauls.unwpi.simulation.SimulationModel;
import edu.wpi.first.wpilibj.SensorBase;

/**
 * Abstraction layer for {@link edu.wpi.first.wpilibj.Victor}
 */
public abstract class AbstractVictor {
    public static AbstractVictor getInstance(final int channel) {
        return getInstance(SensorBase.getDefaultDigitalModule(), channel);
    }
    public static AbstractVictor getInstance(int slot, int channel) {
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
