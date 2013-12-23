package com.leighpauls.unwpi.emulations.victor;

import java.util.Arrays;

/**
* Describes the address of a victor
*/
public class VictorAddress {
    private final int mSlot;
    private final int mChannel;
    private final int[] mTupleValue;

    public VictorAddress(int slot, int channel) {
        mSlot = slot;
        mChannel = channel;
        mTupleValue = new int[]{mSlot, mChannel};
    }

    public int hashCode() {
        return Arrays.hashCode(mTupleValue);
    }
    public boolean equals(Object other) {
        if (!(other instanceof VictorAddress)) {
            return false;
        }
        VictorAddress that = (VictorAddress)other;
        return Arrays.equals(that.mTupleValue, mTupleValue);
    }
    public String toString() {
        return mSlot + "," + mChannel;
    }
}
