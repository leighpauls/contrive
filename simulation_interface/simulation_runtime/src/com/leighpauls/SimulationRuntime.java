package com.leighpauls;

import com.example.MyRobotCode;
import com.leighpauls.simulationactuators.Reset;
import com.leighpauls.unwpi.IterativeRobotInterface;
import com.leighpauls.unwpi.simulation.SimulationModel;
import com.leighpauls.unwpi.simulation.SimulationServer;

/**
 * The main file that gets run during a simulation
 */
public class SimulationRuntime {

    private static final long CYCLE_TIME_MS = 20;
    private static final int DISABLED_CYCLES = 10;
    private static final int AUTON_CYCLES = 100;

    // TODO: apply a feedback cycle for timing
    public static void main(String[] args) {
        System.out.println("Simualtion starting");

        SimulationServer server = SimulationModel.connectToSimulationServer();
        IterativeRobotInterface robot = new MyRobotCode();

        server.sendActuatorCommand(new Reset());
        server.handleSensorCommands();
        robot.robotInit();
        robot.disabledInit();
        server.handleSensorCommands();
        for (int i = 0; i < DISABLED_CYCLES; ++i) {
            robot.disabledPeriodic();
            server.handleSensorCommands();
            try {
                Thread.sleep(CYCLE_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        robot.autonomousInit();
        server.handleSensorCommands();
        for (int i = 0; i < AUTON_CYCLES; ++i) {
            robot.autonomousPeriodic();
            server.handleSensorCommands();
            try {
                Thread.sleep(CYCLE_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
