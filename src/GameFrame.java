import javax.naming.ldap.UnsolicitedNotification;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Time;

public class GameFrame extends JFrame implements ActionListener {
    static Clip music;
    static Clip music_h;
    static int STATE = 0;
    GamePanel gamePanel;
    SkinPanel skinPanel;
    MenuPanel menuPanel;
    ScorePanel scorePanel;
    static Timer timer;

    public void startTheme(){
        music.start();
    }
    public void stopTheme(){
        music.stop();
    }

    GameFrame() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        File file = new File("menu.wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        music = clip;
        music.start();

        File file2 = new File("menu_H.wav");
        AudioInputStream audioStream2 = AudioSystem.getAudioInputStream(file2);
        Clip clip2 = AudioSystem.getClip();
        clip2.open(audioStream2);
        music_h = clip2;

        // Game Panel
        int offset = GamePanel.UNIT_SIZE;
        menuPanel = new MenuPanel();
        this.add(menuPanel);
        //gamePanel = new GamePanel();
        //this.add(gamePanel);
        //skinPanel = new SkinPanel();
        //this.add(skinPanel);
        //scorePanel = new ScorePanel();
        //this.add(scorePanel);

        this.setTitle("Goldline Miami™ II");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(GamePanel.SCREEN_WIDTH+15, GamePanel.SCREEN_HEIGHT+133);
        //this.pack();

        // Logical sets
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        // Extra
        ImageIcon image = new ImageIcon("C:\\UNI\\Code\\Pacman\\SKINS\\PACMAN\\VANILLA\\DOWN\\pacman_vanilla_down.png");
        this.setIconImage(image.getImage());

        timer = new Timer(100, this);
        timer.start();
    }

    public void switchToGamePanel() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this.remove(skinPanel);
        gamePanel = new GamePanel();
        this.add(gamePanel);
        gamePanel.requestFocus();
        this.revalidate();
        timer.stop();
    }

    public void switchToSkinPanel() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this.remove(menuPanel);
        skinPanel = new SkinPanel();
        this.add(skinPanel);
        skinPanel.requestFocus();
        this.revalidate();
        timer.stop();
    }

    public void switchBackToMenuPanel() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this.remove(scorePanel);
        menuPanel = new MenuPanel();
        this.add(menuPanel);
        menuPanel.requestFocus();
        this.revalidate();
        timer.stop();
    }

    public void switchToScorePanel() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this.remove(menuPanel);
        scorePanel = new ScorePanel();
        this.add(scorePanel);
        scorePanel.requestFocus();
        this.revalidate();
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (STATE){
            case 0:
                break;
            case 1:
                try {
                    switchToSkinPanel();
                } catch (UnsupportedAudioFileException ex) {
                    throw new RuntimeException(ex);
                } catch (LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case 2:
                try {
                    switchToGamePanel();
                } catch (UnsupportedAudioFileException ex) {
                    throw new RuntimeException(ex);
                } catch (LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case 3:
                try {
                    switchBackToMenuPanel();
                    STATE = 0;
                } catch (UnsupportedAudioFileException ex) {
                    throw new RuntimeException(ex);
                } catch (LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                timer.start();
                break;
            case 4:
                try {
                    switchToScorePanel();
                } catch (UnsupportedAudioFileException ex) {
                    throw new RuntimeException(ex);
                } catch (LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
        }
    }
}
