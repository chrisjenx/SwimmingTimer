package couk.jenxsol.timing;

/**
 * Created with Intellij with Android, BIZZBY product.
 * See licencing for usage of this code.
 * <p/>
 * User: chris
 * Date: 16/02/2013
 * Time: 03:06
 */
public class Timer
{

    private static Timer sInstance;

    public static synchronized final Timer get()
    {
        if (sInstance == null) sInstance = new Timer();
        return sInstance;
    }

    /**
     * Joiner object for synchronisation
     */
    private final Object joiner = new Object();
    /**
     * Thread to run
     */
    private StopWatch mStopWatch;

    private Timer()
    {
    }

    /**
     * Stop the Thread running the stop watch
     *
     * @return
     */
    public boolean stop()
    {
        synchronized (joiner)
        {
            if (mStopWatch == null) return false;
            return mStopWatch.finish();
        }
    }

    public boolean stop(int pos, long when)
    {
        synchronized (joiner)
        {
            if (mStopWatch == null || !mStopWatch.isRunning() || mStopWatch.isStopped()) return false;
            mStopWatch.stopLane(pos, when);
            return true;
        }
    }

    /**
     * @param lanes
     * @return
     */
    public boolean start(int lanes, final StopWatch.OnLaneUpdatedListener listener, final StopWatch.OnStopWatchListener stopWatchListener)
    {
        synchronized (joiner)
        {
            if (isRunning()) return false;
            mStopWatch = new StopWatch(lanes);
            mStopWatch.setLaneUpdatedListener(listener);
            mStopWatch.setStopWatchListener(stopWatchListener);
            mStopWatch.start();
            return true;
        }
    }

    public void clear()
    {
        synchronized (joiner){
            if(mStopWatch != null && isRunning() || !isStopped()) return;
            mStopWatch = null;
        }
    }

    public boolean isRunning()
    {
        synchronized (joiner)
        {
            return (mStopWatch != null && mStopWatch.isRunning());
        }
    }

    public boolean isStopped()
    {
        synchronized (joiner)
        {
            return (mStopWatch != null && mStopWatch.isStopped());
        }
    }
}
