import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class Pacman extends Entity{
    static long TAUNT_TIME = System.currentTimeMillis()/100;
    int lives = 3;
    ArrayList<Bomb> bombs;
    boolean invincible = false;
    double time = 0;
    Clip theme;

    // METHODS  --
    public void tauntMethod(){
        float maxScore = 3;
        if(getTaunting()){
            if(GamePanel.SCORE_GAIN + time/1000 <= maxScore){
                time = System.currentTimeMillis()/100-TAUNT_TIME;
                GamePanel.SCORE_GAIN += time/1000;
            } else GamePanel.SCORE_GAIN = maxScore;
        }
        else if(!(GamePanel.SCORE_GAIN - time/4000 < 1)){
            GamePanel.SCORE_GAIN -= time/8000;
        }
        else GamePanel.SCORE_GAIN = 1;
    }

    public void move() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        // Taunt Multiplier
        tauntMethod();

        // Move Legality
        if (getDirSave() != 'n') {
            if (checkIfMovable() && !checkIfBlocked(getDirSave())) {
                setDir(getDirSave());
                setDirSave('n');
            }
        }

        // Move
        switch (getDir()){
            case 'd' :
                getTaunt().setMicrosecondPosition(0);
                setTaunting(false);
                getTaunt().stop();
                getTheme().start();
                if (!wallBlock('d'))
                    setY(getY() + MOVE_FACTOR);
                break;
            case 'l':
                getTaunt().setMicrosecondPosition(0);
                setTaunting(false);
                getTaunt().stop();
                getTheme().start();
                if (!wallBlock('l'))
                    setX(getX() - MOVE_FACTOR);
                break;
            case 'u':
                getTaunt().setMicrosecondPosition(0);
                getTaunt().stop();
                setTaunting(false);
                getTheme().start();
                if (!wallBlock('u'))
                    setY(getY() - MOVE_FACTOR);
                break;
            case 'r':
                getTaunt().setMicrosecondPosition(0);
                getTaunt().stop();
                setTaunting(false);
                getTheme().start();
                if (!wallBlock('r'))
                    setX(getX() + MOVE_FACTOR);
                break;
            case 't':
                getTaunt().start();
                taunt();
                break;
            case 'n':
                break;
        }
        //System.out.println("---");
    }
    public void die() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File("die.wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
        lives--;
        setX(12*UNIT_SIZE);
        setY(9*UNIT_SIZE);
        setDir('d');
        setDirSave('n');
        setDirPrev('n');
        GamePanel.RUNNING = false;
    }

    public void taunt() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        setTaunting(true);
        //getTheme().stop();
    }

    // CONSTRUCTORS --
    Pacman() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        super();
        if (GamePanel.HARDCORE) {
            lives = 1;
         }
        setX(12*UNIT_SIZE);
        setY(7*UNIT_SIZE);
        // Theme
        File file = new File("help.wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        setTheme(clip);
        // Skin
        ImageIcon img = new ImageIcon("pacman_vanilla.png");
        setSkin(img);
        // Bombs
        bombs = new ArrayList<>();
        bombs.add(new Bomb());
        // test
    }

    // SETTERS  --
    public void setTheme(Clip theme) {
        this.theme = theme;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public Bomb placeBomb() {
        Bomb placedBomb = bombs.remove(bombs.size()-1);
        placedBomb.setX(getX());
        placedBomb.setY(getY());
        return placedBomb;
    }

    public void addBomb() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        bombs.add(new Bomb());
    }

    @Override
    public void setTaunting(boolean taunting) {
        super.setTaunting(taunting);
        if(getTaunting() == false){
            //time = 0;
            TAUNT_TIME = System.currentTimeMillis()/100;
        }

    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    // GETTERS  --
    public Clip getTheme() {
        return theme;
    }

    public int getLives() {
        return lives;
    }

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }

    public boolean isInvincible() {
        return invincible;
    }
}
