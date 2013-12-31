package com.example;

import com.leighpauls.unwpi.IterativeRobotInterface;
import com.leighpauls.unwpi.emulations.encoder.AbstractEncoder;
import com.leighpauls.unwpi.emulations.gyro.AbstractGyro;
import com.leighpauls.unwpi.emulations.victor.AbstractVictor;

/**
 * Write your robot control code here
 */
public class MyRobotCode implements IterativeRobotInterface {

    private final AbstractVictor mLeftDriveVictor;
    private final AbstractVictor mRightDriveVictor;

    private final AbstractEncoder mLeftDriveEncoder;
    private final AbstractEncoder mRightDriveEncoder;

    private final AbstractGyro mGyro;

    public MyRobotCode() {
        mLeftDriveVictor = AbstractVictor.getInstance(1);
        mRightDriveVictor = AbstractVictor.getInstance(2);

        mLeftDriveEncoder = AbstractEncoder.getInstance(1, 2);
        mRightDriveEncoder = AbstractEncoder.getInstance(3, 4);

        mGyro = AbstractGyro.getInstance(1);
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
