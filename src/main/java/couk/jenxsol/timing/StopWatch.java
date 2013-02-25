package couk.jenxsol.timing;

/**
 * Stop Watch thread
 */
public class StopWatch extends Thread
{
    /**
     * 16 millis sleep, should keep it around 60fps
     */
    public static final long SLEEP_TIME = 16;
    //
    private final Object sync = new Object();
    // isRunning
    private volatile boolean run = false;
    private volatile boolean started = false;
    private volatile boolean stopped = false;
    // Lanes
    private Lane[] mLanes;
    //
    private OnLaneUpdatedListener mLaneUpdatedListener;
    private OnStopWatchListener mStopWatchListener;

    public StopWatch(int laneCount)
    {
        mLanes = new Lane[laneCount];
    }

    /**
     * Set the lane listener for the UI
     *
     * @param laneUpdatedListener
     */
    public void setLaneUpdatedListener(final OnLaneUpdatedListener laneUpdatedListener)
    {
        mLaneUpdatedListener = laneUpdatedListener;
    }

    public void setStopWatchListener(final OnStopWatchListener stopWatchListener)
    {
        mStopWatchListener = stopWatchListener;
    }

    @Override
    public void start()
    {
        synchronized (sync)
        {
            this.start(System.currentTimeMillis());
        }
    }

    /**
     * Trying to start the thread twice will throw an exception
     *
     * @param startTime current system time
     */
    public void start(long startTime)
    {
        synchronized (sync)
        {
            if (isAlive() || run) throw new IllegalStateException("StopWatch already started!");
            createLanes(startTime);
            run = true;
            started = true;
            super.start();
        }
    }

    /**
     * Finish all the lanes and stop the thread
     *
     * @return
     */
    public boolean finish()
    {
        synchronized (sync)
        {
            if (!isAlive() || !run) throw new IllegalStateException("StopWatch wasn't running!");
            run = false;
            stopLanes(System.currentTimeMillis());
            return stopped = true;
        }
    }

    /**
     * Stop a lane and stop it being updated
     *
     * @return
     */
    public long stopLane(int position, long when)
    {
        synchronized (sync)
        {
            if (!isStarted()) throw new IllegalStateException("StopWatch hasn't started yet!");
            if (mLanes[position].isStopped())
                return mLanes[position].getFinishTime();

            long finishTime = mLanes[position].stop(when);
            fireLaneFinished(mLanes[position], position, finishTime);
            if(!checkStillRunning()) finish();
            return finishTime;
        }
    }

    @Override
    public void run()
    {
        if (mStopWatchListener != null) mStopWatchListener.onStopWatchStart();
        sleep();
        while (run)
        {
            updateLanes(System.currentTimeMillis());
            sleep();
        }
        stopped = true;
        if (mStopWatchListener != null) mStopWatchListener.onStopWatchStop();
    }

    /**
     * Is the StopWatched running
     *
     * @return
     */
    public boolean isRunning()
    {
        synchronized (sync)
        {
            return isAlive() && run;
        }
    }

    public boolean isStarted()
    {
        synchronized (sync)
        {
            return started;
        }
    }

    public boolean isStopped()
    {
        synchronized (sync)
        {
            return started && stopped;
        }
    }

    public int runningCount()
    {
        synchronized (sync)
        {
            if (mLanes == null) return 0;
            int count = 0;
            for (Lane lane : mLanes)
            {
                if (!lane.isStopped()) count++;
            }
            return count;
        }
    }

    /**
     * Call after stopping a timer(s)
     */
    private boolean checkStillRunning()
    {
        if (isAlive() && runningCount() > 0)
            return true;

        return false;
    }

    /**
     * Caught sleep method
     */
    private void sleep()
    {
        try
        {
            Thread.sleep(SLEEP_TIME);
        }
        catch (InterruptedException e)
        {
        }
    }

    private final void stopLanes(final long finishTime)
    {
        long currTime;
        for (int i = 0; i < mLanes.length; i++)
        {
            if (!mLanes[i].isStopped())
            {
                currTime = mLanes[i].stop(finishTime);
                fireLaneFinished(mLanes[i], i, currTime);
            }
        }
    }

    private final void updateLanes(final long updateTime)
    {
        synchronized (sync)
        {
            long currTime;
            for (int i = 0; i < mLanes.length; i++)
            {
                if (!mLanes[i].isStopped())
                {
                    currTime = mLanes[i].update(updateTime);
                    fireLaneTimeUpdated(mLanes[i], i, currTime);
                }
            }
        }
    }

    private final void createLanes(final long startTime)
    {
        for (int i = 0; i < mLanes.length; i++)
        {
            mLanes[i] = new Lane(startTime);
            fireLaneTimeStarted(mLanes[i], i);
        }
    }

    private void fireLaneTimeStarted(final Lane lane, final int position)
    {
        if (null != mLaneUpdatedListener) mLaneUpdatedListener.onLaneTimeStarted(lane, position);
    }

    private void fireLaneTimeUpdated(final Lane lane, final int position, final long currentTime)
    {
        if (null != mLaneUpdatedListener) mLaneUpdatedListener.onLaneTimeUpdated(lane, position, currentTime);
    }

    private void fireLaneFinished(final Lane lane, final int position, final long finishedTime)
    {
        if (null != mLaneUpdatedListener) mLaneUpdatedListener.onLaneFinished(lane, position, finishedTime);
    }


    public static interface OnLaneUpdatedListener
    {

        public void onLaneTimeStarted(final Lane lane, final int position);

        public void onLaneTimeUpdated(final Lane lane, final int position, final long currentTime);

        public void onLaneFinished(final Lane lane, final int position, final long finishedTime);

    }

    public static interface OnStopWatchListener
    {

        public void onStopWatchStart();

        public void onStopWatchStop();
    }
}