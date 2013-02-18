package couk.jenxsol;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import couk.jenxsol.timing.Lane;
import couk.jenxsol.timing.StopWatch;
import couk.jenxsol.timing.Timer;
import couk.jenxsol.utils.Colours;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Created with Intellij with Android, BIZZBY product.
 * See licencing for usage of this code.
 * <p/>
 * User: chris
 * Date: 16/02/2013
 * Time: 01:12
 */
public class Application implements ActionListener, KeyEventDispatcher
{
    public static final String DEFAULT_TEXT = "0:00:000";

    public static void main(final String[] args)
    {
        JFrame frame = new JFrame("Application");
        frame.setContentPane(new Application().mMainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private static JTextField createTextField()
    {
        final JTextField textTimer = new JTextField();
        textTimer.setAutoscrolls(true);
        textTimer.setBackground(Colours.WHITISH);
        textTimer.setForeground(Colours.FONT_DARK_GREY);
        textTimer.setEditable(false);
        textTimer.setFocusable(false);
        textTimer.setText(DEFAULT_TEXT);
        return textTimer;
    }

    private static JLabel createTextLabel()
    {
        final JLabel textLabel = new JLabel();
        textLabel.setFocusable(false);
        textLabel.setForeground(Colours.FONT_DARK_GREY);
        textLabel.setOpaque(true);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return textLabel;
    }

    private final StopWatch.OnLaneUpdatedListener mListener = new StopWatch.OnLaneUpdatedListener()
    {
        @Override
        public void onLaneTimeStarted(final Lane lane, final int position)
        {
            setTextStatusFromPos(position, STATUS.RUNNING);
        }

        @Override
        public void onLaneTimeUpdated(final Lane lane, final int position, final long currentTime)
        {
            setTextTimersFromPos(position, currentTime);
        }

        @Override
        public void onLaneFinished(final Lane lane, final int position, final long finishedTime)
        {
            setTextTimersFromPos(position, finishedTime);
            setTextStatusFromPos(position, STATUS.STOPPED);
        }
    };
    private final StopWatch.OnStopWatchListener mStopWatchListener = new StopWatch.OnStopWatchListener()
    {
        @Override
        public void onStopWatchStart()
        {
            buttonStates();
        }

        @Override
        public void onStopWatchStop()
        {
            buttonStates();
        }
    };
    /**
     * Timer class
     */
    private final Timer mTimer = Timer.get();
    /**
     * Ui stuff
     */
    private JPanel mMainPanel;
    private JTextField mTextTimer1;
    private JTextField mTextTimer2;
    private JTextField mTextTimer3;
    private JTextField mTextTimer4;
    private JTextField mTextTimer5;
    private JTextField mTextTimer6;
    private JTextField mTextTimer7;
    private JTextField mTextTimer8;
    private JTextField mTextTimer9;
    private JTextField mTextTimer10;
    private JButton mStartStopButton;
    private JButton mResetButton;
    private JLabel mStatusTimer1;
    private JLabel mStatusTimer3;
    private JLabel mStatusTimer2;
    private JLabel mStatusTimer4;
    private JLabel mStatusTimer5;
    private JLabel mStatusTimer6;
    private JLabel mStatusTimer7;
    private JLabel mStatusTimer8;
    private JLabel mStatusTimer9;
    private JLabel mStatusTimer10;

    public Application()
    {
        $$$setupUI$$$();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
        mStartStopButton.addActionListener(this);
        mResetButton.addActionListener(this);
        buttonStates();
        resetTextTimers();
    }

    private void buttonStates()
    {
        mResetButton.setEnabled(false);
        if (mTimer.isRunning())
            mStartStopButton.setText("STOP");
        else
            mStartStopButton.setText("START");
        if (mTimer.isStopped())
        {
            mStartStopButton.setEnabled(false);
            mResetButton.setEnabled(true);
        }
        else
        {
            mStartStopButton.setEnabled(true);
        }
    }

    @Override
    public void actionPerformed(final ActionEvent actionEvent)
    {
        Object item = actionEvent.getSource();
        final long when = actionEvent.getWhen();
        if (item.equals(mStartStopButton))
        {
            if (mTimer.isRunning())
            {
                System.out.println("StartStopButton Running -> Stop");
                mTimer.stop();
            }
            else if (!mTimer.isStopped())
            {
                System.out.println("StartStopButton Stopped -> Start");
                mTimer.start(10, mListener, mStopWatchListener);
            }
        }
        if (item.equals(mResetButton))
        {
            System.out.println("ResetButton [" + when + "]");
            if (mTimer.isRunning()) return;
            mTimer.clear();
            resetTextTimers();
            buttonStates();
        }
    }

    private void setTextTimersFromPos(int pos, long when)
    {
        final JTextField field;
        switch (pos)
        {
            case 0:
                field = mTextTimer1;
                break;
            case 1:
                field = mTextTimer2;
                break;
            case 2:
                field = mTextTimer3;
                break;
            case 3:
                field = mTextTimer4;
                break;
            case 4:
                field = mTextTimer5;
                break;
            case 5:
                field = mTextTimer6;
                break;
            case 6:
                field = mTextTimer7;
                break;
            case 7:
                field = mTextTimer8;
                break;
            case 8:
                field = mTextTimer9;
                break;
            case 9:
                field = mTextTimer10;
                break;
            default:
                field = null;
                break;
        }
        if (field != null)
        {
            setTextTimerTime(field, when);
        }
    }

    private void setTextStatusFromPos(final int pos, final STATUS status)
    {
        final JLabel label;
        switch (pos)
        {
            case 0:
                label = mStatusTimer1;
                break;
            case 1:
                label = mStatusTimer2;
                break;
            case 2:
                label = mStatusTimer3;
                break;
            case 3:
                label = mStatusTimer4;
                break;
            case 4:
                label = mStatusTimer5;
                break;
            case 5:
                label = mStatusTimer6;
                break;
            case 6:
                label = mStatusTimer7;
                break;
            case 7:
                label = mStatusTimer8;
                break;
            case 8:
                label = mStatusTimer9;
                break;
            case 9:
                label = mStatusTimer10;
                break;
            default:
                label = null;
                break;
        }
        if (label != null)
        {
            setTextStatusTime(label, status);
        }

    }

    private void resetTextTimers()
    {
        setTextTimerTime(mTextTimer1, 0);
        setTextStatusTime(mStatusTimer1, STATUS.WAITING);
        setTextTimerTime(mTextTimer2, 0);
        setTextStatusTime(mStatusTimer2, STATUS.WAITING);
        setTextTimerTime(mTextTimer3, 0);
        setTextStatusTime(mStatusTimer3, STATUS.WAITING);
        setTextTimerTime(mTextTimer4, 0);
        setTextStatusTime(mStatusTimer4, STATUS.WAITING);
        setTextTimerTime(mTextTimer5, 0);
        setTextStatusTime(mStatusTimer5, STATUS.WAITING);
        setTextTimerTime(mTextTimer6, 0);
        setTextStatusTime(mStatusTimer6, STATUS.WAITING);
        setTextTimerTime(mTextTimer7, 0);
        setTextStatusTime(mStatusTimer7, STATUS.WAITING);
        setTextTimerTime(mTextTimer8, 0);
        setTextStatusTime(mStatusTimer8, STATUS.WAITING);
        setTextTimerTime(mTextTimer9, 0);
        setTextStatusTime(mStatusTimer9, STATUS.WAITING);
        setTextTimerTime(mTextTimer10, 0);
        setTextStatusTime(mStatusTimer10, STATUS.WAITING);
    }

    private void setTextStatusTime(final JLabel label, STATUS status)
    {
        switch (status)
        {
            case STOPPED:
                label.setText("Finished");
                label.setBackground(Colours.WHITISH);
                break;
            case RUNNING:
                label.setText("Swimming");
                label.setBackground(Color.ORANGE);
                break;
            case WAITING:
                label.setText("Ready");
                label.setBackground(Colours.WHITISH);
                break;
            case FIRST:
                label.setText("First");
                label.setBackground(Colours.GREEN_DARK);
                break;
            case SECOND:
                label.setText("Second");
                label.setBackground(Colours.GREEN_MID);
                break;
            case THIRD:
                label.setText("Third");
                label.setBackground(Colours.GREEN_LIGHT);
                break;
        }
    }

    private void setTextTimerTime(final JTextField textTimer, final long time)
    {
        int minutes = (int) ((time / (1000 * 60)) % 60);
        int seconds = (int) (time / 1000) % 60;
        int millis = (int) time % 1000;
        textTimer.setText(String.format("%d:%02d:%03d", minutes, seconds, millis));
    }

    @Override
    public boolean dispatchKeyEvent(final KeyEvent keyEvent)
    {
        final boolean keyPressed = (KeyEvent.KEY_PRESSED == keyEvent.getID());
        if (!keyPressed) return false;
        final String numberPressed;
        final long when = keyEvent.getWhen();
        switch (keyEvent.getKeyCode())
        {
            case KeyEvent.VK_1:
                numberPressed = "1";
                stopLane(0, when);
                break;
            case KeyEvent.VK_2:
                numberPressed = "2";
                stopLane(1, when);
                break;
            case KeyEvent.VK_3:
                numberPressed = "3";
                stopLane(2, when);
                break;
            case KeyEvent.VK_4:
                numberPressed = "4";
                stopLane(3, when);
                break;
            case KeyEvent.VK_5:
                numberPressed = "5";
                stopLane(4, when);
                break;
            case KeyEvent.VK_6:
                numberPressed = "6";
                stopLane(5, when);
                break;
            case KeyEvent.VK_7:
                numberPressed = "7";
                stopLane(6, when);
                break;
            case KeyEvent.VK_8:
                numberPressed = "8";
                stopLane(7, when);
                break;
            case KeyEvent.VK_9:
                numberPressed = "9";
                stopLane(8, when);
                break;
            case KeyEvent.VK_0:
                numberPressed = "10";
                stopLane(9, when);
                break;
            default:
                numberPressed = null;
        }
        if (numberPressed != null)
            System.out.println("Pressed [" + numberPressed + "] [" + when + "]");
        return numberPressed != null;
    }

    private final void stopLane(int pos, final long when)
    {
        if (mTimer.isRunning()) mTimer.stop(pos, when);
    }

    private void createUIComponents()
    {
        mTextTimer1 = createTextField();
        mTextTimer2 = createTextField();
        mTextTimer3 = createTextField();
        mTextTimer4 = createTextField();
        mTextTimer5 = createTextField();
        mTextTimer6 = createTextField();
        mTextTimer7 = createTextField();
        mTextTimer8 = createTextField();
        mTextTimer9 = createTextField();
        mTextTimer10 = createTextField();

        mStatusTimer1 = createTextLabel();
        mStatusTimer2 = createTextLabel();
        mStatusTimer3 = createTextLabel();
        mStatusTimer4 = createTextLabel();
        mStatusTimer5 = createTextLabel();
        mStatusTimer6 = createTextLabel();
        mStatusTimer7 = createTextLabel();
        mStatusTimer8 = createTextLabel();
        mStatusTimer9 = createTextLabel();
        mStatusTimer10 = createTextLabel();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$()
    {
        createUIComponents();
        mMainPanel = new JPanel();
        mMainPanel.setLayout(new FormLayout("fill:m:grow,center:4dlu:noGrow,fill:max(m;192px):noGrow,left:4dlu:noGrow,fill:4dlu:noGrow", "center:20dlu:noGrow,top:4dlu:noGrow,center:358px:grow,top:4dlu:noGrow,center:4dlu:noGrow"));
        mMainPanel.setFocusable(false);
        mMainPanel.setMinimumSize(new Dimension(620, 410));
        mMainPanel.setOpaque(true);
        mMainPanel.setPreferredSize(new Dimension(600, 410));
        mMainPanel.setRequestFocusEnabled(true);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FormLayout("fill:6dlu:noGrow,left:4dlu:noGrow,fill:max(d;12dlu):noGrow,left:8dlu:noGrow,fill:d:grow,left:8dlu:noGrow,fill:max(m;42dlu):noGrow,left:4dlu:noGrow,fill:8dlu:noGrow", "center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
        CellConstraints cc = new CellConstraints();
        mMainPanel.add(panel1, cc.xy(1, 3, CellConstraints.DEFAULT, CellConstraints.CENTER));
        mTextTimer1.setEditable(false);
        panel1.add(mTextTimer1, cc.xy(5, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimer2.setEditable(false);
        panel1.add(mTextTimer2, cc.xy(5, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimer3.setEditable(false);
        panel1.add(mTextTimer3, cc.xy(5, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimer4.setEditable(false);
        panel1.add(mTextTimer4, cc.xy(5, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimer5.setEditable(false);
        panel1.add(mTextTimer5, cc.xy(5, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimer6.setEditable(false);
        panel1.add(mTextTimer6, cc.xy(5, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimer7.setEditable(false);
        panel1.add(mTextTimer7, cc.xy(5, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimer8.setEditable(false);
        panel1.add(mTextTimer8, cc.xy(5, 15, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimer9.setEditable(false);
        panel1.add(mTextTimer9, cc.xy(5, 17, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimer10.setEditable(false);
        panel1.add(mTextTimer10, cc.xy(5, 19, CellConstraints.FILL, CellConstraints.DEFAULT));
        mStatusTimer1.setHorizontalAlignment(0);
        mStatusTimer1.setText("Swimming");
        panel1.add(mStatusTimer1, cc.xy(7, 1, CellConstraints.FILL, CellConstraints.FILL));
        mStatusTimer2.setText("Status");
        panel1.add(mStatusTimer2, cc.xy(7, 3, CellConstraints.FILL, CellConstraints.FILL));
        mStatusTimer3.setText("Status");
        panel1.add(mStatusTimer3, cc.xy(7, 5, CellConstraints.FILL, CellConstraints.FILL));
        mStatusTimer4.setText("Status");
        panel1.add(mStatusTimer4, cc.xy(7, 7, CellConstraints.FILL, CellConstraints.FILL));
        mStatusTimer5.setText("Status");
        panel1.add(mStatusTimer5, cc.xy(7, 9, CellConstraints.FILL, CellConstraints.FILL));
        mStatusTimer6.setText("Status");
        panel1.add(mStatusTimer6, cc.xy(7, 11, CellConstraints.FILL, CellConstraints.FILL));
        mStatusTimer7.setText("Status");
        panel1.add(mStatusTimer7, cc.xy(7, 13, CellConstraints.FILL, CellConstraints.FILL));
        mStatusTimer8.setText("Status");
        panel1.add(mStatusTimer8, cc.xy(7, 15, CellConstraints.FILL, CellConstraints.FILL));
        mStatusTimer9.setText("Status");
        panel1.add(mStatusTimer9, cc.xy(7, 17, CellConstraints.FILL, CellConstraints.FILL));
        mStatusTimer10.setText("Status");
        panel1.add(mStatusTimer10, cc.xy(7, 19, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(0);
        label1.setText("1");
        panel1.add(label1, cc.xy(3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label2 = new JLabel();
        label2.setHorizontalAlignment(0);
        label2.setText("2");
        panel1.add(label2, cc.xy(3, 3, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label3 = new JLabel();
        label3.setHorizontalAlignment(0);
        label3.setText("3");
        panel1.add(label3, cc.xy(3, 5, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label4 = new JLabel();
        label4.setHorizontalAlignment(0);
        label4.setText("4");
        panel1.add(label4, cc.xy(3, 7, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label5 = new JLabel();
        label5.setHorizontalAlignment(0);
        label5.setText("5");
        panel1.add(label5, cc.xy(3, 9, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label6 = new JLabel();
        label6.setHorizontalAlignment(0);
        label6.setText("6");
        panel1.add(label6, cc.xy(3, 11, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label7 = new JLabel();
        label7.setHorizontalAlignment(0);
        label7.setText("7");
        panel1.add(label7, cc.xy(3, 13, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label8 = new JLabel();
        label8.setHorizontalAlignment(0);
        label8.setText("8");
        panel1.add(label8, cc.xy(3, 15, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label9 = new JLabel();
        label9.setHorizontalAlignment(0);
        label9.setText("9");
        panel1.add(label9, cc.xy(3, 17, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label10 = new JLabel();
        label10.setHorizontalAlignment(0);
        label10.setText("10");
        panel1.add(label10, cc.xy(3, 19, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FormLayout("fill:d:grow", "center:max(d;4px):noGrow,top:3dlu:noGrow,top:max(d;20dlu):grow,top:3dlu:noGrow,center:d:grow(0.06)"));
        mMainPanel.add(panel2, cc.xy(3, 3, CellConstraints.DEFAULT, CellConstraints.FILL));
        mStartStopButton = new JButton();
        mStartStopButton.setEnabled(true);
        mStartStopButton.setText("Start/Stop");
        panel2.add(mStartStopButton, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.FILL));
        mResetButton = new JButton();
        mResetButton.setText("Reset");
        panel2.add(mResetButton, cc.xy(1, 5, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label11 = new JLabel();
        label11.setFont(new Font(label11.getFont().getName(), Font.BOLD, 18));
        label11.setText("Controls");
        panel2.add(label11, cc.xy(1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FormLayout("fill:d:grow", "center:d:grow"));
        panel3.setEnabled(false);
        mMainPanel.add(panel3, cc.xyw(1, 1, 3, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label12 = new JLabel();
        label12.setFont(new Font(label12.getFont().getName(), Font.BOLD, 24));
        label12.setText("Swimming Gala Timer");
        panel3.add(label12, cc.xy(1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
        label1.setLabelFor(mTextTimer1);
        label2.setLabelFor(mTextTimer2);
        label3.setLabelFor(mTextTimer3);
        label4.setLabelFor(mTextTimer4);
        label5.setLabelFor(mTextTimer5);
        label6.setLabelFor(mTextTimer6);
        label7.setLabelFor(mTextTimer7);
        label8.setLabelFor(mTextTimer8);
        label9.setLabelFor(mTextTimer9);
        label10.setLabelFor(mTextTimer10);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return mMainPanel;
    }

    private enum STATUS
    {
        WAITING,
        STOPPED,
        RUNNING,
        FIRST,
        SECOND,
        THIRD;
    }
}
