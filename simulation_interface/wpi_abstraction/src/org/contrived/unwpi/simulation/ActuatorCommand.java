package org.contrived.unwpi.simulation;

import org.json.simple.JSONObject;

/**
 * Interface for robot output actuatorcommands
 */
public interface ActuatorCommand {
    JSONObject getCommand();
}
