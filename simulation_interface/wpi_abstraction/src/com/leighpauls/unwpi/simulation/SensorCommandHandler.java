package com.leighpauls.unwpi.simulation;

import org.json.simple.JSONObject;

/**
 * Interface for parsing sensor input actuatorcommands
 */
public interface SensorCommandHandler {
    String getSensorTypeName();
    void handleSensorCommand(JSONObject command);
}
