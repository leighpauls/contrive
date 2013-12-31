package com.leighpauls.unwpi.emulations.gyro;

import com.leighpauls.unwpi.emulations.addresses.AnalogAddress;
import com.leighpauls.unwpi.simulation.SensorCommandHandler;
import com.leighpauls.unwpi.simulation.SimulationModel;
import org.json.simple.JSONObject;

/**
 * Interprets gyro sensor commands
 */
public class GyroCommandHandler implements SensorCommandHandler {
    private final SimulationModel.SensorDelegate mSensorDelegate;

    public GyroCommandHandler(SimulationModel.SensorDelegate sensorDelegate) {
        mSensorDelegate = sensorDelegate;
    }

    public String getSensorTypeName() {
        return "gyro";
    }

    public void handleSensorCommand(JSONObject command) {
        JSONObject data = (JSONObject) command.get("data");
        int slot = Integer.parseInt((String)data.get("slot"));
        int channel = Integer.parseInt((String)data.get("channel"));
        double voltage = Double.parseDouble((String)data.get("voltage"));
        double deltaTime = Double.parseDouble((String)data.get("time"));

        AnalogAddress address = new AnalogAddress(slot, channel);
        EmulationGyro gyro = mSensorDelegate.getGyro(address);
        gyro.updateSensor(voltage, deltaTime);
    }
}
