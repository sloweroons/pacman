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
import java.util.ArrayList;
import java.util.EventListener;

public class ScorePanel extends JPanel implements ActionListener {
    JButton back;
    ImageIcon background;
    ArrayList<Double> scores;

    ScorePanel() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        back = new JButton();
        back.setBounds(140,550,350,180);
        back.addActionListener(this);
        this.add(back);
        back.setOpaque(false);
        back.setContentAreaFilled(false);
        back.setBorderPainted(false);

        background = new ImageIcon("SKINS\\MENU_SCORE.png");
        this.setPreferredSize(new Dimension(GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT));
        this.setFocusable(true);
        this.setVisible(true);
        this.setLayout(null);
    }

    public void draw(Graphics g) throws IOException, FontFormatException {
        g.drawImage(background.getImage(), 0, 0,GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT+100, null);
        scores = new ArrayList<>();
        scores.addAll(Score.readScores());
        Font font = Font.createFont(Font.TRUETYPE_FONT, new File("FONTS\\Shoguns Clan.ttf"));
        g.setFont(font);
        g.setColor(Color.black);
        g.setFont(g.getFont().deriveFont(40f));
        for (int i = 0; i < scores.size(); ++i) {
            //System.out.println(i+1+"\t"+Double.toString(scores.get(i)));

        }
        for (int i = 0; i < scores.size(); ++i) {
            String score = i+1+"   "+Double.toString(scores.get(i));
            g.drawString(score, 250, 200+40*i);
        }
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
        if (e.getSource() == back) {
            System.out.println("back");
            GameFrame.STATE = 3;
            GameFrame.timer.start();
        }
        repaint();
    }
}
