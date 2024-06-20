import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Gajdy extends Ghost{
    public void moveLegality(ArrayList<Ghost> otherGhosts){
        if(checkIfBlocked(getDir()) && checkIfMovable()){
            char newDir = randomDirection();
            while (checkIfBlocked(newDir) || checkIf180(newDir)){
                newDir = randomDirection();
            }
            setDir(newDir);
        }
    }
    public boolean checkIf180(char newDir){
        if (newDir == 'r' && getDir() == 'l'){
            return true;
        }
        else if (newDir == 'l' && getDir() == 'r'){
            return true;
        }
        else if (newDir == 'u' && getDir() == 'd'){
            return true;
        }
        else if (newDir == 'd' && getDir() == 'u'){
            return true;
        }
        else
        return false;
    }
    public void move(ArrayList<Ghost> otherGhosts) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        // Move Logic
        moveLegality(otherGhosts);
        // Move
        switch (getDir()){
            case 'd' :
                setTaunting(false);
                setY(getY() + MOVE_FACTOR);
                break;
            case 'l':
                setTaunting(false);
                setX(getX() - MOVE_FACTOR);
                break;
            case 'u':
                setTaunting(false);
                setY(getY() - MOVE_FACTOR);
                break;
            case 'r':
                setTaunting(false);
                setX(getX() + MOVE_FACTOR);
                break;
            case 't':
                taunt();
                break;
            case 'n':
                break;
        }
    }
    @Override
    public boolean checkIfBlocked(char dir){
        if (wallBlock(dir)){
            return true;
        }
        return false;
    }

    public char randomDirection(){
        Random random = new Random();
        int randInt = random.nextInt(4);
        char direction = 'd';

        switch (randInt) {
            case 0:
                direction = 'r';
                break;
            case 1:
                direction = 'l';
                break;
            case 2:
                direction = 'u';
                break;
            case 3:
                direction = 'd';
                break;
        }
        return direction;
    }


    // CONSTRUCTORS --
    Gajdy() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        ImageIcon gajdySkin = new ImageIcon("SKINS\\GHOSTS\\Gajdy.PNG");
        setDir('d');
        setSkin(gajdySkin);
    }
}
