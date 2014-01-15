package org.contrived.unwpi.emulations.victor;

import org.contrived.unwpi.emulations.addresses.PwmAddress;
import org.contrived.unwpi.simulation.ActuatorCommand;

/**
 * Implementation of an emulated victor
 */
public class ContrivedVictor {
    private final PwmAddress mAddress;
    private double mPower;

    public ContrivedVictor(PwmAddress address) {
        mAddress = address;
        mPower = 0.0;
    }

    public ContrivedVictorDelegate getInstance() {
        return new ContrivedVictorDelegate();
    }

    public ActuatorCommand getActuatorCommand() {
        return new MotorControllerCommand(mAddress.getSlot(), mAddress.getChannel(), mPower);
    }

    public class ContrivedVictorDelegate {
        public void set(double power) {
            mPower = Math.max(-1, Math.min(1, power));
        }

        public double get() {
            return mPower;
        }
    }
}
