import javax.sound.sampled.Clip;
import javax.swing.*;

abstract public class Item {
    private int x;
    private int y;
    private ImageIcon skin;
    Clip sound;

    Item(){
        this.x = GamePanel.UNIT_SIZE;
        this.y = GamePanel.UNIT_SIZE;
    }

    Item(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ImageIcon getSkin() {
        return skin;
    }

    public Clip getSound() {
        return sound;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSkin(ImageIcon skin) {
        this.skin = skin;
    }

    public void setSound(Clip sound) {
        this.sound = sound;
    }
}
