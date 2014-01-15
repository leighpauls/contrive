/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2012. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj;

import org.contrived.unwpi.emulations.addresses.PwmAddress;
import org.contrived.unwpi.emulations.victor.ContrivedVictor;
import org.contrived.unwpi.simulation.SimulationModel;

/**
 * Emulation of a VEX Robotics Victor Speed Controller
 */
public class Victor {
    private final ContrivedVictor.ContrivedVictorDelegate mDelegage;

    public Victor(int channel) {
        PwmAddress address = new PwmAddress(channel);
        mDelegage = SimulationModel.getInstance().getVictor(address);
    }

    public Victor(int slot, int channel) {
        PwmAddress address = new PwmAddress(slot, channel);
        mDelegage = SimulationModel.getInstance().getVictor(address);
    }

    public void set(double power) {
        mDelegage.set(power);
    }

    public double get() {
        return mDelegage.get();
    }
}
