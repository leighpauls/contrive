package com.example;

import com.leighpauls.unwpi.IterativeRobotInterface;
import com.leighpauls.unwpi.emulations.victor.AbstractVictor;

/**
 * Write your robot control code here
 */
public class MyRobotCode implements IterativeRobotInterface {

    private final AbstractVictor mLeftDriveVictor;
    private final AbstractVictor mRightDriveVictor;

    public MyRobotCode() {
        mLeftDriveVictor = AbstractVictor.getInstance(2);
        mRightDriveVictor = AbstractVictor.getInstance(2);
    }

    public void robotInit() {}

    public void disabledInit() {
        mLeftDriveVictor.set(0.0);
        mRightDriveVictor.set(0.0);
    }
    public void disabledPeriodic() {}

    public void autonomousInit() {}
    public void autonomousPeriodic() {
        mLeftDriveVictor.set(1.0);
        mRightDriveVictor.set(-1.0);
    }

    public void teleopInit() {}
    public void teleopPeriodic() {}
}
