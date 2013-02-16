package couk.jenxsol.timing;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created with Intellij with Android, BIZZBY product.
 * See licencing for usage of this code.
 * <p/>
 * User: chris
 * Date: 16/02/2013
 * Time: 17:50
 */
public class StopWatchTest
{

    private int mFinishedLanePos;
    private Lane mFinishedLane;
    private long mFinishedLaneTime;
    //
    private final StopWatch.OnLaneUpdatedListener mListener = new StopWatch.OnLaneUpdatedListener()
    {

        @Override
        public void onLaneTimeStarted(final Lane lane, final int position)
        {
        }

        @Override
        public void onLaneTimeUpdated(final Lane lane, final int position, final long currentTime)
        {
        }

        @Override
        public void onLaneFinished(final Lane lane, final int position, final long finishedTime)
        {
            mFinishedLanePos = position;
            mFinishedLane = lane;
            mFinishedLaneTime = finishedTime;
        }
    };
    private StopWatch mStopWatch;

    @Before
    public void setUp() throws Exception
    {
        mStopWatch = new StopWatch(10);
        assertThat(mStopWatch).isNotNull();
    }

    @After
    public void tearDown() throws Exception
    {
        if (mStopWatch.isRunning())
            mStopWatch.finish();
    }

    @Test
    public void test_started() throws Exception
    {
        mStopWatch.start();
        Thread.sleep(10);
        assertThat(mStopWatch.isRunning()).isTrue();
        assertThat(mStopWatch.isStopped()).isFalse();
        assertThat(mStopWatch.isStarted()).isTrue();
    }

    @Test
    public void test_notStarted() throws Exception
    {
        assertThat(mStopWatch.isRunning()).isFalse();
        assertThat(mStopWatch.isStopped()).isFalse();
        assertThat(mStopWatch.isStarted()).isFalse();
    }

    @Test
    public void test_isStopped() throws Exception
    {
        mStopWatch.start();
        Thread.sleep(10);
        mStopWatch.finish();
        Thread.sleep(10);
        assertThat(mStopWatch.isStopped()).isTrue();

    }

    @Test
    public void test_doubleStartException() throws Exception
    {
        mStopWatch.start();
        Exception ex = null;
        try
        {
            mStopWatch.start();
        }
        catch (IllegalStateException e)
        {
            ex = e;
        }
        assertThat(ex).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void test_finishException() throws Exception
    {
        Exception ex = null;
        try
        {
            mStopWatch.finish();
        }
        catch (IllegalStateException e)
        {
            ex = e;
        }
        assertThat(ex).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void test_finishLaneException() throws Exception
    {
        Exception ex = null;
        try
        {
            mStopWatch.stopLane(0, System.currentTimeMillis());
        }
        catch (IllegalStateException e)
        {
            ex = e;
        }
        assertThat(ex).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void test_finishLane() throws Exception
    {
        mStopWatch.setLaneUpdatedListener(mListener);
        mStopWatch.start();
        Thread.sleep(10);
        long stoppedTime = mStopWatch.stopLane(0, System.currentTimeMillis());
        assertThat(mFinishedLanePos).isEqualTo(0);
        assertThat(mFinishedLaneTime).isEqualTo(stoppedTime);
        Thread.sleep(10);
        stoppedTime = mStopWatch.stopLane(9, System.currentTimeMillis());
        assertThat(mFinishedLanePos).isEqualTo(9);
        assertThat(mFinishedLaneTime).isEqualTo(stoppedTime);
    }



}