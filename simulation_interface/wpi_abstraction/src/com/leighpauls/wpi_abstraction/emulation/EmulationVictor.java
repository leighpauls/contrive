package com.leighpauls.wpi_abstraction.emulation;

import com.leighpauls.wpi_abstraction.abstractions.AbstractVictor;

/**
 * Implementation of an emulated victor
 */
public class EmulationVictor extends AbstractVictor {
    private final int mSlot;
    private final int mChannel;

    public EmulationVictor(int slot, int channel) {
        mSlot = slot;
        mChannel = channel;
    }

    public void set(double power) {
        System.out.println("Victor " + mSlot + ", " + mChannel + " to power: " + power);
    }

    public double get() {
        return 0;
    }
}
