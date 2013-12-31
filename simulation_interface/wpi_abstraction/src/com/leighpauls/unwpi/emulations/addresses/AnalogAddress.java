package com.leighpauls.unwpi.emulations.addresses;

/**
 * Describes the address of an analog pin
 */
public class AnalogAddress extends AbstractPairAddress {
    public AnalogAddress(int slot, int channel) {
        super(slot, channel);
    }
}
