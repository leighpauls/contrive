package org.contrived.unwpi.emulations.addresses;

import java.util.Arrays;

/**
* Describes the location of an Encoder
*/
public class EncoderAddress {
    private final DigitalAddress mAAddress, mBAddress;

    public EncoderAddress(int aSlot, int aChannel, int bSlot, int bChannel) {
        mAAddress = new DigitalAddress(aSlot, aChannel);
        mBAddress = new DigitalAddress(bSlot, bChannel);
    }

    public EncoderAddress(int aChannel, int bChannel) {
        this(1, aChannel, 1, bChannel);
    }

    public int hashCode() {
        return Arrays.hashCode(new int[] { mAAddress.hashCode(), mBAddress.hashCode() });
    }
    public boolean equals(Object other) {
        if (this.getClass() != other.getClass()) {
            return false;
        }
        EncoderAddress that = (EncoderAddress)other;
        return mAAddress.equals(that.mAAddress) && mBAddress.equals(that.mBAddress);
    }
    public String toString() {
        return mAAddress.toString() + ":" + mBAddress.toString();
    }
}
