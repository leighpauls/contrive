package com.leighpauls.unwpi.simulation;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

/**
 * Handles sync/communication with the simulation
 */
public class SimulationServer {
    private final Socket mSocket;
    private final PrintWriter mWriter;
    private final BufferedReader mReader;
    private StringBuffer inputBuffer;

    /// map of sensorType string to SensorCommandHandler
    private final HashMap mSensorHandlers;

    public SimulationServer(SensorCommandHandler[] handlers) {
        try {
            mSocket = new Socket("0.0.0.0", 54321);
            mWriter = new PrintWriter(mSocket.getOutputStream(), true);
            mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mSensorHandlers = new HashMap();
        for (int i = 0; i < handlers.length; ++i) {
            mSensorHandlers.put(handlers[i].getSensorTypeName(), handlers[i]);
        }
        inputBuffer = new StringBuffer();
    }

    public void sendActuatorCommand(ActuatorCommand command) {
        String cmdString = command.getCommand().toJSONString();
        System.out.println(cmdString);
        mWriter.println(cmdString);
    }

    private void handleCommand(String line) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject object = (JSONObject) parser.parse(line);
            String typeName = (String) object.get("type");
            SensorCommandHandler handler = (SensorCommandHandler) mSensorHandlers.get(typeName);
            handler.handleSensorCommand(object);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleSensorCommands() {
        System.out.println("Handle sensor commands!");

        // read out of the reader
        try {
            while (mReader.ready()) {
                int code = mReader.read();
                if (code == '\n') {
                    String line = inputBuffer.toString();
                    handleCommand(line);
                    inputBuffer = new StringBuffer();
                } else {
                    inputBuffer.appendCodePoint(code);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
