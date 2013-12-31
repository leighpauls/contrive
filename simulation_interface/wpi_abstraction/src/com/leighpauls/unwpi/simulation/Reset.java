package com.leighpauls.unwpi.simulation;

import com.leighpauls.unwpi.simulation.AbstractActuatorCommand;
import org.json.simple.JSONObject;

/**
 * Special actuator command for resetting the robot
 */
public class Reset extends AbstractActuatorCommand {

    public Reset() {
        super("reset");
    }

    protected JSONObject getCommandData() {
        JSONObject cmd = new JSONObject();
        cmd.put("x", new Double(-7.5));
        cmd.put("y", new Double(0.1));
        cmd.put("z", new Double(-1));

        cmd.put("pitch", new Double(0));
        cmd.put("yaw", new Double(90));
        cmd.put("roll", new Double(0));

        return cmd;
    }
}
