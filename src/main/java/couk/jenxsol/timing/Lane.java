package couk.jenxsol.timing;

public class Lane
{
    private final long mStartTime;
    private long mCurrentTime;
    private long mFinishTime;

    public Lane(final long startTime)
    {
        mStartTime = startTime;
        mCurrentTime = -0;
        mFinishTime = -1;
    }

    public boolean isStopped()
    {
        return mStartTime >= 0 && mFinishTime >= 0;
    }

    /**
     * Pass a singular reference time to the object (i.e. update the lane from the global clock, not a thread clock)
     * This method is not atomic but it is calculated off the start time so should be fine.
     *
     * @param currentTime the current system time, at least the same clock used the start the stop watch with
     * @return the current time after its been updated
     */
    public long update(long currentTime)
    {
        if (mFinishTime >= 0) return mFinishTime;
        mCurrentTime = currentTime - mStartTime;
        return mCurrentTime;
    }

    /**
     * Stop the lane and get the timer, if the time is already stopped, this will do nothing apart from return the stopped time
     *
     * @param currentTime pass in the current system time, the same one used to start/update the timer
     * @return the time after starting
     */
    long stop(long currentTime)
    {
        update(currentTime);
        mFinishTime = mCurrentTime;
        return mFinishTime;
    }

    /**
     * Gets the current time in millis. Its not atomic but you can call this at any time it will be anywhere from 0 to #getFinishTime
     *
     * @return 0 to #getFinishTime
     */
    public long getCurrentTime()
    {
        return mCurrentTime;
    }

    /**
     * This method is not populated until you stop the lane. it will throw an {@link IllegalStateException} otherwise
     *
     * @return the finish time in millis or an IllegalStateException if its not finished yet
     * @throws IllegalStateException if lane is not stopped
     */
    public long getFinishTime()
    {
        if (mFinishTime < 0)
            throw new IllegalStateException("The lane must be stopped before getting the Finish Time");
        return mFinishTime;
    }
}