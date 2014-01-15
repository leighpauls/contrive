/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2012. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import org.contrived.unwpi.emulations.addresses.EncoderAddress;
import org.contrived.unwpi.emulations.encoder.ContrivedEncoder;
import org.contrived.unwpi.simulation.SimulationModel;

/**
 * Contrived's emulation of the WPILibJ encoder
 */
public class Encoder {

    private final ContrivedEncoder.ContrivedEncoderDelegate mDelegate;

    public Encoder(int channelA, int channelB, boolean reverse) {
        EncoderAddress address = new EncoderAddress(channelA, channelB);
        mDelegate = SimulationModel.getInstance().getEncoder(
                address,
                reverse,
                CounterBase.EncodingType.k4X);
    }

    public void start() {
        mDelegate.start();
    }

    public void stop() {
        mDelegate.stop();
    }
    public void reset() {
        mDelegate.reset();
    }
    public boolean getStopped() {
        return mDelegate.getStopped();
    }

    public boolean getDirection() {
        return mDelegate.getDirection();
    }

    public double getDistance() {
        return mDelegate.getDistance();
    }

    public double getRate() {
        return mDelegate.getRate();
    }

    public void setDistancePerPulse(double distancePerPulse) {
        mDelegate.setDistancePerPulse(distancePerPulse);
    }

    public void setReverseDirection(boolean reverseDirection) {
        mDelegate.setReverseDirection(reverseDirection);
    }
}
