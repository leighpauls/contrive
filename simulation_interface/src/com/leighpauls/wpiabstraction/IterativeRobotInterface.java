package com.leighpauls.wpiabstraction;

/**
 * Interface to build your robot control code on top of
 */
public interface IterativeRobotInterface {
    public void robotInit();

    public void autonomousInit();
    public void autonomousPeriodic();

    public void disabledInit();
    public void disabledPeriodic();

    public void teleopInit();
    public void teleopPeriodic();
}
