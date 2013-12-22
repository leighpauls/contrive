package com.leighpauls.wpi_abstraction.real;

import com.leighpauls.wpi_abstraction.abstractions.AbstractEncoder;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;

/**
 * Real implenentation of {@link AbstractEncoder}
 */
public class RealEncoder extends AbstractEncoder {
    private final Encoder mEncoder;

    public RealEncoder(
            int aSlot,
            int aChannel,
            int bSlot,
            int bChannel,
            boolean reverseDirection,
            CounterBase.EncodingType encodingType) {
        super();
        mEncoder = new Encoder(aSlot, aChannel, bSlot, bChannel, reverseDirection, encodingType);
    }

    public RealEncoder(
            int aSlot,
            int aChannel,
            int bSlot,
            int bChannel,
            int indexSlot,
            int indexChannel,
            boolean reverseDirection) {
        mEncoder = new Encoder(
                aSlot,
                aChannel,
                bSlot,
                bChannel,
                indexSlot,
                indexChannel,
                reverseDirection);
    }

    public void start() {
        mEncoder.start();
    }

    public int getRaw() {
        return mEncoder.getRaw();
    }

    public int get() {
        return mEncoder.get();
    }

    public void reset() {
        mEncoder.reset();
    }

    public double getPeriod() {
        return mEncoder.getPeriod();
    }

    public void setMaxPeriod(double maxPeriod) {
        mEncoder.setMaxPeriod(maxPeriod);
    }

    public boolean getStopped() {
        return mEncoder.getStopped();
    }

    public boolean getDirection() {
        return mEncoder.getDirection();
    }

    public double getDistance() {
        return mEncoder.getDistance();
    }

    public double getRate() {
        return mEncoder.getRate();
    }

    public void setMinRate(double minRate) {
        mEncoder.setMinRate(minRate);
    }

    public void setDistancePerPulse(double distancePerPulse) {
        mEncoder.setDistancePerPulse(distancePerPulse);
    }

    public void setReverseDirection(boolean reverseDirection) {
        mEncoder.setReverseDirection(reverseDirection);
    }
}
