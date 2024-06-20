import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class CharacterMethods {
    public static ArrayList<Ghost> spawnGhosts() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        ArrayList<Ghost> newGhosts = new ArrayList<>();
        for(int i = 0; i < 2; ++i){
            newGhosts.add(new Gajdy());
        }

        int spawnIndex = 6;
        for (int i = 0; i < newGhosts.size(); ++i){
            newGhosts.get(i).spawn(spawnIndex++);
        }
        return newGhosts;
    }
}
