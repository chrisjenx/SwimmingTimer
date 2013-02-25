package couk.jenxsol;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import couk.jenxsol.timing.Lane;
import couk.jenxsol.timing.StopWatch;
import couk.jenxsol.timing.Timer;
import couk.jenxsol.utils.Colours;
import couk.jenxsol.utils.Export;
import couk.jenxsol.utils.Log;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

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
    public static final String SHEET = "RawResults";

    public static void main(final String[] args)
    {
        // set the name of the application menu item
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Swimming Gala Timer");

        JFrame frame = new JFrame("Swimming Gala Timer");
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
            Log.d("Lane [" + position + "] Finished");
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
            Log.d("Stop watch stopped");
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
    private JButton mExportCurrentResultsButton;
    private JButton mChooseButton;
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
    private JTextField mTextTimerMills1;
    private JTextField mTextTimerMills2;
    private JTextField mTextTimerMills3;
    private JTextField mTextTimerMills4;
    private JTextField mTextTimerMills5;
    private JTextField mTextTimerMills6;
    private JTextField mTextTimerMills7;
    private JTextField mTextTimerMills8;
    private JTextField mTextTimerMills9;
    private JTextField mTextTimerMills10;
    private File mFile;

    public Application()
    {
        $$$setupUI$$$();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
        mStartStopButton.addActionListener(this);
        mResetButton.addActionListener(this);
        mExportCurrentResultsButton.addActionListener(this);
        mChooseButton.addActionListener(this);
        buttonStates();
        resetTextTimers();
    }

    private void buttonStates()
    {
        mResetButton.setEnabled(false);
        mExportCurrentResultsButton.setEnabled(false);
        if (mTimer.isRunning())
            mStartStopButton.setText("STOP");
        else
            mStartStopButton.setText("START");
        if (mTimer.isStopped())
        {
            mStartStopButton.setEnabled(false);
            mResetButton.setEnabled(true);
            if (mFile != null && mFile.exists() && mFile.isFile())
            {
                mExportCurrentResultsButton.setEnabled(true);
            }
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
        if (item.equals(mChooseButton))
        {
            final File file = Export.startFileChooser(mMainPanel, mFile);
            if (file != null)
            {
                mFile = file;
                mChooseButton.setText(mFile.getName());
                buttonStates();
            }
        }
        if (item.equals(mExportCurrentResultsButton) && mFile != null)
        {
            exportCurrentResults();
            mExportCurrentResultsButton.setEnabled(false);
        }
    }

    private void exportCurrentResults()
    {
        if (mFile == null) return;
        WritableWorkbook workbook = Export.getWorkBook(mFile);
        if (workbook == null)
        {
            //TODO Error
        }
        WritableSheet sheet = Export.getExportSheet(workbook, SHEET);
        if (sheet == null)
        {
            //TODO error
        }
        int newCol = Export.nextFreeColNumber(sheet);
        String time;
        for (int i = 0; i < 10; i++)
        {
            time = getTimerValue(i);
            Export.writeToCell(sheet, getTimerMillsValue(i), newCol, i);
            Log.d("Wrote time [" + time + "] to row [" + i + "]");
        }
        try
        {
            workbook.write();
            workbook.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (WriteException e)
        {
            e.printStackTrace();
        }
    }

    private String getTimerValue(int pos)
    {
        final JTextField field = getTextTimerForPos(pos);
        if (field != null)
        {
            return field.getText();
        }
        return null;
    }

    private String getTimerMillsValue(int pos)
    {
        final JTextField field = getTextTimerMillsForPos(pos);
        if (field != null)
        {
            return field.getText();
        }
        return null;
    }

    private void setTextTimersFromPos(int pos, long when)
    {
        final JTextField field = getTextTimerForPos(pos);
        final JTextField field1 = getTextTimerMillsForPos(pos);

        if (field != null)
        {
            setTextTimerTime(field, when);
        }
        if (field1 != null)
        {
            field1.setText(String.valueOf(when));
        }
    }

    private JTextField getTextTimerForPos(int pos)
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
        return field;

    }

    private JTextField getTextTimerMillsForPos(int pos)
    {
        final JTextField field;
        switch (pos)
        {
            case 0:
                field = mTextTimerMills1;
                break;
            case 1:
                field = mTextTimerMills2;
                break;
            case 2:
                field = mTextTimerMills3;
                break;
            case 3:
                field = mTextTimerMills4;
                break;
            case 4:
                field = mTextTimerMills5;
                break;
            case 5:
                field = mTextTimerMills6;
                break;
            case 6:
                field = mTextTimerMills7;
                break;
            case 7:
                field = mTextTimerMills8;
                break;
            case 8:
                field = mTextTimerMills9;
                break;
            case 9:
                field = mTextTimerMills10;
                break;
            default:
                field = null;
                break;
        }
        return field;
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
        getTextTimerMillsForPos(0).setText("");
        getTextTimerMillsForPos(1).setText("");
        getTextTimerMillsForPos(2).setText("");
        getTextTimerMillsForPos(3).setText("");
        getTextTimerMillsForPos(4).setText("");
        getTextTimerMillsForPos(5).setText("");
        getTextTimerMillsForPos(6).setText("");
        getTextTimerMillsForPos(7).setText("");
        getTextTimerMillsForPos(8).setText("");
        getTextTimerMillsForPos(9).setText("");

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

        mTextTimerMills1 = createTextField();
        mTextTimerMills2 = createTextField();
        mTextTimerMills3 = createTextField();
        mTextTimerMills4 = createTextField();
        mTextTimerMills5 = createTextField();
        mTextTimerMills6 = createTextField();
        mTextTimerMills7 = createTextField();
        mTextTimerMills8 = createTextField();
        mTextTimerMills9 = createTextField();
        mTextTimerMills10 = createTextField();

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
        mMainPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:m:grow,center:4dlu:noGrow,fill:max(m;192px):noGrow,left:4dlu:noGrow,fill:4dlu:noGrow", "center:358px:grow(0.9),top:3dlu:noGrow,center:max(d;20dlu):grow,top:4dlu:noGrow,center:4dlu:noGrow"));
        mMainPanel.setFocusable(false);
        mMainPanel.setMinimumSize(new Dimension(620, 410));
        mMainPanel.setOpaque(true);
        mMainPanel.setPreferredSize(new Dimension(600, 410));
        mMainPanel.setRequestFocusEnabled(true);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FormLayout("fill:max(d;12dlu):noGrow,left:8dlu:noGrow,fill:d:grow(0.6),left:4dlu:noGrow,fill:d:grow(0.4),left:8dlu:noGrow,fill:max(m;42dlu):noGrow,left:4dlu:noGrow,fill:8dlu:noGrow", "center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
        CellConstraints cc = new CellConstraints();
        mMainPanel.add(panel1, cc.xy(3, 1, CellConstraints.DEFAULT, CellConstraints.CENTER));
        mTextTimer1.setEditable(false);
        panel1.add(mTextTimer1, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimer2.setEditable(false);
        panel1.add(mTextTimer2, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimer3.setEditable(false);
        panel1.add(mTextTimer3, cc.xy(3, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimer4.setEditable(false);
        panel1.add(mTextTimer4, cc.xy(3, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimer5.setEditable(false);
        panel1.add(mTextTimer5, cc.xy(3, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimer6.setEditable(false);
        panel1.add(mTextTimer6, cc.xy(3, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimer7.setEditable(false);
        panel1.add(mTextTimer7, cc.xy(3, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimer8.setEditable(false);
        panel1.add(mTextTimer8, cc.xy(3, 15, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimer9.setEditable(false);
        panel1.add(mTextTimer9, cc.xy(3, 17, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimer10.setEditable(false);
        panel1.add(mTextTimer10, cc.xy(3, 19, CellConstraints.FILL, CellConstraints.DEFAULT));
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
        panel1.add(label1, cc.xy(1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label2 = new JLabel();
        label2.setHorizontalAlignment(0);
        label2.setText("2");
        panel1.add(label2, cc.xy(1, 3, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label3 = new JLabel();
        label3.setHorizontalAlignment(0);
        label3.setText("3");
        panel1.add(label3, cc.xy(1, 5, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label4 = new JLabel();
        label4.setHorizontalAlignment(0);
        label4.setText("4");
        panel1.add(label4, cc.xy(1, 7, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label5 = new JLabel();
        label5.setHorizontalAlignment(0);
        label5.setText("5");
        panel1.add(label5, cc.xy(1, 9, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label6 = new JLabel();
        label6.setHorizontalAlignment(0);
        label6.setText("6");
        panel1.add(label6, cc.xy(1, 11, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label7 = new JLabel();
        label7.setHorizontalAlignment(0);
        label7.setText("7");
        panel1.add(label7, cc.xy(1, 13, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label8 = new JLabel();
        label8.setHorizontalAlignment(0);
        label8.setText("8");
        panel1.add(label8, cc.xy(1, 15, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label9 = new JLabel();
        label9.setHorizontalAlignment(0);
        label9.setText("9");
        panel1.add(label9, cc.xy(1, 17, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label10 = new JLabel();
        label10.setHorizontalAlignment(0);
        label10.setText("10");
        panel1.add(label10, cc.xy(1, 19, CellConstraints.DEFAULT, CellConstraints.FILL));
        mTextTimerMills1.setEditable(false);
        panel1.add(mTextTimerMills1, cc.xy(5, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimerMills2.setEditable(false);
        panel1.add(mTextTimerMills2, cc.xy(5, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimerMills3.setEditable(false);
        panel1.add(mTextTimerMills3, cc.xy(5, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimerMills4.setEditable(false);
        panel1.add(mTextTimerMills4, cc.xy(5, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimerMills5.setEditable(false);
        panel1.add(mTextTimerMills5, cc.xy(5, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimerMills6.setEditable(false);
        panel1.add(mTextTimerMills6, cc.xy(5, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimerMills7.setEditable(false);
        panel1.add(mTextTimerMills7, cc.xy(5, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimerMills8.setEditable(false);
        panel1.add(mTextTimerMills8, cc.xy(5, 15, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimerMills9.setEditable(false);
        panel1.add(mTextTimerMills9, cc.xy(5, 17, CellConstraints.FILL, CellConstraints.DEFAULT));
        mTextTimerMills10.setEditable(false);
        panel1.add(mTextTimerMills10, cc.xy(5, 19, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FormLayout("fill:d:grow", "center:max(d;4px):noGrow,top:3dlu:noGrow,top:max(d;20dlu):grow,top:3dlu:noGrow,center:d:grow(0.06)"));
        mMainPanel.add(panel2, cc.xywh(5, 1, 1, 3, CellConstraints.DEFAULT, CellConstraints.FILL));
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
        panel3.setLayout(new FormLayout("fill:d:grow(0.7999999999999999),left:4dlu:noGrow,fill:max(d;4px):grow(0.19999999999999998)", "center:d:grow"));
        mMainPanel.add(panel3, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.FILL));
        mExportCurrentResultsButton = new JButton();
        mExportCurrentResultsButton.setText("Export Current Results");
        panel3.add(mExportCurrentResultsButton, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.FILL));
        mChooseButton = new JButton();
        mChooseButton.setText("Choose");
        panel3.add(mChooseButton, cc.xy(3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
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
