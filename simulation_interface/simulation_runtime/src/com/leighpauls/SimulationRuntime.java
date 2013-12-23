package com.leighpauls;

import com.example.MyRobotCode;
import com.leighpauls.unwpi.IterativeRobotInterface;
import com.leighpauls.unwpi.simulation.SimulationModel;

/**
 * The main file that gets run during a simulation
 */
public class SimulationRuntime {

    private static final long CYCLE_TIME_MS = 20;
    private static final int DISABLED_CYCLES = 100;
    private static final int AUTON_CYCLES = 1000;

    // TODO: apply a feedback cycle for timing
    public static void main(String[] args) {
        System.out.println("Hello World!");

        SimulationModel model = SimulationModel.getInstance();
        IterativeRobotInterface robot = new MyRobotCode();

        model.handleSensorCommands();
        robot.robotInit();
        robot.disabledInit();
        model.handleSensorCommands();
        for (int i = 0; i < DISABLED_CYCLES; ++i) {
            robot.disabledPeriodic();
            model.handleSensorCommands();
            try {
                Thread.sleep(CYCLE_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        robot.autonomousInit();
        model.handleSensorCommands();
        for (int i = 0; i < AUTON_CYCLES; ++i) {
            robot.autonomousPeriodic();
            model.handleSensorCommands();
            try {
                Thread.sleep(CYCLE_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
