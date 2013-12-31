package org.contrived.unwpi.emulations.encoder;

import org.contrived.unwpi.emulations.addresses.EncoderAddress;
import org.contrived.unwpi.simulation.SensorCommandHandler;
import org.contrived.unwpi.simulation.SimulationModel;
import org.json.simple.JSONObject;

/**
 * Implementation for handling encoder commands
 */
public class EncoderCommandHandler implements SensorCommandHandler {
    private final SimulationModel.SensorDelegate mModel;

    public EncoderCommandHandler(SimulationModel.SensorDelegate model) {
        mModel = model;
    }

    public String getSensorTypeName() {
        return "encoder";
    }

    public void handleSensorCommand(JSONObject command) {
        JSONObject data = (JSONObject) command.get("data");
        int aSlot = Integer.parseInt((String)data.get("a_slot"));
        int aChannel = Integer.parseInt((String)data.get("a_channel"));
        int bSlot = Integer.parseInt((String)data.get("b_slot"));
        int bChannel = Integer.parseInt((String)data.get("b_channel"));

        double lastPeriod = Double.parseDouble((String) data.get("period"));
        double position = Double.parseDouble((String) data.get("position"));
        boolean direction = Boolean.parseBoolean((String)data.get("direction"));

        EncoderAddress address = new EncoderAddress(aSlot, aChannel, bSlot, bChannel);
        EmulationEncoder encoder = mModel.getEncoder(address);
        if (encoder == null) {
            System.out.println("Received command for unknown encoder: " + address);
            return;
        }
        encoder.updateSensor(position, lastPeriod, direction);
    }
}
