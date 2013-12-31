package com.leighpauls;

import com.example.MyRobotCode;
import com.leighpauls.unwpi.IterativeRobotInterface;
import com.leighpauls.unwpi.simulation.SimulationModel;
import com.leighpauls.unwpi.simulation.SimulationServer;

import java.util.Date;

/**
 * The main file that gets run during a simulation
 */
public class SimulationRuntime {

    private static final int DISABLED_CYCLES = 10;
    private static final int AUTON_CYCLES = 100;

    public static void main(String[] args) {
        System.out.println("Simulation starting");

        SimulationServer server = SimulationModel.connectToSimulationServer();
        IterativeRobotInterface robot = new MyRobotCode();

        server.reset();

        server.syncSensors();
        robot.robotInit();
        robot.disabledInit();
        server.syncActuators();
        for (int i = 0; i < DISABLED_CYCLES; ++i) {
            server.syncSensors();
            robot.disabledPeriodic();
            server.syncActuators();
        }

        server.syncSensors();
        robot.autonomousInit();
        server.syncActuators();
        for (int i = 0; i < AUTON_CYCLES; ++i) {
            server.syncSensors();
            robot.autonomousPeriodic();
            server.syncActuators();
        }

        server.close();
    }
}
