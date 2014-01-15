package org.contrived;

import edu.wpi.first.wpilibj.IterativeRobot;
import org.contrived.unwpi.simulation.SimulationModel;
import org.contrived.unwpi.simulation.SimulationServer;

import java.lang.reflect.InvocationTargetException;

/**
 * The main file that gets run during a simulation
 */
public class SimulationRuntime {

    private static final int DISABLED_CYCLES = 10;
    private static final int AUTON_CYCLES = 500;

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        System.out.println("Simulation starting");

        // TODO: read the manifest file
        IterativeRobot robot = (IterativeRobot) Class
                .forName("ca.teamdave.letterman.LettermanMain")
                .getConstructors()[0]
                .newInstance(new Object[0]);

        SimulationServer server = SimulationModel.connectToSimulationServer();
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
