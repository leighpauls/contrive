package org.contrived.unwpi.simulation;

import org.contrived.unwpi.emulations.addresses.AnalogAddress;
import org.contrived.unwpi.emulations.addresses.PwmAddress;
import org.contrived.unwpi.emulations.encoder.ContrivedEncoder;
import org.contrived.unwpi.emulations.encoder.EncoderCommandHandler;
import org.contrived.unwpi.emulations.gyro.ContrivedGyro;
import org.contrived.unwpi.emulations.gyro.GyroCommandHandler;
import org.contrived.unwpi.emulations.victor.ContrivedVictor;
import org.contrived.unwpi.emulations.addresses.EncoderAddress;
import edu.wpi.first.wpilibj.CounterBase;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
    private final HashMap mGyros;

    private SimulationModel() {
        SensorDelegate sensorDelegate = new SensorDelegate();
        SensorCommandHandler[] handlers = new SensorCommandHandler[] {
                new EncoderCommandHandler(sensorDelegate),
                new GyroCommandHandler(sensorDelegate)
        };

        mSimulationServer = new SimulationServer(handlers, new ActuatorDelegate());

        mVictors = new HashMap();
        mEncoders = new HashMap();
        mGyros = new HashMap();

        RobotBuildDelegate buildDelegate = new RobotBuildDelegate();

        // TODO: do this build dynamically
        buildDelegate.addVictor(new PwmAddress(1, 1));
        buildDelegate.addVictor(new PwmAddress(1, 2));
        buildDelegate.addVictor(new PwmAddress(1, 3));
        buildDelegate.addVictor(new PwmAddress(1, 4));


        buildDelegate.addEncoder(new EncoderAddress(1, 1, 1, 2));
        buildDelegate.addEncoder(new EncoderAddress(1, 3, 1, 4));

        buildDelegate.addGyro(new AnalogAddress(1, 1));
    }

    /**
     * Exposes functions for injecting the state of the emulated sensors
     */
    public class SensorDelegate {
        public ContrivedEncoder getEncoder(EncoderAddress address) {
            return (ContrivedEncoder) mEncoders.get(address);
        }
        public ContrivedGyro getGyro(AnalogAddress address) {
            return (ContrivedGyro) mGyros.get(address);
        }
    }

    /**
     * Exposes functions for reading the emulated actuator output states
     */
    public class ActuatorDelegate {
        /**
         * @return Messages containing the states of all the actuators
         */
        public JSONObject[] getActuatorMessages() {
            ArrayList messages = new ArrayList();

            Iterator it = mVictors.values().iterator();
            while (it.hasNext()) {
                messages.add(((ContrivedVictor) it.next()).getActuatorCommand().getCommand());
            }
            JSONObject[] result = new JSONObject[messages.size()];
            messages.toArray(result);
            return result;
        }
    }

    /**
     * Exposes functions for building the robot's hardware (eg. victor/encoder addresses)
     */
    public class RobotBuildDelegate {
        public void addVictor(PwmAddress address) {
            mVictors.put(address, new ContrivedVictor(address));
        }
        public void addEncoder(EncoderAddress address) {
            mEncoders.put(address, new ContrivedEncoder());
        }
        public void addGyro(AnalogAddress address) {
            mGyros.put(address, new ContrivedGyro());
        }
    }

    public ContrivedVictor.ContrivedVictorDelegate getVictor(PwmAddress address) {
        if (!mVictors.containsKey(address)) {
            throw new RuntimeException("No victor available at " + address.toString());
        }
        return ((ContrivedVictor) mVictors.get(address)).getInstance();
    }

    public ContrivedEncoder.ContrivedEncoderDelegate getEncoder(
            EncoderAddress address,
            boolean reverseDirection,
            CounterBase.EncodingType encodingType) {
        if (!mEncoders.containsKey(address)) {
            throw new RuntimeException("No encoder available at " + address.toString());
        }
        return ((ContrivedEncoder) mEncoders.get(address))
                .getInstance(reverseDirection, encodingType);
    }

    public ContrivedGyro.ContrivedGyroDelegate getGyro(AnalogAddress address) {
        if (!mGyros.containsKey((address))) {
            throw new RuntimeException("No gyro available at " + address.toString());
        }
        return ((ContrivedGyro) mGyros.get(address)).getInstance();
    }

}
