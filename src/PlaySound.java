import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

import static javax.sound.sampled.Clip.LOOP_CONTINUOUSLY;

public class PlaySound {
    public PlaySound(String fileSource) {
        try {
            File file = new File(fileSource);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip loopClip = AudioSystem.getClip();
            loopClip.setFramePosition(0);
            loopClip.open(audioStream);
            loopClip.loop(0);
            loopClip.start();


        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
}
