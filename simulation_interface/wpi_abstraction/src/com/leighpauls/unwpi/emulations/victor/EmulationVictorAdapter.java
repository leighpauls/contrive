package com.leighpauls.unwpi.emulations.victor;

import com.leighpauls.unwpi.simulation.SimulationServer;

/**
 * Implementation of an emulated victor
 */
public class EmulationVictorAdapter {
    private final SimulationServer mSimulationServer;
    private final int mSlot;
    private final int mChannel;

    public EmulationVictorAdapter(SimulationServer simulationServer, int slot, int channel) {
        mSimulationServer = simulationServer;
        mSlot = slot;
        mChannel = channel;
    }

    public EmulationVictor getInstance() {
        return new EmulationVictor();
    }

    public class EmulationVictor extends AbstractVictor {
        private double mPower;

        private EmulationVictor() {
            mPower = 0.0;
        }

        public void set(double power) {
            mPower = power;
            mSimulationServer.sendActuatorCommand(
                    new MotorControllerCommand(mSlot, mChannel, power));
        }

        public double get() {
            return mPower;
        }
    }
}
