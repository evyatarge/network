/*
 * This class represents a Creating of Simple Media Player through
 * JPanel that plays a media file from a URL.
 */

package MediaPlayer;

import java.awt.BorderLayout;
import java.awt.Component;

//import java.io.File;    //for get the file as URL
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import javax.media.Manager;
import javax.media.Player;
import javax.media.CannotRealizeException;
import javax.media.NoPlayerException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author E&E
 */
public class MediaPlayer extends JPanel {

    //Constructor
    public MediaPlayer(URL mediauUrl) {        

        setLayout(new BorderLayout());  //use a BorderLayout
        
        // Use lightweight components for Swing compatibility
        Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, true);
        
        try {
            // create a player to play the media specified in the URL
            Player mediaPlayer = Manager.createRealizedPlayer( mediauUrl );

            // get the components for the video and the playback controls
            Component video = mediaPlayer.getVisualComponent();
            Component control=mediaPlayer.getControlPanelComponent();

            if (video != null) {
                add(video, BorderLayout.CENTER);          // place the video component in the panel
            }
            
            if (control != null) {
                add(control, BorderLayout.SOUTH);         // place the control component in  the panel
            }
            
            mediaPlayer.start(); // start playing the media clip

        } catch (NoPlayerException noPlayerException) {
            System.err.println("No media player found: " + noPlayerException);
        } catch (CannotRealizeException cannotRealizeException ) {
            System.err.println( "Could not realize media player: " + cannotRealizeException );
        } catch (IOException iOException) {
            System.err.println( "Error reading from the source: " + iOException);
        }

    }
    
    public static void main(String[] args) {

        // create a file chooser
        JFileChooser fileChooser = new JFileChooser();

        // show open file dialog
        fileChooser.showOpenDialog(null);

        URL mediaUrl=null;

        try {
            // get the chosen file as URL
            mediaUrl = fileChooser.getSelectedFile().toURI().toURL();
            
            /* ==== for get the file as URL ====
            // get the file
            File testFile = new File("pirates.mpeg");       

            // get the file as URL
            mediaUrl = testFile.toURI().toURL();
            */
        } catch (MalformedURLException malformedURLException) {
            System.err.println( "Could not create URL for the file: " + malformedURLException );
        }
        
        if ( mediaUrl != null ) { // only display if there is a valid URL
            
            JFrame mediaTest = new JFrame( "Simple Media Player" );

            mediaTest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            MediaPlayer mediaPanel = new MediaPlayer(mediaUrl);

            mediaTest.add(mediaPanel);

            mediaTest.setSize(400, 400); // set the size of the player

            //mediaTest.setLocationRelativeTo(null);

            mediaTest.setVisible(true);
        }
        
    }
    
}