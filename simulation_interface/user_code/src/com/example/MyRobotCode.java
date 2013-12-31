package com.example;

import org.contrived.unwpi.IterativeRobotInterface;
import org.contrived.unwpi.emulations.encoder.ContrivedEncoder;
import org.contrived.unwpi.emulations.gyro.ContrivedGyro;
import org.contrived.unwpi.emulations.victor.ContrivedVictor;

/**
 * Write your robot control code here
 */
public class MyRobotCode implements IterativeRobotInterface {

    private final ContrivedVictor mLeftDriveVictor;
    private final ContrivedVictor mRightDriveVictor;

    private final ContrivedEncoder mLeftDriveEncoder;
    private final ContrivedEncoder mRightDriveEncoder;

    private final ContrivedGyro mGyro;

    public MyRobotCode() {
        mLeftDriveVictor = ContrivedVictor.getInstance(1);
        mRightDriveVictor = ContrivedVictor.getInstance(2);

        mLeftDriveEncoder = ContrivedEncoder.getInstance(1, 2);
        mRightDriveEncoder = ContrivedEncoder.getInstance(3, 4);

        mGyro = ContrivedGyro.getInstance(1);
    }

    public void robotInit() {}

    public void disabledInit() {
        mLeftDriveVictor.set(0.0);
        mRightDriveVictor.set(0.0);
    }
    public void disabledPeriodic() {}

    public void autonomousInit() {}
    public void autonomousPeriodic() {
        mLeftDriveVictor.set(2-mLeftDriveEncoder.getDistance());
        mRightDriveVictor.set(2-mRightDriveEncoder.getDistance());
        System.out.println(mGyro.getAngle());
    }

    public void teleopInit() {}
    public void teleopPeriodic() {}
}
