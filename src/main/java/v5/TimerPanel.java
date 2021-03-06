package v5;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/** This is the Timer panel.
 *
 * The timer panel is used to set a timer.
 * Giving the panel some Hours, some Minutes,
 * and some Seconds, it will display the panel
 * when the timer reaches '00:00:00'.
 *
 * @author michael ball
 * @version 2.5
 */
public class TimerPanel extends JPanel implements IClockPanel, Runnable
{
    private GridBagLayout layout;
    private GridBagConstraints constraints;
    private JTextField jtextField1; // Hour textField
    private JTextField jtextField2; // Min textField
    private JTextField jtextField3; // Second textField
    private JButton timerButton;
    private JButton resetButton;
    private Clock clock;
    private Thread countdownThread = new Thread(this::performCountDown);

    public TimerPanel(Clock clock)
    {
        setClock(clock);
        setSize(Clock.defaultSize);
        setGridBagLayout(new GridBagLayout());
        setLayout(getGridBagLayout());
        setGridBagConstraints(new GridBagConstraints());
        setBackground(Color.WHITE); // Color.BLACK
        setForeground(Color.WHITE);
        setBorder(new LineBorder(Color.BLACK));
        setupTimerPanel();
        setupTimerButton();
        setupResetButton();
        updateLabels();
        addComponentsToPanel();
    }

    public GridBagLayout getGridBagLayout() { return this.layout; }
    public GridBagConstraints getGridBagConstraints() { return this.constraints; }
    public Clock getClock() { return this.clock; }
    public JTextField getJTextField1() { return this.jtextField1; }
    public JTextField getJTextField2() { return this.jtextField2; }
    public JTextField getJTextField3() { return this.jtextField3; }
    public JButton getTimerButton() { return this.timerButton; }
    public JButton getResetButton() { return this.resetButton; }

    protected void setGridBagLayout(GridBagLayout layout) { this.layout = layout; }
    protected void setGridBagConstraints(GridBagConstraints constraints) { this.constraints = constraints; }
    protected void setClock(Clock clock) { this.clock = clock; }
    protected void setJTextField1(JTextField jtextField1) { this.jtextField1 = jtextField1; }
    protected void setJTextField2(JTextField jtextField2) { this.jtextField2 = jtextField2; }
    protected void setJTextField3(JTextField jtextField3) { this.jtextField3 = jtextField3; }
    protected void setTimerButton(JButton timerButton) { this.timerButton = timerButton; }
    protected void setResetButton(JButton resetButton) { this.resetButton = resetButton; }

