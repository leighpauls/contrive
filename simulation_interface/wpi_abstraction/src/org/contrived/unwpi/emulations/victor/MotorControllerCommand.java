package org.contrived.unwpi.emulations.victor;

import org.contrived.unwpi.simulation.AbstractActuatorCommand;
import org.json.simple.JSONObject;

/**
 * Actuator command implementation for motor controllers
 */
public class MotorControllerCommand extends AbstractActuatorCommand {
    private final int mSlot;
    private final int mChannel;
    private final double mPower;

    public MotorControllerCommand(int slot, int channel, double power) {
        super("motor_controller");
        mSlot = slot;
        mChannel = channel;
        mPower = power;
    }

    protected JSONObject getCommandData() {
        JSONObject cmdData = new JSONObject();
        cmdData.put("slot", Integer.valueOf(mSlot));
        cmdData.put("channel", Integer.valueOf(mChannel));
        cmdData.put("power", Double.valueOf(mPower));

        return cmdData;
    }
}
