import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class Entity {
    //STATIC (map) --
    static final int SCREEN_WIDTH = GamePanel.SCREEN_WIDTH;
    static final int SCREEN_HEIGHT = GamePanel.SCREEN_HEIGHT;
    static final int UNIT_SIZE = GamePanel.UNIT_SIZE;
    static final int GAME_UNITS = GamePanel.GAME_UNITS;
    static final int MOVE_FACTOR = GamePanel.MOVE_FACTOR;
    static final int DELAY = GamePanel.DELAY;
    static final int[][] MAP = GamePanel.MAP;

    //VARS
    private int x;
    private int y;
    private char dir;
    private char dirSave;
    private char dirPrev;
    private ImageIcon skin;
    private boolean blocked;
    Clip taunt;
    long clipTime = 0;
    private boolean taunting;

    //METHODS

    //Move Legality Methods
    public boolean wallBlock(char dir){
        switch (dir){
            case 'r' :
                if(MAP[getY()/UNIT_SIZE][getX()/UNIT_SIZE+1] != 0) {
                    return true;
                }
                break;
            case 'l' :
                if(MAP[getY()/UNIT_SIZE][getX()/UNIT_SIZE-1] != 0) {
                    if(getX() % UNIT_SIZE == 0) {
                        return true;
                    }
                }
                break;
            case 'u':
                if(MAP[getY()/UNIT_SIZE-1][getX()/UNIT_SIZE] != 0) {
                    if(getY() % UNIT_SIZE == 0) {
                        return true;
                    }
                }
                break;
            case 'd':
                if(MAP[getY()/UNIT_SIZE+1][getX()/UNIT_SIZE] != 0) {
                    return true;
                }
                break;
        }
        return false;
    }
    public boolean checkIfMovable(){
        if (!illegalX() && !illegalY()){
            return true;
        }
        return false;
    }
    public boolean checkIfBlocked(char dir){
        if (wallBlock(dir)){
            setBlocked(true);
            return true;
        }
        return false;
    }
    public boolean illegalX(){
        if((getX() + MOVE_FACTOR % UNIT_SIZE != 0 && getX() - MOVE_FACTOR % UNIT_SIZE != 0) && (getY() % UNIT_SIZE != 0)) {  /*illegal x axis*/
            //System.out.println("illegal x");
            return true;
        }
        return false;
    }
    public boolean illegalY(){
        if((getY() + MOVE_FACTOR % UNIT_SIZE != 0 && getY() - MOVE_FACTOR % UNIT_SIZE != 0) && (getX() % UNIT_SIZE != 0)) {  /*illegal y axis*/
            //System.out.println("illegal y");
            return true;
        }
        return false;
    }

    public void taunt() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        setTaunting(true);
        getTaunt().start();
    }

    //CONSTRUCTORS --
    Entity() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        setY(UNIT_SIZE);
        setX(UNIT_SIZE);
        setDir('r');
        setDirPrev('l');
        setDirSave('r');
        setSkin(new ImageIcon("asd.png"));
        setTaunting(false);

        // Taunt
        File tauntFile = new File("taunt2.wav");
        AudioInputStream tauntAudioStream = AudioSystem.getAudioInputStream(tauntFile);
        Clip taunt = AudioSystem.getClip();
        taunt.open(tauntAudioStream);
        setTaunt(taunt);
    }

    //GETTERS --
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getDir() {
        return dir;
    }

    public char getDirSave() {
        return dirSave;
    }

    public char getDirPrev() {
        return dirPrev;
    }

    public ImageIcon getSkin() {
        return skin;
    }

    public Clip getTaunt() {
        return taunt;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public boolean getTaunting() {
        return taunting;
    }

    //SETTERS --
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDir(char dir) {
        this.dir = dir;
    }

    public void setDirSave(char dirSave) {
        this.dirSave = dirSave;
    }

    public void setDirPrev(char dirPrev) {
        this.dirPrev = dirPrev;
    }

    public void setSkin(ImageIcon skin) {
        this.skin = skin;
    }

    public void setClipTime(long clipTime) {
        this.clipTime = clipTime;
    }

    public void setTaunt(Clip taunt) {
        this.taunt = taunt;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void setTaunting(boolean taunting) {
        this.taunting = taunting;
    }

}