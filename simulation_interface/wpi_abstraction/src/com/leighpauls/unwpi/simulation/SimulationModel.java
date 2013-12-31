package com.leighpauls.unwpi.simulation;

import com.leighpauls.unwpi.emulations.encoder.EmulationEncoder;
import com.leighpauls.unwpi.emulations.encoder.EncoderAddress;
import com.leighpauls.unwpi.emulations.victor.EmulationVictor;
import com.leighpauls.unwpi.emulations.victor.VictorAddress;
import com.leighpauls.unwpi.emulations.encoder.EncoderCommandHandler;
import edu.wpi.first.wpilibj.CounterBase;

import java.util.HashMap;

/**
 * Interface used to connect to the physical simulation
 */
public class SimulationModel {
    private static SimulationModel sInstance;
    public static SimulationModel getInstance() {
        if (sInstance == null) {
            sInstance = new SimulationModel();
        }
        return sInstance;
    }

    public static SimulationServer connectToSimulationServer() {
        // force an instance to be made
        SimulationModel model = getInstance();
        // return it's server connection
        return model.mSimulationServer;
    }

    private final SimulationServer mSimulationServer;
    private final HashMap mVictors;
    private final HashMap mEncoders;

    private SimulationModel() {
        ModelDelegate delegateInstance = new ModelDelegate();
        SensorCommandHandler[] handlers = new SensorCommandHandler[] {
                new EncoderCommandHandler(delegateInstance)
        };

        mSimulationServer = new SimulationServer(handlers);

        mVictors = new HashMap();
        mEncoders = new HashMap();

        addVictor(1, 1);
        addVictor(1, 2);

        addEncoder(1, 1, 1, 2);
        addEncoder(1, 3, 1, 4);
    }

    public class ModelDelegate {
        public EmulationVictor getVictor(VictorAddress address) {
            return (EmulationVictor) mVictors.get(address);
        }
        public EmulationEncoder getEncoder(EncoderAddress address) {
            return (EmulationEncoder) mEncoders.get(address);
        }
    }

    private void addVictor(int slot, int channel) {
        VictorAddress address = new VictorAddress(slot, channel);
        if (mVictors.containsKey(address)) {
            throw new RuntimeException(
                    "Tried to make more than one victor at " + address.toString());
        }
        mVictors.put(address, new EmulationVictor(mSimulationServer, slot, channel));
    }

    private void addEncoder(int aSlot, int aChannel, int bSlot, int bChannel) {
        EncoderAddress address = new EncoderAddress(aSlot, aChannel, bSlot, bChannel);
        // TODO: check digital IO more completely
        if (mEncoders.containsKey(address)) {
            throw new RuntimeException(
                    "Tried to make more than one encoder at " + address.toString());
        }
        mEncoders.put(address, new EmulationEncoder(
                aSlot,
                aChannel,
                bSlot,
                bChannel));
    }

    public EmulationVictor.EmulationVictorDelegate getVictor(int slot, int channel) {
        VictorAddress address = new VictorAddress(slot, channel);
        if (!mVictors.containsKey(address)) {
            throw new RuntimeException("No victor available at " + address.toString());
        }
        EmulationVictor adapter = (EmulationVictor) mVictors.get(address);
        return adapter.getInstance();
    }

    public EmulationEncoder.EmulationEncoderDelegate getEncoder(
            int aSlot,
            int aChannel,
            int bSlot,
            int bChannel,
            boolean reverseDirection,
            CounterBase.EncodingType encodingType) {
        EncoderAddress address = new EncoderAddress(aSlot, aChannel, bSlot, bChannel);
        if (!mEncoders.containsKey(address)) {
            throw new RuntimeException("No encoder available at " + address.toString());
        }
        EmulationEncoder adapter = (EmulationEncoder) mEncoders.get(address);
        return adapter.getInstance(reverseDirection, encodingType);
    }

}
