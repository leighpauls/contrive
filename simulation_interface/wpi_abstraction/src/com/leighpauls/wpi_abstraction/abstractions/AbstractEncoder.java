package com.leighpauls.wpi_abstraction.abstractions;

import com.leighpauls.wpi_abstraction.InjectionController;
import com.leighpauls.wpi_abstraction.emulation.EmulationEncoder;
import com.leighpauls.wpi_abstraction.real.RealEncoder;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.SensorBase;

/**
 * Absraction layer for {@link edu.wpi.first.wpilibj.Encoder}
 */
public abstract class AbstractEncoder {
    public static AbstractEncoder getInstance(
            int aSlot,
            int aChannel,
            int bSlot,
            int bChannel,
            boolean reverseDirection) {
        return getInstance(
                aSlot,
                aChannel,
                bSlot,
                bChannel,
                reverseDirection,
                CounterBase.EncodingType.k4X);
    }

    public static AbstractEncoder getInstance(int aSlot, int aChannel, int bSlot, int bChannel) {
        return getInstance(aSlot, aChannel, bSlot, bChannel, false);
    }

    public static AbstractEncoder getInstance(
            int aSlot,
            int aChannel,
            int bSlot,
            int bChannel,
            boolean reverseDirection,
            CounterBase.EncodingType encodingType) {
        if (InjectionController.isEmulation()) {
            return new EmulationEncoder(aSlot, aChannel, bSlot, bChannel, reverseDirection, encodingType);
        } else {
            return new RealEncoder(aSlot, aChannel, bSlot, bChannel, reverseDirection, encodingType);
        }

    }


    public static AbstractEncoder getInstance(int aChannel, int bChannel, boolean reverseDirection) {
        return getInstance(
                SensorBase.getDefaultDigitalModule(),
                aChannel,
                SensorBase.getDefaultDigitalModule(),
                bChannel,
                reverseDirection);
    }

    public static AbstractEncoder getInstance(int aChannel, int bChannel) {
        return getInstance(aChannel, bChannel, false);
    }

    // TODO: the rest of these stupid constructors...


    public abstract void start();
    public abstract int getRaw();
    public abstract int get();
    public abstract void reset();
    public abstract double getPeriod();
    public abstract void setMaxPeriod(double maxPeriod);
    public abstract boolean getStopped();
    public abstract boolean getDirection();
    public abstract double getDistance();
    public abstract double getRate();
    public abstract void setMinRate(double minRate);
    public abstract void setDistancePerPulse(double distancePerPulse);
    public abstract void setReverseDirection(boolean reverseDirection);

    // TODO: stupid built-in PID things

}
