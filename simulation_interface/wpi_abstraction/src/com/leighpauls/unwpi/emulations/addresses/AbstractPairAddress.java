package com.leighpauls.unwpi.emulations.addresses;

import java.util.Arrays;

/**
 * Abstract address type for slot-channel addresses
 */
public class AbstractPairAddress {
    private final int mSlot;
    private final int mChannel;
    private final int[] mTupleValue;

    public AbstractPairAddress(int slot, int channel) {
        mSlot = slot;
        mChannel = channel;
        mTupleValue = new int[]{ mSlot, mChannel };
    }

    public int getSlot() {
        return mSlot;
    }
    public int getChannel() {
        return  mChannel;
    }

    public int hashCode() {
        return Arrays.hashCode(mTupleValue);
    }
    public boolean equals(Object other) {
        if (this.getClass() != other.getClass()) {
            return false;
        }
        AbstractPairAddress that = (AbstractPairAddress)other;
        return Arrays.equals(that.mTupleValue, mTupleValue);
    }
    public String toString() {
        return mSlot + "," + mChannel;
    }
}
