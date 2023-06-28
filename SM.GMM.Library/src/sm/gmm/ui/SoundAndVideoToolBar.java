/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.ui;

import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JToolBar;
import sm.gmm.internalframes.InternalFrameVideo;
import sm.gmm.listeners.IFunctionToFire;

/**
 * Class for sound management and related tasks.
 * @author Gonzalo
 */
public class SoundAndVideoToolBar 
{    
    /**
     * Array of the video button names.
     */
    protected static final String[] VIDEO_BUTTONS = 
    {
        "camara",
        "capturar"
    };
    
    /**
     * Array of Strings with the video extensions allowed.
     */
    protected static final String[] VIDEO_FILE_EXTENSIONS =
    {
        "mp4",
        "mpg",
        "avi"
    };
    
    /**
     * Array of String with the sound button names.
     */
    public static final String[] SOUND_BUTTONS =
    {
        "play24x24",
        "stop24x24",
        "record24x24",
        "pausa24x24"
    };    
        
    /**
     * toolBar to contain all the sound and video components.
     */
    public JToolBar toolBar;
    /**
     * Button to manage play action
     */
    protected Button playButton = null;
    /**
     * Button to manage stop action
     */
    public Button stopButton = null;
    /**
     * Button to manage record action
     */
    public Button recordButton = null;
    /**
     * Button to manage pause action
     */
    public Button pauseButton = null;
    /**
     * Property to manage all the sounds tasks.
     */
    private SoundManager soundManager = null;
    /**
     * Property to reference MainWindow JDesktopPane
     */
    private DesktopPane desktop;
    
    /**
     * Constructor to initialize SoundsManager object class.
     */
    public SoundAndVideoToolBar(DesktopPane d)
    {
        super();
        this.initToolbar();
        desktop = d;
        soundManager = new SoundManager(this);
        initVideoButtons();
    }
    /**
     * Method that initialize the toolbar component.
     */
    private void initToolbar()
    {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setVisible(true);
    }
    
    /**
     * Method to create all the button actions {play, stop, record and pause}.
     * @return an Array of IFunctionToFire[] to be added to the click event.
     */
    public IFunctionToFire[] getButtonFuncs()
    {
        IFunctionToFire play = (Object o) ->
        {
            JInternalFrame internalFrame = desktop.getSelectedFrame();
            if (internalFrame instanceof InternalFrameVideo)
            {
                InternalFrameVideo internalFrameVideo = (InternalFrameVideo)internalFrame;
                stopButton.setEnabled(true);
                recordButton.setEnabled(false);
                switchPlayPauseButton(false);
                internalFrameVideo.play();
            }
            else
            {
                recordButton.setEnabled(soundManager.playSoundFile());
            }
        };  
        
        IFunctionToFire stop = (Object o) ->
        {
            JInternalFrame internalFrame = desktop.getSelectedFrame();
            if (internalFrame instanceof InternalFrameVideo)
            {
                InternalFrameVideo internalFrameVideo = (InternalFrameVideo)internalFrame;
                if ((Button)toolBar.getComponent(2) == pauseButton)
                {
                    switchPlayPauseButton(false);                
                }
                
                internalFrameVideo.stop();
            }
            else
            {
                soundManager.stop();
            }
        };
        
        IFunctionToFire record = (Object o) ->
        {
            playButton.setEnabled(soundManager.startRecording());
        };
        
        IFunctionToFire pause = (Object) ->
        {
            JInternalFrame internalFrame = desktop.getSelectedFrame();
            if (internalFrame instanceof InternalFrameVideo)
            {
                InternalFrameVideo internalFrameVideo = (InternalFrameVideo)internalFrame;
                recordButton.setEnabled(true);
                switchPlayPauseButton(false);
                internalFrameVideo.pause();
            }
            else
            {
                soundManager.pause();                
            }
        };
        
        return new IFunctionToFire[]
        {
            play,
            stop,
            record,
            pause
        };
    }

    /**
     * Method to switch between playButton and pauseButton in the ToolBar.
     * @param initState Boolean flag to indicate if set to initial state with the play button, false to keep the common flow.
     */
    public void switchPlayPauseButton(Boolean initState) 
    {
        Button b = initState ? pauseButton : (Button)toolBar.getComponent(2);
        toolBar.remove(b);
        
        if(b == playButton)
        {
            toolBar.add(pauseButton, 2);
        }
        else
        {
            toolBar.add(playButton, 2);
        }
        
        toolBar.repaint();
    }

    /**
     * Method to initialize the sound buttons.
     */
    protected void createSoundButtons() 
    {
        IFunctionToFire[] buttonFuncs = this.getButtonFuncs();
        ImageIcon[] buttonIcons = ComponentsManager.getImageIcons(SOUND_BUTTONS);

        playButton = ComponentsManager.createButton(buttonIcons[0], buttonFuncs[0], "play");
        stopButton = ComponentsManager.createButton(buttonIcons[1], buttonFuncs[1], "stop");
        stopButton.setEnabled(false);
        recordButton = ComponentsManager.createButton(buttonIcons[2], buttonFuncs[2], "record");
        pauseButton = ComponentsManager.createButton(buttonIcons[3], buttonFuncs[3], "pause");
        
        toolBar.add(playButton);
        toolBar.add(stopButton);
        toolBar.add(recordButton);
    }

    /**
     * Method to add a file to the soundFile list
     * @param f The file to be added.
     */
    public void addFile(File f)
    {
        soundManager.addFile(f);
        switchPlayPauseButton(true);
    }
    
    /**
     * Method to check if a file has one of the extensions passed as parameter.
     * @param f The file to be checked.
     * @param extensions The list of extensions.
     * @return True if the file {f} has one of the extensions of {extensions}, False otherwise.
     */
    private static Boolean checkIfFileEndsWithExtension(File f, String[] extensions)
    {
        for (String extension : extensions)
        {
            if (f.getName().endsWith("."+extension))
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Method to check if a file is a sound file.
     * @param f The file to be checked.
     * @return True if the file f has one of the extensions in SOUNDS_FILE_EXTENSIONS, false otherwise.
     */
    public static Boolean isSoundExtensionFile(File f)
    {
        return SoundAndVideoToolBar.checkIfFileEndsWithExtension(f, SoundManager.SOUND_FILE_EXTENSIONS);
    }
    
    /**
     * Method to check if a file is a video file.
     * @param f The file to be checked.
     * @return True if the file f has one of the extensions in VIDEO_FILE_EXTENSIONS, false otherwise.
     */
    public static boolean isVideoExtension(File f) 
    {
       return SoundAndVideoToolBar.checkIfFileEndsWithExtension(f, VIDEO_FILE_EXTENSIONS);
    }
    /**
     * Method to get the video button functions.
     * @return An array of IFunctionToFire with every video button function.
     */
    private IFunctionToFire[] videoButtonFuncs()
    {
        IFunctionToFire webcam = (Object o) ->
        {
            desktop.createInternalFrameCamera();
        };
        
        IFunctionToFire snapshot = (Object o) ->
        {
            desktop.createInternalFrameSnapshot();
        };

        
        IFunctionToFire[] funcs =
        {
            webcam,
            snapshot
        };

        return funcs;
    }
    /**
     * Method to init the video buttons and include it in the toolbar.
     */
    private void initVideoButtons()
    {
        ComponentsManager.createButtonGroup(VIDEO_BUTTONS, toolBar, videoButtonFuncs(), ComponentsManager.getImageIcons(VIDEO_BUTTONS));
    }
    
}
