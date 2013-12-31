package com.leighpauls.unwpi.emulations.victor;

import com.leighpauls.unwpi.simulation.SimulationServer;

/**
 * Implementation of an emulated victor
 */
public class EmulationVictor {
    private final SimulationServer mSimulationServer;
    private final int mSlot;
    private final int mChannel;

    public EmulationVictor(SimulationServer simulationServer, int slot, int channel) {
        mSimulationServer = simulationServer;
        mSlot = slot;
        mChannel = channel;
    }

    public EmulationVictorDelegate getInstance() {
        return new EmulationVictorDelegate();
    }

    public class EmulationVictorDelegate extends AbstractVictor {
        private double mPower;

        private EmulationVictorDelegate() {
            mPower = 0.0;
        }

        public void set(double power) {
            mPower = Math.max(-1, Math.min(1, power));
            mSimulationServer.sendActuatorCommand(
                    new MotorControllerCommand(mSlot, mChannel, mPower));
        }

        public double get() {
            return mPower;
        }
    }
}
