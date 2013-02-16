package couk.jenxsol.timing;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created with Intellij with Android, BIZZBY product.
 * See licencing for usage of this code.
 * <p/>
 * User: chris
 * Date: 16/02/2013
 * Time: 16:01
 */
public class LaneTest
{

    private Lane mLane;
    private long mSystemTime;

    @Before
    public void setUp() throws Exception
    {
        mSystemTime = System.currentTimeMillis();
        mLane = new Lane(mSystemTime);
    }

    @Test
    public void test_create() throws Exception
    {
        assertThat(mLane.getCurrentTime()).isEqualTo(0);
        Exception exception = null;
        try
        {
            mLane.getFinishTime();
        }
        catch (IllegalStateException e)
        {
            exception = e;
        }
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void test_timeUpdated() throws Exception
    {
        //Sleep for 10 millis
        Thread.sleep(10);
        long newTime = System.currentTimeMillis();
        mLane.update(newTime);
        assertThat(mLane.getCurrentTime()).isEqualTo(newTime - mSystemTime);
    }

    /**
     * Just makes sure that even after updating the time, it is still considered not finished
     * @throws Exception
     */
    @Test
    public void test_notFinished() throws Exception
    {
        Thread.sleep(10);
        mLane.update(System.currentTimeMillis());
        assertThat(mLane.isStopped()).isFalse();
        Exception exception = null;
        try
        {
            mLane.getFinishTime();
        }
        catch (IllegalStateException e)
        {
            exception = e;
        }
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void test_finish() throws Exception
    {
        //Sleep for 10 millis
        Thread.sleep(10);
        mLane.update(System.currentTimeMillis());
        Thread.sleep(10);
        long newTime = System.currentTimeMillis();
        mLane.stop(newTime);
        long finishedTime = newTime - mSystemTime;
        assertThat(mLane.getCurrentTime()).isEqualTo(finishedTime);
        assertThat(mLane.getFinishTime()).isEqualTo(finishedTime);
        assertThat(mLane.isStopped()).isTrue();
    }


    @Test
    public void test_isFinishedDoesNotUpdate() throws Exception
    {
        //Sleep for 10 millis
        Thread.sleep(10);
        mLane.update(System.currentTimeMillis());
        Thread.sleep(10);
        long newTime = System.currentTimeMillis();
        mLane.stop(newTime);
        long finishedTime = newTime - mSystemTime;
        Thread.sleep(10);
        assertThat(mLane.getCurrentTime()).isEqualTo(finishedTime);
        assertThat(mLane.getFinishTime()).isEqualTo(finishedTime);
        assertThat(mLane.isStopped()).isTrue();
    }
}
