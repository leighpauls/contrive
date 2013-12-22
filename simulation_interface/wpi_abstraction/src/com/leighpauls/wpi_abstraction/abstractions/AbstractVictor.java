package com.leighpauls.wpi_abstraction.abstractions;

import com.leighpauls.wpi_abstraction.InjectionController;
import com.leighpauls.wpi_abstraction.emulation.EmulationVictor;
import com.leighpauls.wpi_abstraction.real.RealVictor;
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
            return new EmulationVictor(slot, channel);
        } else {
            return new RealVictor(slot, channel);
        }
    }


    abstract public void set(double power);
    abstract public double get();
    // TODO: pidWrite
}
