import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Ghost extends Entity {
    Clip deathSound;
    public void spawn(int spawnPoint){
        for (int i = 0; i < SCREEN_WIDTH/UNIT_SIZE; ++i){
            for (int j = 0; j < SCREEN_HEIGHT/UNIT_SIZE; ++j) {
                if(MAP[i][j] == spawnPoint){
                    setX(j*UNIT_SIZE); setY(i*UNIT_SIZE);
                }
            }
        }
    }

    public void move(ArrayList<Ghost> otherGhosts) throws UnsupportedAudioFileException, LineUnavailableException, IOException { }

    // Move Legality Methods

    public void collideWithPacman(Pacman pacman) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        if (getX() == pacman.getX() && getY() == pacman.getY() && !pacman.invincible){
            pacman.die();
        }
    }

    public ArrayList<Ghost> collideWithBombs(ArrayList<Bomb> bombs, ArrayList<Ghost> ghosts, Graphics graphics) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        for (int i = 0; i < bombs.size(); ++i) {
            if (getX() == bombs.get(i).getX() && getY() == bombs.get(i).getY()) {
                graphics.drawImage(bombs.get(0).getEffect().getImage(), this.getX(), this.getY(), 4*UNIT_SIZE, 4*UNIT_SIZE, null);
                //ghosts.remove(this);
                bombs.get(i).getSound().start();
                bombs.get(i).explode(ghosts, graphics);
                bombs.remove(i);
                GamePanel.SCORE+=200;
            }
        }
        return ghosts;
    }

    public Ghost die(Graphics graphics) {
        getDeathSound().setFramePosition(10);
        getDeathSound().start();
        return this;
    }

    //MODIFIED FOR GHOST TILES
    public boolean wallBlock() {
        switch (getDir()) {
            case 'r':
                if (MAP[getY() / UNIT_SIZE][getX() / UNIT_SIZE + 1] != 0 || MAP[getY() / UNIT_SIZE][getX() / UNIT_SIZE + 1] != 5) {
                    return true;
                }
                break;
            case 'l':
                if (MAP[getY() / UNIT_SIZE][getX() / UNIT_SIZE - 1] != 0 || MAP[getY() / UNIT_SIZE][getX() / UNIT_SIZE - 1] != 5) {
                    if (getX() % UNIT_SIZE == 0) {
                        return true;
                    }
                }
                break;
            case 'u':
                if (MAP[getY() / UNIT_SIZE - 1][getX() / UNIT_SIZE] != 0 || MAP[getY() / UNIT_SIZE - 1][getX() / UNIT_SIZE] != 5) {
                    if (getY() % UNIT_SIZE == 0) {
                        return true;
                    }
                }
                break;
            case 'd':
                if (MAP[getY() / UNIT_SIZE + 1][getX() / UNIT_SIZE] != 0 || MAP[getY() / UNIT_SIZE + 1][getX() / UNIT_SIZE] != 5) {
                    return true;
                }
                break;
        }
        return false;
    }

    // CONSTRUCTORS --
    Ghost() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        super();
        File sound = new File("ghost_death.wav");
        AudioInputStream Audio = AudioSystem.getAudioInputStream(sound);
        Clip deathSound = AudioSystem.getClip();
        deathSound.open(Audio);
        setDeathSound(deathSound);
    }

    // GETTERS  --

    public Clip getDeathSound() {
        return deathSound;
    }

    // SETTERS  --

    public void setDeathSound(Clip deathSound) {
        this.deathSound = deathSound;
    }
}
