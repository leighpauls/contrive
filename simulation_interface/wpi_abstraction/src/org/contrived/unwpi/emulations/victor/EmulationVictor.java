package org.contrived.unwpi.emulations.victor;

import org.contrived.unwpi.emulations.addresses.PwmAddress;
import org.contrived.unwpi.simulation.ActuatorCommand;

/**
 * Implementation of an emulated victor
 */
public class EmulationVictor {
    private final PwmAddress mAddress;
    private double mPower;

    public EmulationVictor(PwmAddress address) {
        mAddress = address;
        mPower = 0.0;
    }

    public EmulationVictorDelegate getInstance() {
        return new EmulationVictorDelegate();
    }

    public ActuatorCommand getActuatorCommand() {
        return new MotorControllerCommand(mAddress.getSlot(), mAddress.getChannel(), mPower);
    }

    public class EmulationVictorDelegate extends ContrivedVictor {
        public void set(double power) {
            mPower = Math.max(-1, Math.min(1, power));
        }

        public double get() {
            return mPower;
        }
    }
}
