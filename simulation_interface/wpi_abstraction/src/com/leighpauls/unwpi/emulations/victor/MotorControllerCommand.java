package com.leighpauls.unwpi.emulations.victor;

import com.leighpauls.unwpi.simulation.ActuatorCommand;
import org.json.simple.JSONObject;

/**
 * Actuator command implementation for motor controllers
 */
public class MotorControllerCommand implements ActuatorCommand {
    private final int mSlot;
    private final int mChannel;
    private final double mPower;

    public MotorControllerCommand(int slot, int channel, double power) {
        mSlot = slot;
        mChannel = channel;
        mPower = power;
    }

    public JSONObject getCommand() {
        JSONObject cmdData = new JSONObject();
        cmdData.put("slot", Integer.valueOf(mSlot));
        cmdData.put("channel", Integer.valueOf(mChannel));
        cmdData.put("power", Double.valueOf(mPower));

        JSONObject cmd = new JSONObject();
        cmd.put("type", "motor_controller");
        cmd.put("data", cmdData);

        return cmd;
    }
}
