package couk.jenxsol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
    public static void main(final String[] args)
    {
        JFrame frame = new JFrame("Application");
        frame.setContentPane(new Application().mMainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

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
    private JButton mStartButton;
    private JButton mStopButton;
    private JLabel mTitleControls;
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
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
        mStartButton.addActionListener(this);
        mStopButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(final ActionEvent actionEvent)
    {
        Object item = actionEvent.getSource();
        final long when = actionEvent.getWhen();
        if (item.equals(mStartButton))
        {
            System.out.println("StartButton [" + when + "]");
        }
        if (item.equals(mStopButton))
        {
            System.out.println("StopButton [" + when + "]");
        }
    }

    @Override
    public boolean dispatchKeyEvent(final KeyEvent keyEvent)
    {
        final String numberPressed;
        final long when = keyEvent.getWhen();
        switch (keyEvent.getKeyCode())
        {
            case KeyEvent.VK_1:
                numberPressed = "1";
                break;
            default:
                numberPressed = null;
        }
        System.out.println("Pressed " + KeyEvent.KEY_PRESSED == keyEvent.getID() + "[" + numberPressed + "] [" + when + "]");
        return numberPressed != null;
    }


}
