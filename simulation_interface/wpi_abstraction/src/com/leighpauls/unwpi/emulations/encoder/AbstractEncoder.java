package com.leighpauls.unwpi.emulations.encoder;

import com.leighpauls.unwpi.InjectionController;
import com.leighpauls.unwpi.simulation.SimulationModel;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.SensorBase;

/**
 * Abstraction layer for {@link edu.wpi.first.wpilibj.Encoder}
 */
public abstract class AbstractEncoder {
    public static AbstractEncoder getInstance(
            int aSlot,
            int aChannel,
            int bSlot,
            int bChannel,
            boolean reverseDirection,
            CounterBase.EncodingType encodingType) {
        if (InjectionController.isEmulation()) {
            return SimulationModel.getInstance().getEncoder(
                    aSlot,
                    aChannel,
                    bSlot,
                    bChannel,
                    reverseDirection,
                    encodingType);
        } else {
            return new RealEncoder(aSlot, aChannel, bSlot, bChannel, reverseDirection, encodingType);
        }

    }

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
    public abstract void stop();
    public abstract void reset();
    public abstract boolean getStopped();
    public abstract boolean getDirection();
    public abstract double getDistance();
    public abstract double getRate();
    public abstract void setDistancePerPulse(double distancePerPulse);
    public abstract void setReverseDirection(boolean reverseDirection);

    // TODO: stupid built-in PID things

}
