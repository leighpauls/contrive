package com.leighpauls.unwpi.emulations.encoder;

import java.util.Arrays;

/**
* Describes the location of an Encoder
*/
public class EncoderAddress {
    private final int mASlot, mAChannel, mBSlot, mBChannel;
    private final int[] mTupleValue;

    public EncoderAddress(int aSlot, int aChannel, int bSlot, int bChannel) {
        mASlot = aSlot;
        mAChannel = aChannel;
        mBSlot = bSlot;
        mBChannel = bChannel;
        mTupleValue = new int[]{mASlot, mAChannel, mBSlot, mBChannel};
    }

    public int hashCode() {
        return Arrays.hashCode(mTupleValue);
    }
    public boolean equals(Object other) {
        if (!(other instanceof EncoderAddress)) {
            return false;
        }
        EncoderAddress that = (EncoderAddress)other;
        return Arrays.equals(that.mTupleValue, mTupleValue);
    }
    public String toString() {
        return mASlot + "," + mBSlot + ":" + mBSlot + "," + mBChannel;
    }
}
