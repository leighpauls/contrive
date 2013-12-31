package com.leighpauls.unwpi.emulations.victor;

import com.leighpauls.unwpi.simulation.ActuatorCommand;
import com.leighpauls.unwpi.simulation.SimulationServer;

/**
 * Implementation of an emulated victor
 */
public class EmulationVictor {
    private final SimulationServer mSimulationServer;
    private final int mSlot;
    private final int mChannel;
    private double mPower;

    public EmulationVictor(SimulationServer simulationServer, int slot, int channel) {
        mSimulationServer = simulationServer;
        mSlot = slot;
        mChannel = channel;
        mPower = 0.0;
    }

    public EmulationVictorDelegate getInstance() {
        return new EmulationVictorDelegate();
    }

    public ActuatorCommand getActuatorCommand() {
        return new MotorControllerCommand(mSlot, mChannel, mPower);
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
