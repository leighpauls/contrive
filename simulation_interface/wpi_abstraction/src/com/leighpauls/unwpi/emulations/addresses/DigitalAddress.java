package com.leighpauls.unwpi.emulations.addresses;

/**
 * Address of a digital I/O pin
 */
public class DigitalAddress extends AbstractPairAddress {
    public DigitalAddress(int slot, int channel) {
        super(slot, channel);
    }
}
