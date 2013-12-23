package com.leighpauls.unwpi.emulations.encoder;

import com.leighpauls.unwpi.simulation.SensorCommandHandler;
import com.leighpauls.unwpi.simulation.SimulationModel;
import org.json.simple.JSONObject;

/**
 * Implementation for handling encoder commands
 */
public class EncoderCommandHandler implements SensorCommandHandler {
    private final SimulationModel.ModelDelegate mModel;

    public EncoderCommandHandler(SimulationModel.ModelDelegate model) {
        mModel = model;
    }

    public String getSensorTypeName() {
        return "encoder";
    }

    public void handleSensorCommand(JSONObject command) {
        JSONObject data = (JSONObject) command.get("data");
        int aSlot = ((Integer)data.get("a_slot")).intValue();
        int aChannel = ((Integer)data.get("a_channel")).intValue();
        int bSlot = ((Integer)data.get("b_slot")).intValue();
        int bChannel = ((Integer)data.get("b_channel")).intValue();
        double lastPeriod = ((Double)data.get("period")).doubleValue();
        double position = ((Double)data.get("position")).doubleValue();

        mModel.getEncoder(new EncoderAddress(aSlot, aChannel, bSlot, bChannel))
                .updateSensor(position, lastPeriod);
    }
}
