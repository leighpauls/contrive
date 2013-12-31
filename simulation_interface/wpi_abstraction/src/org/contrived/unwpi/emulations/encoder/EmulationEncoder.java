package org.contrived.unwpi.emulations.encoder;

import edu.wpi.first.wpilibj.CounterBase;

/**
 * Implementation of an emulated encoder
 */
public class EmulationEncoder {

    // encoder state
    private double mPosition;
    private double mPeriod;
    private boolean mDirection;

    public EmulationEncoder() {
        mPosition = 0;
        mPeriod = 0;
        mDirection = true;
    }

    public EmulationEncoderDelegate getInstance(
            boolean reverseDirection,
            CounterBase.EncodingType encodingType) {
        return new EmulationEncoderDelegate(reverseDirection, encodingType);
    }

    public void updateSensor(double position, double period, boolean direction) {
        mPosition = position;
        mPeriod = period;
        mDirection = direction;
    }

    public class EmulationEncoderDelegate extends ContrivedEncoder {
        private boolean mReverseDirection;
        private double mDistPerPulse;
        private boolean mStarted;
        private final int mEncodingScale;

        public EmulationEncoderDelegate(boolean reverseDirection, CounterBase.EncodingType encodingType) {
            mReverseDirection = reverseDirection;
            mDistPerPulse = 1.0;
            mStarted = false;

            if (encodingType.equals(CounterBase.EncodingType.k1X)) {
                mEncodingScale = 4;
            } else if (encodingType.equals(CounterBase.EncodingType.k2X)) {
                mEncodingScale = 2;
            } else {
                mEncodingScale = 1;
            }
        }

        public void start() {
            mStarted = true;
        }

        public void stop() {
            mStarted = false;
        }
        public void reset() {
            mPosition = 0;
        }
        public boolean getStopped() {
            return !mStarted;
        }

        public boolean getDirection() {
            return mDirection;
        }

        public double getDistance() {
            return mDistPerPulse * mPosition / mEncodingScale * (mReverseDirection ? -1.0 : 1.0);
        }

        public double getRate() {
            return mDistPerPulse / (mEncodingScale * mPeriod) * (mReverseDirection ? -1.0 : 1.0);
        }
        public void setDistancePerPulse(double distancePerPulse) {
            mDistPerPulse = distancePerPulse;
        }

        public void setReverseDirection(boolean reverseDirection) {
            mReverseDirection = reverseDirection;
        }
    }
}
