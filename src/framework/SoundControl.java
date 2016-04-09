package framework;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * new class for future sound management
 * 
 * @author danny
 *        
 */
class SoundControl {
    private boolean on = false;
    
    private MediaPlayer mediaPlayer;
    private Media sound;
    
    public SoundControl() {
        sound = new Media(new File("src/resources/sounds/tristram.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
    }
    
    private void update() {
        if (on) {
            mediaPlayer.setAutoPlay(true);
        }
    }
    
    /**
     * @return the on
     */
    public boolean isOn() {
        return on;
    }
    
    /**
     * @param on
     *            the on to set
     */
    public void setOn(boolean on) {
        this.on = on;
        update();
    }
}
