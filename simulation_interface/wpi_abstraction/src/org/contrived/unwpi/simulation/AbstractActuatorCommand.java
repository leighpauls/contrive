package org.contrived.unwpi.simulation;

import org.json.simple.JSONObject;

/**
 * Wraps {@link ActuatorCommand} with the proper message formatting
 */
public abstract class AbstractActuatorCommand implements ActuatorCommand {
    private final String mTypename;

    public AbstractActuatorCommand(String typename) {
        mTypename = typename;
    }

    public final JSONObject getCommand() {
        JSONObject cmd = new JSONObject();
        cmd.put("type", mTypename);
        cmd.put("data", getCommandData());

        return cmd;
    }

    protected abstract JSONObject getCommandData();
}
