package org.contrived.unwpi.emulations.encoder;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;

/**
 * Real implenentation of {@link ContrivedEncoder}
 */
public class RealEncoder extends ContrivedEncoder {
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

    public void stop() {
        mEncoder.stop();
    }

    public void reset() {
        mEncoder.reset();
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

    public void setDistancePerPulse(double distancePerPulse) {
        mEncoder.setDistancePerPulse(distancePerPulse);
    }

    public void setReverseDirection(boolean reverseDirection) {
        mEncoder.setReverseDirection(reverseDirection);
    }
}
