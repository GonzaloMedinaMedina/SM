/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.ui;

import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import sm.sound.SMClipPlayer;
import sm.sound.SMSoundRecorder;

/**
 * Class to Manage the sound functionality of the SoundAndVideoToolBar
 * @author Gonzalo
 */
public class SoundManager 
{     
    /**
     * Class for Time elapsed count using Threads.
     */
    class TimeElapsedCounterTask implements Runnable
    {
        /**
         * Flag to determine if the Thread must keep executing.
         */
        private volatile boolean running = true;
        /**
         * Variable to count ms.
         */
        private volatile BigDecimal count = null;
        /**
        * Total count time int secs.
        */
        private volatile int countLimit = 0;
        /**
         * Decimal formatter object to display the number as desired.
         */
        private volatile DecimalFormat decimalFormatter = null;
        /**
         * Label to be updated.
         */
        private volatile JLabel labelToBeUpdated = null;
        
        /**
         * Constructor with the second limit counter as parameter.
         * @param cl The total seconds of the sound record.
         */        
        public TimeElapsedCounterTask(int cl, JLabel ltbu)
        {
            count = new BigDecimal(0.1);
            decimalFormatter = new DecimalFormat("#00.00");
            countLimit = cl;
            labelToBeUpdated = ltbu;
            updateLabel(0);
        }
        
        /**
         * Method to update labels with current count.
         * @param currentSeconds The current count played in seconds of the sound record.
         */ 
        private void updateLabel(double currentSeconds)
        {
            String formattedElapsedTime = decimalFormatter.format(currentSeconds);
            String formattedTotalTime = decimalFormatter.format(countLimit);
            String stringToShow = formattedElapsedTime;
            
            if (countLimit != -1)
            {
                stringToShow = formattedElapsedTime + " / " + formattedTotalTime;
            }

            labelToBeUpdated.setText(stringToShow);
        }
        
        /**
         * Method that starts the Thread execution.
         */
        @Override
        public void run() 
        {
            final BigDecimal fixedIncrement = new BigDecimal(0.1);
            running = true;

            while(running)
            {
                try
                {
                    count = count.add(fixedIncrement);
                    Thread.sleep(100);
                    
                    if (count.intValue() % 1 == 0)
                    {
                        updateLabel(count.intValue());
                    }
                    
                    if (!running)
                    {
                        synchronized (this) 
                        {
                            wait();
                        }
                    }
                } catch(Exception e)
                {
                    System.err.println(e.getMessage());
                }
            }
        }
        
        /**
         * Method to resume Thread execution.
         */
        public void resume()
        {
            running = true;
            synchronized(this)
            {
                notify();
            }
        }

        /**
         * Method to check if the countLimit have been reached.
         * @return 
         */
        private boolean countFinished() 
        {
            if (countLimit == -1)
            {
                return true;
            }

            if ((count.doubleValue() >= countLimit))
            {
                updateLabel(countLimit);
                return true;
            }
            return false;
        }
    }
    
    /**
     * Class to manage all the SMClipPlayer object actions.
     */
    class SoundAdapter implements LineListener 
    {
        /**
         * Runnable object that defines the Thread task.
         */
        TimeElapsedCounterTask timeElapsedCounterTask = null;
        /**
         * Label to be updated during Thread execution.
         */
        private JLabel labelToUpdate = null;

        /**
          * Total count time int secs.
          */
        private int totalCountTime = -1;
        /**
         * Constructor with the label to be updated as parameter.
         */
        public SoundAdapter(JLabel ltu)
        {
            labelToUpdate = ltu;
            totalCountTime = -1;
        }
        
        /**
         * Init the thread with the specific Runnable object task.
         */
        private void initCounterThread()
        {
            if (timeElapsedCounterTask == null)
            {
                timeElapsedCounterTask = new TimeElapsedCounterTask(totalCountTime, labelToUpdate);
                soundFileThreadCounter = new Thread(timeElapsedCounterTask); 
                soundFileThreadCounter.start();                                
            }
            else
            {
                timeElapsedCounterTask.resume();
            }
        }
        
        /**
         * Implementation of LineLinester update method.
         * @param event The LiveEvent object.
         */
        @Override
        public void update(LineEvent event) 
        {
            if (event.getType() == LineEvent.Type.START) 
            {
                if (player != null && player.getClip().isRunning())
                {
                   totalCountTime = (int)(player.getClip().getMicrosecondLength() / 1000000.0);
                }
                
                soundFilesList.setEnabled(false);                
                parentToolBar.stopButton.setEnabled(true);

                parentToolBar.switchPlayPauseButton(false);
                initCounterThread();
            }
            if (event.getType() == LineEvent.Type.STOP) 
            {
                parentToolBar.switchPlayPauseButton(false);
                soundFilesList.setEnabled(true);
                parentToolBar.stopButton.setEnabled(true);
                
                if (timeElapsedCounterTask != null)
                {
                    timeElapsedCounterTask.running = false;
                    if (timeElapsedCounterTask.countFinished())
                    {
                        parentToolBar.playButton.setEnabled(true);
                        parentToolBar.recordButton.setEnabled(true);
                        timeElapsedCounterTask = null;
                    }
                }
            }
            if (event.getType() == LineEvent.Type.CLOSE) 
            {
                parentToolBar.playButton.setEnabled(true);
                parentToolBar.recordButton.setEnabled(true);
                soundFilesList.setEnabled(true);
                labelToUpdate.setText("00.00 / 00.00");
            }
        }
    }
    
