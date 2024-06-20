import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class PowerUp extends Item {

    public static void placePowerUps(ArrayList<PowerUp> powerUps, ArrayList<Coordinate> goodTiles){
        Random random = new Random();
        int index;
        for (int i = 0; i < powerUps.size(); ++i) {
            index = random.nextInt(goodTiles.size());
            powerUps.get(i).setX(goodTiles.get(index).getX());
            powerUps.get(i).setY(goodTiles.get(index).getY());
        }
    }

    PowerUp() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        super();
        setSkin(new ImageIcon("SKINS\\ITEMS\\powerup.png"));
        File power = new File("powerup.wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(power);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        setSound(clip);
    }
}
