package com.leighpauls.unwpi.simulation;

import com.leighpauls.unwpi.emulations.encoder.EncoderAddress;
import com.leighpauls.unwpi.emulations.encoder.EmulationEncoderAdapter;
import com.leighpauls.unwpi.emulations.victor.VictorAddress;
import com.leighpauls.unwpi.emulations.victor.EmulationVictorAdapter;
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

    public class ModelDelegate {
        public EmulationVictorAdapter getVictor(VictorAddress address) {
            return (EmulationVictorAdapter) mVictors.get(address);
        }
        public EmulationEncoderAdapter getEncoder(EncoderAddress address) {
            return (EmulationEncoderAdapter) mEncoders.get(address);
        }
    }


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

    private void addVictor(int slot, int channel) {
        VictorAddress address = new VictorAddress(slot, channel);
        if (mVictors.containsKey(address)) {
            throw new RuntimeException(
                    "Tried to make more than one victor at " + address.toString());
        }
        mVictors.put(address, new EmulationVictorAdapter(mSimulationServer, slot, channel));
    }

    private void addEncoder(int aSlot, int aChannel, int bSlot, int bChannel) {
        EncoderAddress address = new EncoderAddress(aSlot, aChannel, bSlot, bChannel);
        // TODO: check digital IO more completely
        if (mEncoders.containsKey(address)) {
            throw new RuntimeException(
                    "Tried to make more than one encoder at " + address.toString());
        }
        mEncoders.put(address, new EmulationEncoderAdapter(
                aSlot,
                aChannel,
                bSlot,
                bChannel));
    }

    public EmulationVictorAdapter.EmulationVictor getVictor(int slot, int channel) {
        VictorAddress address = new VictorAddress(slot, channel);
        if (!mVictors.containsKey(address)) {
            throw new RuntimeException("No victor available at " + address.toString());
        }
        EmulationVictorAdapter adapter = (EmulationVictorAdapter) mVictors.get(address);
        return adapter.getInstance();
    }

    public EmulationEncoderAdapter.EmulationEncoder getEncoder(
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
        EmulationEncoderAdapter adapter = (EmulationEncoderAdapter) mEncoders.get(address);
        return adapter.getInstance(reverseDirection, encodingType);
    }
}
