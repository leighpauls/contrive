package com.leighpauls.unwpi.emulations.victor;

import com.leighpauls.unwpi.emulations.addresses.PwmAddress;
import com.leighpauls.unwpi.simulation.ActuatorCommand;

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

    public class EmulationVictorDelegate extends AbstractVictor {
        public void set(double power) {
            mPower = Math.max(-1, Math.min(1, power));
        }

        public double get() {
            return mPower;
        }
    }
}
