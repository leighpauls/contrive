package com.leighpauls.wpiabstraction.abstractions;

import com.leighpauls.wpiabstraction.InjectionController;
import com.leighpauls.wpiabstraction.emulation.EmulationVictor;
import com.leighpauls.wpiabstraction.real.RealVictor;
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
