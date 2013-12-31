package org.contrived.unwpi.simulation;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Handles sync/communication with the simulation
 */
public class SimulationServer {
    private final String END_OF_SENSORS_TYPE = "end_of_sensors";
    private final JSONObject END_OF_ACTUATORS_MESSAGE;

    private final Socket mSocket;
    private final PrintWriter mWriter;
    private final BufferedReader mReader;

    /// map of sensorType string to SensorCommandHandler
    private final HashMap mSensorHandlers;
    private final SimulationModel.ActuatorDelegate mActuatorDelegate;

    public SimulationServer(
            SensorCommandHandler[] handlers,
            SimulationModel.ActuatorDelegate actuatorDelegate) {
        try {
            mSocket = new Socket("0.0.0.0", 54321);
            mWriter = new PrintWriter(mSocket.getOutputStream(), true);
            mReader = new BufferedReader(
                    new InputStreamReader(new BufferedInputStream(mSocket.getInputStream())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mSensorHandlers = new HashMap();
        for (int i = 0; i < handlers.length; ++i) {
            mSensorHandlers.put(handlers[i].getSensorTypeName(), handlers[i]);
        }
        mActuatorDelegate = actuatorDelegate;

        END_OF_ACTUATORS_MESSAGE = new JSONObject();
        END_OF_ACTUATORS_MESSAGE.put("type", "end_of_actuators");
        END_OF_ACTUATORS_MESSAGE.put("data", new JSONObject());
    }

    private void handleSensorMessage(JSONObject message) {
        String typeName = (String) message.get("type");
        SensorCommandHandler handler = (SensorCommandHandler) mSensorHandlers.get(typeName);
        handler.handleSensorCommand(message);
    }

    public void syncSensors() {
        // read out of the reader
        try {
            // wait to hear about all the sensor states
            ArrayList receivedSensorMessages = new ArrayList();
            JSONParser parser = new JSONParser();

            while (true) {
                String line = mReader.readLine();
                JSONObject message = (JSONObject) parser.parse(line);
                if (END_OF_SENSORS_TYPE.equals(message.get("type"))) {
                    break;
                }
                receivedSensorMessages.add(message);
            }

            for (int i = 0; i < receivedSensorMessages.size(); ++i) {
                JSONObject message = (JSONObject) receivedSensorMessages.get(i);
                handleSensorMessage(message);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void syncActuators() {
        // send all of the actuator states
        JSONObject[] messages = mActuatorDelegate.getActuatorMessages();
        for (int i = 0; i < messages.length; ++i) {
            mWriter.println(messages[i].toJSONString());
        }
        mWriter.println(END_OF_ACTUATORS_MESSAGE.toJSONString());
    }

    public void reset() {
        syncSensors();
        mWriter.println(new Reset().getCommand().toJSONString());
        syncActuators();
    }

    public void close() {
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
