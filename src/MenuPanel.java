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

public class MenuPanel extends JPanel implements ActionListener {

    int mTime = 0;
    JButton start;
    JButton hardcore;
    JButton score;
    ImageIcon background;
    boolean hmOn = false;

    MenuPanel() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        start = new JButton();
        start.setBounds(70,80,310,100);
        start.addActionListener(this);
        this.add(start);
        start.setOpaque(false);
        start.setContentAreaFilled(false);
        start.setBorderPainted(false);

        hardcore = new JButton();
        hardcore.setBounds(70,290,350,120);
        hardcore.addActionListener(this);
        this.add(hardcore);
        hardcore.setOpaque(false);
        hardcore.setContentAreaFilled(false);
        hardcore.setBorderPainted(false);

        score = new JButton();
        score.setBounds(50,540,340,110);
        score.addActionListener(this);
        this.add(score);
        score.setOpaque(false);
        score.setContentAreaFilled(false);
        score.setBorderPainted(false);

        background = new ImageIcon("SKINS\\MENU_e.png");
        this.setPreferredSize(new Dimension(GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT));
        this.setFocusable(true);
        this.setVisible(true);
        this.setLayout(null);
    }

    public void draw(Graphics g) throws IOException, FontFormatException {
        if (hmOn) {
            background = new ImageIcon("SKINS\\MENU_e_H.png");
        }
        else background = background = new ImageIcon("SKINS\\MENU_e.png");
        Font font = Font.createFont(Font.TRUETYPE_FONT, new File("FONTS\\Shoguns Clan.ttf"));
        g.setFont(font);
        g.setFont(g.getFont().deriveFont(30f));

        g.drawImage(background.getImage(), 0, 0,GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT+100, null);
        System.out.println(GameFrame.STATE);
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
        if (e.getSource() == start) {
            System.out.println("to skins");
            GameFrame.STATE = 1;
        }
        else if (e.getSource() == hardcore) {
            if (!hmOn) {
                mTime = GameFrame.music.getFramePosition();
                GameFrame.music.stop();
                GameFrame.music_h.setFramePosition(mTime);
                GameFrame.music_h.start();
                GamePanel.HARDCORE = true;
                hmOn = true;
                System.out.println("ON");
                System.out.println(GamePanel.HARDCORE);
            }
            else {
                mTime = GameFrame.music_h.getFramePosition();
                GameFrame.music_h.stop();
                GameFrame.music.setFramePosition(mTime);
                GameFrame.music.start();
                GamePanel.HARDCORE = false;
                hmOn = false;
                System.out.println("OFF");
                System.out.println(GamePanel.HARDCORE);
            }
        }
        else if (e.getSource() == score) {
            System.out.println("score");
            GameFrame.STATE = 4;
        }
        repaint();
    }
}
