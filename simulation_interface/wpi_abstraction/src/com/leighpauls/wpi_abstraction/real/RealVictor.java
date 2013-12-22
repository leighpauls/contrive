package com.leighpauls.wpi_abstraction.real;

import com.leighpauls.wpi_abstraction.abstractions.AbstractVictor;
import edu.wpi.first.wpilibj.Victor;

/**
 * Implementation for a real victor
 */
public class RealVictor extends AbstractVictor {
    private final Victor mVictor;

    public RealVictor(int slot, int channel) {
        mVictor = new Victor(slot, channel);
    }

    public void set(double power) {
        mVictor.set(power);
    }

    public double get() {
        return mVictor.get();
    }
}
