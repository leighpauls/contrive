/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2012. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import org.contrived.unwpi.emulations.addresses.AnalogAddress;
import org.contrived.unwpi.emulations.gyro.ContrivedGyro;
import org.contrived.unwpi.simulation.SimulationModel;

/**
 * Shim object between a contrived gyro and a wpi gyro
 */
public class Gyro {
    private final ContrivedGyro.ContrivedGyroDelegate mContrivedGyro;

    public Gyro(int channel) {
        AnalogAddress address = new AnalogAddress(channel);
        mContrivedGyro = SimulationModel.getInstance().getGyro(address);
    }

    public Gyro(int module, int channel) {
        AnalogAddress address = new AnalogAddress(module, channel);
        mContrivedGyro = SimulationModel.getInstance().getGyro(address);
    }

    public void setSensitivity(double voltSecondsPerDegree) {
        mContrivedGyro.setSensitivity(voltSecondsPerDegree);
    }

    public double getAngle() {
        return mContrivedGyro.getAngle();
    }

    public void reset() {
        mContrivedGyro.reset();
    }
}
