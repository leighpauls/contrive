package com.leighpauls;

import com.example.MyRobotCode;
import com.leighpauls.wpiabstraction.IterativeRobotInterface;

/**
 * The main file that gets run during a simulation
 */
public class SimulationInterface {

    private static final long CYCLE_TIME_MS = 20;
    private static final int DISABLED_CYCLES = 100;
    private static final int AUTON_CYCLES = 1000;

    // TODO: apply a feedback cycle for timing
    public static void main(String[] args) {
        System.out.println("Hello World!");

        IterativeRobotInterface robot = new MyRobotCode();

        robot.robotInit();

        robot.disabledInit();
        for (int i = 0; i < DISABLED_CYCLES; ++i) {
            robot.disabledPeriodic();
            try {
                Thread.sleep(CYCLE_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        robot.autonomousInit();
        for (int i = 0; i < AUTON_CYCLES; ++i) {
            robot.autonomousPeriodic();
            try {
                Thread.sleep(CYCLE_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
