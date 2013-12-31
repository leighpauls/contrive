package org.contrived.unwpi.emulations.victor;

import edu.wpi.first.wpilibj.Victor;

/**
 * Implementation for a real victor
 */
public class RealVictor extends ContrivedVictor {
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
