package com.leighpauls.wpiabstraction.emulation;

import com.leighpauls.wpiabstraction.abstractions.AbstractEncoder;
import edu.wpi.first.wpilibj.CounterBase;

/**
 * Implementation of an emulated encoder
 */
public class EmulationEncoder extends AbstractEncoder {

    private final int mASlot;
    private final int mAChannel;
    private final int mBSlot;
    private final int mBChannel;
    private final boolean mReverseDirection;
    private final CounterBase.EncodingType mEncodingType;

    public EmulationEncoder(
            int aSlot,
            int aChannel,
            int bSlot,
            int bChannel,
            boolean reverseDirection,
            CounterBase.EncodingType encodingType) {
        mASlot = aSlot;
        mAChannel = aChannel;
        mBSlot = bSlot;
        mBChannel = bChannel;
        mReverseDirection = reverseDirection;
        mEncodingType = encodingType;
    }

    public void start() {
        System.out.println(
                "Start Encoder " + mASlot + "," + mAChannel + ":" + mBSlot + "," + mBChannel);
    }

    public int getRaw() {
        return 0;
    }

    public int get() {
        return 0;
    }

    public void reset() {
        System.out.println(
                "Reset Encoder " + mASlot + "," + mAChannel + ":" + mBSlot + "," + mBChannel);
    }

    public double getPeriod() {
        return 0;
    }

    public void setMaxPeriod(double maxPeriod) {
        System.out.println(
                "Set Max Period of Encoder "
                        + mASlot + "," + mAChannel + ":" + mBSlot + "," + mBChannel
                        + " to " + maxPeriod);
    }

    public boolean getStopped() {
        return false;
    }

    public boolean getDirection() {
        return false;
    }

    public double getDistance() {
        return 0;
    }

    public double getRate() {
        return 0;
    }

    public void setMinRate(double minRate) {
        System.out.println(
                "Set Min Rate of Encoder "
                        + mASlot + "," + mAChannel + ":" + mBSlot + "," + mBChannel
                        + " to " + minRate);
    }

    public void setDistancePerPulse(double distancePerPulse) {
        System.out.println(
                "Dist Per Pulse of Encoder "
                        + mASlot + "," + mAChannel + ":" + mBSlot + "," + mBChannel
                        + " to " + distancePerPulse);
    }

    public void setReverseDirection(boolean reverseDirection) {
        System.out.println(
                "Reverse Encoder "
                        + mASlot + "," + mAChannel + ":" + mBSlot + "," + mBChannel
                        + " to " + reverseDirection);
    }
}
