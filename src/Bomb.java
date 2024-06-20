import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Bomb extends Item {
    ImageIcon effect;
    int radius = 4 * GamePanel.UNIT_SIZE;

    public int explode(ArrayList<Ghost> ghosts, Graphics graphics){
        int kills = 0;
        for (int i = 0; i < ghosts.size(); ++i) {
            if (Math.abs(this.getX() - ghosts.get(i).getX()) <= radius && Math.abs(this.getY() - ghosts.get(i).getY()) <= radius) {
                ImageIcon img = new ImageIcon("SKINS\\ITEMS\\bomb_ex_effect.png");
                graphics.drawImage(img.getImage(), ghosts.get(i).getX()-100, ghosts.get(i).getY()-100, 200, 200, null);
                ghosts.remove(ghosts.get(i).die(graphics));
                kills++;
            }
        }
        System.out.println("killed : "+kills);
        return kills;
    }

    // CONSTRUCTORS --
    Bomb() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        super();
        ImageIcon img = new ImageIcon("SKINS\\ITEMS\\bomb.png");
        setSkin(img);
        ImageIcon eff = new ImageIcon("SKINS\\ITEMS\\bomb_effect.png");
        setEffect(eff);
        File exp = new File("SKINS\\ITEMS\\explosion.wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(exp);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        setSound(clip);
    }

    // SETTERS  --
    public void setEffect(ImageIcon effect) {
        this.effect = effect;
    }

    // GETTERS  --
    public ImageIcon getEffect() {
        return effect;
    }
}
