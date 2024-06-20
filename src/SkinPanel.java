import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.EventListener;

public class SkinPanel extends JPanel implements ActionListener {
    int mTime = 0;
    JButton dog;
    JButton vanilla;
    ImageIcon background;

    SkinPanel() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        dog = new JButton();
        dog.setBounds(100,150,310,100);
        dog.addActionListener(this);
        this.add(dog);
        dog.setOpaque(false);
        dog.setContentAreaFilled(false);
        dog.setBorderPainted(false);

        vanilla = new JButton();
        vanilla.setBounds(240,440,350,120);
        vanilla.addActionListener(this);
        this.add(vanilla);
        vanilla.setOpaque(false);
        vanilla.setContentAreaFilled(false);
        vanilla.setBorderPainted(false);

        background = new ImageIcon("SKINS\\MENU_SKIN.png");
        this.setPreferredSize(new Dimension(GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT));
        this.setFocusable(true);
        this.setVisible(true);
        this.setLayout(null);
    }

    public void draw(Graphics g) throws IOException, FontFormatException {
        g.drawImage(background.getImage(), 0, 0,GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT+100, null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            draw(g);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public class CustomKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent event) {
            switch (event.getKeyCode()) {

            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == dog) {
            GamePanel.VANILLA = false;
            System.out.println("dog");
            GameFrame.timer.start();
            GameFrame.STATE = 2;
        }
        else if (e.getSource() == vanilla) {
            System.out.println("vanilla");
            GameFrame.timer.start();
            GameFrame.STATE = 2;
        }
    }
}