    public void setupTimerPanel()
    {
        setJTextField1(new JTextField("Hour", 4));
        setJTextField2(new JTextField("Min", 4));
        setJTextField3(new JTextField("Sec", 4));
        getJTextField1().setSize(new Dimension(50, 50));
        getJTextField2().setSize(new Dimension(50, 50));
        getJTextField3().setSize(new Dimension(50, 50));
        getJTextField1().addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent e)
            {
                if (!validateFirstTextField())
                {
                    getJTextField1().setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                if (StringUtils.isBlank(getJTextField1().getText()) ||
                    StringUtils.isEmpty(getJTextField1().getText()))
                {
                    getJTextField1().setText("Hour");
                }
                if (NumberUtils.isNumber(getJTextField1().getText()))
                {
                    if (Integer.parseInt(getJTextField1().getText()) < 24 &&
                        Integer.parseInt(getJTextField1().getText()) >= 0)
                    {
                        getTimerButton().setText("Set");
                        getTimerButton().repaint();
                        getTimerButton().updateUI();
                    }
                    if (Integer.parseInt(getJTextField1().getText()) >= 24 ||
                        Integer.parseInt(getJTextField1().getText()) < 0)
                    {
                        getTimerButton().setText("Hour between 0 and 24");
                        getTimerButton().repaint();
                        getTimerButton().updateUI();
                        getJTextField1().grabFocus();
                    }
                }
                if (validateFirstTextField() && validateSecondTextField() && validateThirdTextField())
                {
                    getTimerButton().setEnabled(true);
                }
                else if (validateFirstTextField() && !validateSecondTextField())
                {
                    getJTextField2().grabFocus();
                    getTimerButton().setEnabled(false);
                }
                else if (validateFirstTextField() && validateSecondTextField() && !validateThirdTextField())
                {
                    getJTextField3().grabFocus();
                    getTimerButton().setEnabled(false);
                }
        }
        });
        getJTextField2().addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent e)
            {
                if (!validateSecondTextField())
                {
                    getJTextField2().setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                if (StringUtils.isBlank(getJTextField2().getText()) ||
                    StringUtils.isEmpty(getJTextField2().getText()))
                {
                    getJTextField2().setText("Min");
                }
                if (NumberUtils.isNumber(getJTextField2().getText()))
                {
                    if (Integer.parseInt(getJTextField2().getText()) < 60 &&
                        Integer.parseInt(getJTextField2().getText()) >= 0)
                    {
                        getTimerButton().setText("Set");
                        getTimerButton().repaint();
                        getTimerButton().updateUI();
                    }
                    if (Integer.parseInt(getJTextField2().getText()) >= 60 ||
                        Integer.parseInt(getJTextField2().getText()) < 0)
                    {
                        getTimerButton().setText("Min between 0 and 60");
                        getTimerButton().repaint();
                        getTimerButton().updateUI();
                        getJTextField2().grabFocus();
                    }
                }
                if (validateFirstTextField() && validateSecondTextField() && validateThirdTextField())
                {
                    getTimerButton().setEnabled(true);
                }
                else if (!validateFirstTextField())
                {
                    getJTextField1().grabFocus();
                    getTimerButton().setEnabled(false);
                }
            }
        });
        getJTextField3().addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent e)
            {
                if (!validateThirdTextField())
                {
                    getJTextField3().setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                if (StringUtils.isBlank(getJTextField3().getText()) ||
                    StringUtils.isEmpty(getJTextField3().getText()))
                {
                    getJTextField3().setText("Sec");
                }
                if (NumberUtils.isNumber(getJTextField3().getText()))
                {
                    if (Integer.parseInt(getJTextField3().getText()) < 60 &&
                        Integer.parseInt(getJTextField3().getText()) >= 0)
                    {
                        getTimerButton().setText("Set");
                        getTimerButton().repaint();
                        getTimerButton().updateUI();
                    }
                    if (Integer.parseInt(getJTextField3().getText()) >= 60 ||
                        Integer.parseInt(getJTextField3().getText()) < 0)
                    {
                        getTimerButton().setText("Sec between 0 and 60");
                        getTimerButton().repaint();
                        getTimerButton().updateUI();
                        getJTextField3().grabFocus();
                    }
                }
                if (validateFirstTextField() && validateSecondTextField() && validateThirdTextField())
                {
                    getTimerButton().setEnabled(true);
                }
                else if (!validateFirstTextField())
                {
                    getJTextField1().grabFocus();
                    getTimerButton().setEnabled(false);
                }
                else if (!validateSecondTextField())
                {
                    getJTextField2().grabFocus();
                    getTimerButton().setEnabled(false);
                }
            }

        });
        getJTextField1().setBorder(new LineBorder(Color.WHITE));
        getJTextField2().setBorder(new LineBorder(Color.WHITE));
        getJTextField3().setBorder(new LineBorder(Color.WHITE));
        getJTextField1().setHorizontalAlignment(JTextField.CENTER);
        getJTextField2().setHorizontalAlignment(JTextField.CENTER);
        getJTextField3().setHorizontalAlignment(JTextField.CENTER);
        setTimerButton(new JButton("Set"));
        getTimerButton().setFont(Clock.font20);
        getTimerButton().setOpaque(true);
        getTimerButton().setBackground(Color.BLACK);
        getTimerButton().setForeground(Color.WHITE);
        getTimerButton().setBorder(new LineBorder(Color.WHITE));
        getTimerButton().setEnabled(false);
        setResetButton(new JButton("Reset"));
        getResetButton().setFont(Clock.font20);
        getResetButton().setOpaque(true);
        getResetButton().setBackground(Color.BLACK);
        getResetButton().setForeground(Color.WHITE);
        getResetButton().setBorder(new LineBorder(Color.WHITE));
        setBackground(Color.BLACK);
    }
    protected void setupTimerButton()
    {
        getTimerButton().addActionListener(action ->  this.run());
    }
    protected void setupResetButton()
    {
        getResetButton().addActionListener(this::resetTimerFields);
    }
    public boolean validateFirstTextField()
    {
        if (StringUtils.equals(getJTextField1().getText(), "Hour"))
        {
            return false;
        }
        if (!NumberUtils.isNumber(getJTextField1().getText()))
        {
            return false;
        }
        // cannot be more than 23 hours or less than 0
        return Integer.parseInt(getJTextField1().getText()) < 24 &&
                Integer.parseInt(getJTextField1().getText()) >= 0;
    }
    public boolean validateSecondTextField()
    {
        if (StringUtils.equals(getJTextField2().getText(), "Min"))
        {
            return false;
        }
        if (!NumberUtils.isNumber(getJTextField2().getText()))
        {
            return false;
        }
        // 70 minutes given
        return Integer.parseInt(getJTextField2().getText()) < 60 &&
                Integer.parseInt(getJTextField2().getText()) >= 0;
    }
    public boolean validateThirdTextField()
    {
        if (StringUtils.equals(getJTextField3().getText(), "Sec"))
        {
            return false;
        }
        if (!NumberUtils.isNumber(getJTextField3().getText()))
        {
            return false;
        }
        // 70 seconds given
        return Integer.parseInt(getJTextField3().getText()) < 60 &&
                Integer.parseInt(getJTextField3().getText()) >= 0;
    }
    public void startTimer()
    {
        getTimerButton().setText("Pause Timer");
        getTimerButton().repaint();
        getTimerButton().updateUI();
        //System.err.println(countdownThread.getName() + " is in state: " + countdownThread.getState());
        if (countdownThread.getState() == Thread.State.NEW)
        {
            countdownThread.start(); // calls the run method of this class
            //setTimerHasConcluded(false);
            getClock().setTimerGoingOff(false);
        }
        else if (countdownThread.getState() == Thread.State.TERMINATED)
        {
            countdownThread = new Thread(this::performCountDown);
            countdownThread.start();
        }
        else if (countdownThread.getState() == Thread.State.TIMED_WAITING)
        {
            countdownThread.notify();
        }
    }
    public void pauseTimer() throws InterruptedException
    {
        getTimerButton().setText("Resume Timer");
        getTimerButton().repaint();
        getTimerButton().updateUI();
        countdownThread.interrupt();
    }
    public void performCountDown()
    {
        try
        {
            // get total number of seconds to count down
            //int totalSeconds = Integer.parseInt(getJTextField3().getText());
            //totalSeconds += (Integer.parseInt(getJTextField2().getText()) * 60);
            //totalSeconds += (Integer.parseInt(getJTextField1().getText()) * (60*60));
            // reduce the total, keeping hours, minutes, and seconds current
            //for(int i = totalSeconds; i > 0; i--)
            while (Integer.parseInt(getJTextField3().getText()) > 0 ||
                    Integer.parseInt(getJTextField2().getText()) > 0 ||
                    Integer.parseInt(getJTextField1().getText()) > 0 )
            {
                if (Integer.parseInt(getJTextField3().getText()) >= 0)
                {
                    int seconds = Integer.parseInt(getJTextField3().getText()) - 1;
                    getJTextField3().setText(Integer.toString(seconds));
                    // check hours and minutes, to see if they now need to be decreased
                    if (Integer.parseInt(getJTextField3().getText()) < 0 && Integer.parseInt(getJTextField2().getText()) >= 0)
                    {
                        getJTextField3().setText(Integer.toString(59));
                        int minutes = Integer.parseInt(getJTextField2().getText()) - 1;
                        getJTextField2().setText(Integer.toString(minutes));
                        if (Integer.parseInt(getJTextField2().getText()) < 0 && Integer.parseInt(getJTextField1().getText()) > 0)
                        {
                            getJTextField2().setText(Integer.toString(59));
                            int hours = Integer.parseInt(getJTextField1().getText()) - 1;
                            getJTextField1().setText(Integer.toString(hours));
                        }
                    }
                }
                Thread.sleep(1000);
            }
            // when done counting, set Set button back to Set
            getTimerButton().setText("Set");
            getTimerButton().setEnabled(false);
            //setTimerHasConcluded(true);
            getClock().setTimerGoingOff(true);
        }
        catch (Exception e)
        {
            printStackTrace(e);
        }
    }
    public void resetTimerFields(ActionEvent action)
    {
        getJTextField1().setText("Hour");
        getJTextField2().setText("Min");
        getJTextField3().setText("Sec");
        getTimerButton().setText("Set");
        getTimerButton().setEnabled(false);
        //countdownThread = new Thread(this::performCountDown);
    }
    public void printStackTrace(Exception e, String message)
    {
        if (null != message)
            System.err.println(message);
        else
            System.err.println(e.getMessage());
        for(StackTraceElement ste : e.getStackTrace())
        {
            System.out.println(ste.toString());
        }
    }
    protected void printStackTrace(Exception e)
    { printStackTrace(e, ""); }
    public void addComponent(Component cpt, int gridy, int gridx, double gwidth, double gheight, int ipadx, int ipady, int fill)
    {
        getGridBagConstraints().gridx = gridx;
        getGridBagConstraints().gridy = gridy;
        getGridBagConstraints().gridwidth = (int)Math.ceil(gwidth);
        getGridBagConstraints().gridheight = (int)Math.ceil(gheight);
        getGridBagConstraints().fill = fill;
        getGridBagConstraints().ipadx = ipadx;
        getGridBagConstraints().ipady = ipady;
        getGridBagConstraints().fill = fill;
        getGridBagConstraints().weightx = 0;
        getGridBagConstraints().weighty = 0;
        getGridBagConstraints().insets = new Insets(0,0,0,0);
        getGridBagLayout().setConstraints(cpt, getGridBagConstraints());
        add(cpt);
    }
    public void checkIfTimerHasConcluded()
    {
        if (getClock().isTimerGoingOff()) {
            getClock().changeToTimerPanel();
        }
    }
    @Override
    public void addComponentsToPanel()
    {
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        addComponent(getJTextField1(), 0,0,1,1,0,0, GridBagConstraints.HORIZONTAL); // TextField 1
        addComponent(getJTextField2(), 0,1,1,1,0,0, GridBagConstraints.HORIZONTAL); // TextField 2
        addComponent(getJTextField3(), 0,2,1,1,0,0, GridBagConstraints.HORIZONTAL); // TextField 3
        addComponent(getResetButton(), 1, 0, 3, 1, 0, 0, GridBagConstraints.HORIZONTAL); // Reset Button
        addComponent(getTimerButton(), 2,0,3,1,0,0, GridBagConstraints.HORIZONTAL); // Set Timer Button
    }
    @Override
    public void updateLabels()
    {
        getJTextField1().grabFocus();
        getClock().repaint();
    }
    @Override
    public void run() // called when we hit the Start Timer button
    {
        try
        {
            if (StringUtils.equals(getTimerButton().getText(), "Set") ||
                StringUtils.equals(getTimerButton().getText(), "Resume Timer"))
            {
                startTimer();
            }
            else
            {
                pauseTimer();
            }
        }
        catch (InterruptedException e)
        {
            printStackTrace(e);
        }
    }
}