    /**
     * Constructor to initialize SoundsManager object class.
     * @param parent The SoundAndVideoToolBar UI parent component.
     */
    public SoundManager(SoundAndVideoToolBar parent)
    {
        super();
        parentToolBar = parent;
        soundFileTime = new JLabel();
        soundFileTime.setToolTipText("Tiempo de reproducción.");
        recordingTime = new JLabel();
        recordingTime.setToolTipText("Tiempo de grabación.");
        this.initSoundsToolBar();
    }

    /**
     * Reference to the parent.
     */
    private SoundAndVideoToolBar parentToolBar;

    /**
     * Variable to performs operations on a sound file.
     */
    private SMClipPlayer player = null;
    /**
     * Variable to performs operation to record sounds.
     */
    private SMSoundRecorder recorder = null;

    /**
     * Array of Strings with the sound extensions allowed.
     */    
    protected static final String[] SOUND_FILE_EXTENSIONS =
    {
        "au", 
        "aiff", 
        "aifc", 
        "snd",
        "wav"
    };

    /**
     * Thread object to count the elapsed time of the current sound file.
     */
    private Thread soundFileThreadCounter = null;

    /**
     * List with all the sound files available.
     */
    public JComboBox<File> soundFilesList;
    /**
     * Total duration time and played time of the current loaded sound file.
     */
    private final JLabel soundFileTime;
    /**
     * 
     */
    private final JLabel recordingTime;
    
    /**
     * Method to initialize the soundFilesList JComboBox property.
     */
    private void initSoundFileList()
    {
        soundFilesList = new JComboBox<File>();
        soundFilesList.setMaximumSize(new java.awt.Dimension(150,100));
        soundFilesList.setToolTipText("Archivos de audio a reproducir.");
        soundFilesList.addItemListener(new ItemListener() 
        {
            @Override
            public void itemStateChanged(ItemEvent evt) 
            {
            }
        });
    }
    
    /**
     * Method to initialize all the UI components of the sound ToolBar.
     */
    public void initSoundsToolBar() 
    {
        initSoundFileList();
        parentToolBar.toolBar.add(soundFilesList);

        soundFileTime.setText("00.00 / 00.00");
        parentToolBar.toolBar.add(soundFileTime);
        
        parentToolBar.createSoundButtons();

        recordingTime.setText("00.00");
        parentToolBar.toolBar.add(recordingTime);
    }
    /**
     * Method to check if a sound file is already included in the soundFilesList JComboBox property.
     * @param f The sound file to be checked.
     * @return True if the file is already included, false otherwise.
     */
    public Boolean contains(File f)
    {
        for (Integer i = 0; i< this.soundFilesList.getItemCount(); i++)
        {
            if (this.soundFilesList.getItemAt(i).getName().equals(f.getName()))
            {
                return true;
            }
        }
        
        return false;
    }
    /**
     * Method to add a new sound file to the soundFilesList JComboBox property.
     * @param f The file to be added.
     */
    public void addFile(File f) 
    {
        if (this.contains(f))
        {
            return;                    
        }
        
        File newFile = new File(f.getAbsolutePath())
        {
            @Override
            public String toString()
            {
                return this.getName();
            }
        };

        this.soundFilesList.addItem(newFile);
        this.soundFilesList.setSelectedItem(newFile);
    }   
    /**
     * Method to play the current file sound selected.
     * @return True if the player started or resume the sound file, False otherwise.
     */
    public Boolean playSoundFile() 
    {
        File f = (File) soundFilesList.getSelectedItem();
        if (f != null)
        {
            if (player == null)
            {
                player = new SMClipPlayer(f);

                if (player != null)
                {
                    player.addLineListener(new SoundManager.SoundAdapter(soundFileTime));
                    player.play();
                }
            }
            else
            {
                player.resume();
            }
            
            return true;
        }

        return false;
    }
    /**
     * Method to stop the player or recorder.
     */
    public void stop() 
    {
        if (player != null)
        {
            player.stop();
            player = null;
        }
        if (recorder != null)
        {
            recorder.stop();
            recorder = null;
        }
    }
    /**
     * Method to start recording
     * @return True if the recorder starts correctly, False otherwise.
     */
    public Boolean startRecording()
    {
        JFileChooser dlg = new JFileChooser();
        Container parent = soundFilesList.getParent();
        while (parent != null)
        {
            parent = parent.getParent();
        }

        int resp = dlg.showOpenDialog(parent);
        if (resp == JFileChooser.APPROVE_OPTION) 
        {
            File f = dlg.getSelectedFile();
            try
            {
                recorder = new SMSoundRecorder(f);
                if (recorder != null)
                {
                    recorder.addLineListener(new SoundManager.SoundAdapter(recordingTime));
                    recorder.record();
                    return true;
                }
            }
            catch (Exception e)
            {
                System.err.println(e.getMessage());
            }
        }   
        
        return false;
    }
    /**
     * Method to pause the current player.
     */
    void pause() 
    {
        if (player != null)
        {
            player.pause();
        }
    }
}
