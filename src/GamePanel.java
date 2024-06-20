import javax.sound.sampled.*;
import javax.swing.*;
import javax.xml.crypto.dom.DOMCryptoContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GamePanel extends JPanel implements ActionListener {
    // PLAYER SELECTED
    static boolean HARDCORE;
    boolean endGame = false;
    long diffStart = System.currentTimeMillis();
    ImageObserver test;
    Graphics2D g2;
    BufferedImage screenEffect;
    Clip paused;
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int MOVE_FACTOR = UNIT_SIZE/5;
    static final int DELAY = 15;
    static double SCORE = 0;
    static double SCORE_GAIN = 1;
    static float FRAME_COUNTER;
    static int MOVE_COUNTER = 0;
    static int DIFFICULTY = 0;
    static boolean RUNNING = false;
    static boolean VANILLA = true;
    int maxDifCounter = 0;
    boolean scoreCap = false;
    float scoreSize = 50f;
    float depthOpacity = 0;
    float depthOpacityRed = 0;

    //SKIN INDEXES
    int tauntIndex = 0;

    static final int[][] MAP = {
        {1,1,1,1,1,1, 1,1,1,1,1,1, 1,1,1,1,1,1, 1,1,1,1,1,1},
        {1,0,0,0,0,0, 0,1,0,0,0,0, 0,0,0,0,1,0, 0,0,0,0,0,1},
        {1,0,1,2,1,1, 0,1,0,1,2,1, 1,2,1,0,1,0, 1,1,1,1,0,1},
        {1,0,1,1,1,1, 0,1,0,1,5,1, 1,5,1,0,1,0, 1,1,2,1,0,1},
        {1,0,1,0,0,0, 0,1,0,1,6,1, 1,7,1,0,1,0, 0,0,0,1,0,1},
        {1,0,1,0,1,1, 1,1,0,1,0,1, 1,0,1,0,1,1, 1,1,0,1,0,1},   //6
        {1,0,1,0,1,1, 2,1,0,1,0,1, 1,0,1,0,1,2, 1,1,0,1,0,1},
        {1,0,2,0,0,0, 0,0,0,0,0,0, 0,0,0,0,0,0, 0,0,0,1,0,1},
        {1,0,1,1,1,1, 1,1,0,2,1,1, 1,1,2,0,1,1, 1,1,1,1,0,1},
        {1,0,0,0,0,0, 0,0,0,0,0,0, 0,0,0,0,0,0, 0,0,0,0,0,1},
        {1,0,1,0,1,1, 1,0,1,1,0,1, 1,0,1,1,0,1, 1,1,0,1,0,1},
        {1,0,1,0,1,1, 1,0,2,1,0,1, 1,0,1,1,0,1, 1,2,0,1,0,1},   //12
        {1,0,1,0,1,0, 0,0,0,0,0,1, 1,0,0,0,0,0, 0,1,0,1,0,1},
        {1,0,1,0,1,0, 1,1,1,1,0,1, 1,0,1,1,1,1, 0,1,0,1,0,1},
        {1,0,2,0,1,0, 0,0,0,0,0,1, 1,0,0,0,0,0, 0,1,0,1,0,1},
        {1,0,1,0,1,1, 1,0,1,0,0,0, 0,0,0,1,0,1, 1,1,0,1,0,1},
        {1,0,1,0,1,1, 1,0,1,0,1,1, 1,1,0,1,0,1, 1,1,0,1,0,1},
        {1,0,0,0,0,0, 0,0,1,0,2,1, 1,2,0,1,0,0, 0,0,0,0,0,1},   //18
        {1,0,1,0,1,1, 1,0,1,0,1,1, 1,1,0,1,0,1, 2,1,0,1,0,1},
        {1,0,1,0,1,1, 1,0,0,0,0,1, 1,0,0,0,0,1, 1,1,0,1,0,1},
        {1,0,1,0,0,0, 0,0,0,0,0,1, 1,0,0,0,0,0, 0,0,0,1,0,1},
        {1,0,1,1,2,1, 1,1,2,1,0,1, 1,0,1,1,2,1, 1,1,1,1,0,1},
        {1,0,0,0,0,0, 0,0,0,0,0,1, 1,0,0,0,0,0, 0,0,0,0,0,1},
        {1,1,1,1,1,1, 1,1,1,1,1,1, 1,1,1,1,1,1, 1,1,1,1,1,1}    //24
    };
    //Items
    ArrayList<ImageIcon> torches;
    ArrayList<ImageIcon> taunt;
    ArrayList<PowerUp> powerUps;
    ArrayList<Pellet> pellets;
    ArrayList<Bomb> liveBombs;
    ArrayList<Ghost> ghosts;
    ArrayList<Coordinate> goodTiles;
    ArrayList<Double> scores;
    Pacman pacman = new Pacman();
    Timer timer;
    boolean ended = false;

    public void test(){

    }
    GamePanel() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new CustomKeyAdapter());

        screenEffect = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        g2 = (Graphics2D)screenEffect.getGraphics();

        File file = new File("pause.wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        paused = clip;

        this.beginGame();
    }
    public void beginGame() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        // Tests
        test();

        GameFrame.music.stop();
        GameFrame.music_h.stop();

        // Score
        scores = new ArrayList<>();
        scores.addAll(Score.readScores());
        for(int i = 0; i < scores.size(); ++i) {
            System.out.println(scores.get(i));
        }

        // Torches
        torches = new ArrayList<>();
        torches.add(new ImageIcon("torch_0.jpg"));
        torches.add(new ImageIcon("torch_1.jpg"));
        torches.add(new ImageIcon("torch_2.jpg"));
        torches.add(new ImageIcon("torch_3.jpg"));
        torches.add(new ImageIcon("torch_4.jpg"));

        // Pellets
        pellets = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < SCREEN_WIDTH/UNIT_SIZE; ++i){
            for (int j = 0; j < SCREEN_HEIGHT/UNIT_SIZE; ++j) {
                if(MAP[i][j] == 0){
                    pellets.add(0, new Pellet(i, j));
                    index++;
                }
            }
        }

        // Power-Ups
        powerUps = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            powerUps.add(new PowerUp());
        }
        PowerUp.placePowerUps(powerUps, listGoodTiles());

        // Bombs
        liveBombs = new ArrayList<>();

        // Taunt Skin
        taunt = new ArrayList<>();
        taunt.add(new ImageIcon("SKINS\\PACMAN\\VANILLA\\TAUNT\\pacman_taunt1.png"));
        taunt.add(new ImageIcon("SKINS\\PACMAN\\VANILLA\\TAUNT\\pacman_taunt2.png"));
        taunt.add(new ImageIcon("SKINS\\PACMAN\\VANILLA\\TAUNT\\pacman_taunt3.png"));
        taunt.add(new ImageIcon("SKINS\\PACMAN\\VANILLA\\TAUNT\\pacman_taunt4.png"));
        taunt.add(new ImageIcon("SKINS\\PACMAN\\VANILLA\\TAUNT\\pacman_taunt5.png"));
        taunt.add(new ImageIcon("SKINS\\PACMAN\\VANILLA\\TAUNT\\pacman_taunt6.png"));

        // Ghosts
        ghosts = new ArrayList<>();
        ghosts.addAll(CharacterMethods.spawnGhosts());

        // GAME
        RUNNING = true;
        FRAME_COUNTER = 0;
        timer = new Timer(DELAY, this);
        timer.start();
        pacman.getTheme().start();
    }

    public ArrayList<Coordinate> listGoodTiles(){
        int index = 0;
        ArrayList<Coordinate> goodTiles = new ArrayList<>();
        for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++){
            for (int j = 0; j < SCREEN_WIDTH/UNIT_SIZE; j++) {
                if(MAP[j][i] == 0){
                    goodTiles.add(index, new Coordinate(i, j));
                    index++;
                }
            }
        }
        return goodTiles;
    }

    public void draw(Graphics graphics) throws UnsupportedAudioFileException, IOException, LineUnavailableException, FontFormatException {
        MOVE_COUNTER++;
        ImageIcon torch = new ImageIcon("torch_effect.png");
        int offset = UNIT_SIZE*4;
        Random random = new Random();
        int boundBG = 200;
        int BGCR = random.nextInt(boundBG);
        int BGCB = random.nextInt(boundBG);

        switch (DIFFICULTY){
            case 0:
                graphics.setColor(Color.BLACK);
                break;
            case 1:
                graphics.setColor(new Color(boundBG/3-BGCR/3, 0, boundBG/5-BGCB/5));
                break;
            case 2:
                graphics.setColor(new Color(boundBG-50-BGCR/3, 0, boundBG/7-BGCB/7));
                break;
        }
        graphics.fillRect(0,0,1000, 1000);

        for (int i = 0; i < SCREEN_WIDTH/UNIT_SIZE; ++i){
            for (int j = 0; j < SCREEN_HEIGHT/UNIT_SIZE; ++j){
                switch (MAP[i][j]) {
                    case 0 :
                        ImageIcon imgFloor = new ImageIcon("scenery_wood.jpg");
                        graphics.drawImage(imgFloor.getImage(), j*UNIT_SIZE, i*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE, test);
                        for (int k = 0; k < pellets.size(); ++k) {
                            if (i == pellets.get(k).getX() && j == pellets.get(k).getY()){
                                ImageIcon pellet = new ImageIcon("pellet.png");
                                if(pellets.get(k).isAlive())
                                    graphics.drawImage(pellet.getImage(), j*UNIT_SIZE+7, i*UNIT_SIZE+7, UNIT_SIZE/2, UNIT_SIZE/2, test);
                            }
                        }
                        break;
                    case 1 :
                        ImageIcon imgWall = new ImageIcon("scenery_stone2.jpg");
                        graphics.drawImage(imgWall.getImage(), j*UNIT_SIZE, i*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE, test);
                        break;
                    case 2 :
                        graphics.drawImage(torches.get(Math.round(FRAME_COUNTER)).getImage(), j*UNIT_SIZE, i*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE, test);
                        graphics.drawImage(torch.getImage(), j*UNIT_SIZE, i*UNIT_SIZE, offset, offset, test);
                        break;
                    case 5 :    //Ghost Path
                        break;
                    case 6:     //Ghost Spawn
                        break;
                }
            }
        }
        // PowerUps
        for (int p = 0; p < powerUps.size(); ++p) {
            graphics.drawImage(powerUps.get(p).getSkin().getImage(), powerUps.get(p).getX()*UNIT_SIZE, powerUps.get(p).getY()*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE, test);
            ImageIcon pwrEff = new ImageIcon("SKINS\\ITEMS\\powerup_effect.png");
            graphics.drawImage(pwrEff.getImage(), powerUps.get(p).getX()*UNIT_SIZE, powerUps.get(p).getY()*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE, test);
        }

        // Ghosts
        for(int i = 0; i < ghosts.size(); ++i){
            graphics.drawImage(ghosts.get(i).getSkin().getImage(), ghosts.get(i).getX(), ghosts.get(i).getY(), UNIT_SIZE, UNIT_SIZE, test);
        }

        // Pacman
        if(tauntIndex == 5) tauntIndex = 0;
        else tauntIndex++;
        if(pacman.getTaunting()) {
            Random rand = new Random();
            int offsetX = rand.nextInt(40)-20 ; int offsetY = rand.nextInt(40)-20;
            graphics.drawImage(taunt.get(tauntIndex).getImage(), pacman.getX(), pacman.getY(), UNIT_SIZE, UNIT_SIZE, test);
            graphics.drawImage(torch.getImage(), (pacman.getX()-4*UNIT_SIZE)+offsetY, (pacman.getY()-4*UNIT_SIZE)+offsetX, 10*UNIT_SIZE, 10*UNIT_SIZE, test);
            graphics.drawImage(torch.getImage(), (pacman.getX()-4*UNIT_SIZE)+offsetX, (pacman.getY()-4*UNIT_SIZE)+offsetY, 10*UNIT_SIZE, 10*UNIT_SIZE, test);
        } else if (VANILLA) {
            switch (pacman.getDir()) {
                case 'r':
                    if (Math.round(FRAME_COUNTER) == 2) {
                        ImageIcon img = new ImageIcon("SKINS\\PACMAN\\VANILLA\\RIGHT\\pacman_vanilla.png");
                        pacman.setSkin(img);
                        graphics.drawImage(pacman.getSkin().getImage(), pacman.getX(), pacman.getY(), UNIT_SIZE, UNIT_SIZE, test);
                        graphics.drawImage(torch.getImage(), pacman.getX() - 32, pacman.getY() - 33, offset, offset, test);
                    } else {
                        ImageIcon img = new ImageIcon("SKINS\\PACMAN\\VANILLA\\RIGHT\\pacman_vanilla2.png");
                        pacman.setSkin(img);
                        graphics.drawImage(pacman.getSkin().getImage(), pacman.getX(), pacman.getY(), UNIT_SIZE, UNIT_SIZE, test);
                        graphics.drawImage(torch.getImage(), pacman.getX() - 30, pacman.getY() - 30, offset, offset, test);
                    }
                    break;
                case 'l':
                    if (Math.round(FRAME_COUNTER) == 2) {
                        ImageIcon img = new ImageIcon("SKINS\\PACMAN\\VANILLA\\LEFT\\pacman_vanilla_left.png");
                        pacman.setSkin(img);
                        graphics.drawImage(pacman.getSkin().getImage(), pacman.getX(), pacman.getY(), UNIT_SIZE, UNIT_SIZE, test);
                        graphics.drawImage(torch.getImage(), pacman.getX() - 32, pacman.getY() - 33, offset, offset, test);
                    } else {
                        ImageIcon img = new ImageIcon("SKINS\\PACMAN\\VANILLA\\LEFT\\pacman_vanilla2_left.png");
                        pacman.setSkin(img);
                        graphics.drawImage(pacman.getSkin().getImage(), pacman.getX(), pacman.getY(), UNIT_SIZE, UNIT_SIZE, test);
                        graphics.drawImage(torch.getImage(), pacman.getX() - 30, pacman.getY() - 30, offset, offset, test);
                    }
                    break;
                case 'u':
                    if (Math.round(FRAME_COUNTER) == 2) {
                        ImageIcon img = new ImageIcon("SKINS\\PACMAN\\VANILLA\\UP\\pacman_vanilla_up.png");
                        pacman.setSkin(img);
                        graphics.drawImage(pacman.getSkin().getImage(), pacman.getX(), pacman.getY(), UNIT_SIZE, UNIT_SIZE, test);
                        graphics.drawImage(torch.getImage(), pacman.getX() - 32, pacman.getY() - 33, offset, offset, test);
                    } else {
                        ImageIcon img = new ImageIcon("SKINS\\PACMAN\\VANILLA\\UP\\pacman_vanilla2_up.png");
                        pacman.setSkin(img);
                        graphics.drawImage(pacman.getSkin().getImage(), pacman.getX(), pacman.getY(), UNIT_SIZE, UNIT_SIZE, test);
                        graphics.drawImage(torch.getImage(), pacman.getX() - 30, pacman.getY() - 30, offset, offset, test);
                    }
                    break;
                case 'd':
                    if (Math.round(FRAME_COUNTER) == 2) {
                        ImageIcon img = new ImageIcon("SKINS\\PACMAN\\VANILLA\\DOWN\\pacman_vanilla_down.png");
                        pacman.setSkin(img);
                        graphics.drawImage(pacman.getSkin().getImage(), pacman.getX(), pacman.getY(), UNIT_SIZE, UNIT_SIZE, test);
                        graphics.drawImage(torch.getImage(), pacman.getX() - 32, pacman.getY() - 33, offset, offset, test);
                    } else {
                        ImageIcon img = new ImageIcon("SKINS\\PACMAN\\VANILLA\\DOWN\\pacman_vanilla2_down.png");
                        pacman.setSkin(img);
                        graphics.drawImage(pacman.getSkin().getImage(), pacman.getX(), pacman.getY(), UNIT_SIZE, UNIT_SIZE, test);
                        graphics.drawImage(torch.getImage(), pacman.getX() - 30, pacman.getY() - 30, offset, offset, test);
                    }
                    break;
            }
        }
        else {
            ImageIcon img = new ImageIcon("SKINS\\PACMAN\\DOG\\dog.png");
            pacman.setSkin(img);
            graphics.drawImage(pacman.getSkin().getImage(), pacman.getX(), pacman.getY(), UNIT_SIZE, UNIT_SIZE, test);
            graphics.drawImage(torch.getImage(), pacman.getX() - 30, pacman.getY() - 30, offset, offset, test);
        }

        // Powerup pickup
        for (int i = 0; i < powerUps.size(); i++) {
            if(pacman.getX()/UNIT_SIZE == powerUps.get(i).getX() && pacman.getY()/UNIT_SIZE == powerUps.get(i).getY()) {
                powerUps.get(i).getSound().start();
                powerUps.remove(i);
                pacman.addBomb();
                SCORE+=100;
            }
        }

        // Pellet pickup
        File pellet = new File("pellet_sound.wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(pellet);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);

        for(int i = 0; i < pellets.size(); ++i){
            if(pacman.getX()/UNIT_SIZE == pellets.get(i).getY() && pacman.getY()/UNIT_SIZE == pellets.get(i).getX()) {
                pellets.remove(i);
                SCORE+=10*(float)Math.ceil(Math.round(SCORE_GAIN));
                clip.start();
            }
        }
        // Current bombs
        for (int i = 0; i < 4; ++i) {
            ImageIcon bombBG = new ImageIcon("SKINS\\ITEMS\\bomb_dark.png");
            graphics.drawImage(bombBG.getImage(), SCREEN_WIDTH-(i+1)*4*UNIT_SIZE-UNIT_SIZE, 21*UNIT_SIZE+10, 4*UNIT_SIZE, 4*UNIT_SIZE, test);
        }
        for (int i = 0; i < pacman.getBombs().size(); ++i) {
            if (i >= 5) {
                break;
            }
            graphics.drawImage(pacman.getBombs().get(i).getSkin().getImage(), SCREEN_WIDTH-(i+1)*4*UNIT_SIZE-UNIT_SIZE, 21*UNIT_SIZE+10, 4*UNIT_SIZE, 4*UNIT_SIZE, test);
        }

        // Bombs placed
        Font font = Font.createFont(Font.TRUETYPE_FONT, new File("FONTS/Shoguns Clan.ttf"));
        graphics.setFont(font);
        graphics.setFont(graphics.getFont().deriveFont(40f));
        String boom = "MEOW...";
        for (int i = 0; i < liveBombs.size(); ++i) {
            int offsetX = random.nextInt(30)-15;
            int offsetY = random.nextInt(30)-15;
            graphics.drawImage(liveBombs.get(i).getSkin().getImage(), liveBombs.get(i).getX()+offsetX/4, liveBombs.get(i).getY()+offsetY/4, UNIT_SIZE, UNIT_SIZE, test);
            graphics.drawImage(liveBombs.get(i).getEffect().getImage(), (liveBombs.get(i).getX()+offsetX/2-2*UNIT_SIZE)+8, (liveBombs.get(i).getY()-offsetY/2-2*UNIT_SIZE)+8, 4*UNIT_SIZE, 4*UNIT_SIZE, test);
            graphics.drawString(boom,(liveBombs.get(i).getX()+offsetX/2-2*UNIT_SIZE), (liveBombs.get(i).getY()-offsetY/2));
        }

        // Bombs explode
        for (int i = 0; i < ghosts.size(); ++i) {
            ghosts = ghosts.get(i).collideWithBombs(liveBombs, ghosts, graphics);
        }

        // Score
        if(Math.round(FRAME_COUNTER) == 3) FRAME_COUNTER = 0;
        Font font1 = Font.createFont(Font.TRUETYPE_FONT, new File("FONTS/WIDEAWAKE.TTF"));
        graphics.setFont(font1);
        String score = SCORE+""; String gain = "x"+Math.round(SCORE_GAIN); String diff = DIFFICULTY+1+"";
        float gainSize = 30f+10*(float)Math.ceil(Math.round(SCORE_GAIN));
        int scoreCM = 0; int gainCM = 0;

        scoreCM += SCORE/30;
        if(Math.ceil(Math.round(SCORE_GAIN)) != 1) {
            gainCM += SCORE_GAIN*80;
        }
        float lastScoreSize = scoreSize;
        if(scoreCM > 255) {
            scoreCM = 255;
            gainCM = 255;
            scoreCap = true;
        }
        if(SCORE > 500 && !scoreCap) {
            scoreSize = 42 + (int) (scoreSize + SCORE) / 70;
            lastScoreSize = scoreSize;
        }
        // Shadow
        float size = 60+(DIFFICULTY*60);
        Random rand = new Random(); //instance of random class
        int upperbound = 20; int diffOffsetX = 1; int diffOffsetY = 1;
        graphics.setColor(Color.BLACK);
        //generate random values
        switch (DIFFICULTY) {
            case 0 :
                graphics.setFont(graphics.getFont().deriveFont(size));
                graphics.drawString(diff, 21*UNIT_SIZE+10, SCREEN_HEIGHT + 1 * UNIT_SIZE + 10);
                graphics.setFont(graphics.getFont().deriveFont(gainSize+10f));
                graphics.drawString(gain, 16*UNIT_SIZE+10, SCREEN_HEIGHT + 1 * UNIT_SIZE);
                graphics.setFont(graphics.getFont().deriveFont(scoreSize+10f));
                graphics.drawString(score, UNIT_SIZE+10, SCREEN_HEIGHT + 1 * UNIT_SIZE);
                break;
            case 1 :
                diffOffsetX = rand.nextInt(upperbound)-10;
                diffOffsetY = rand.nextInt(upperbound)-10;
                graphics.setFont(graphics.getFont().deriveFont(gainSize+10f));
                graphics.drawString(gain, 16*UNIT_SIZE+10+5*diffOffsetX, SCREEN_HEIGHT + 1 * UNIT_SIZE +2*diffOffsetY);
                graphics.setFont(graphics.getFont().deriveFont(scoreSize+10f));
                graphics.drawString(score, UNIT_SIZE+10+7*diffOffsetX, SCREEN_HEIGHT + 1 * UNIT_SIZE +2*diffOffsetY);
                graphics.setFont(graphics.getFont().deriveFont(size));
                graphics.drawString(diff, (21-DIFFICULTY)*UNIT_SIZE+4*diffOffsetX, SCREEN_HEIGHT + 1 * UNIT_SIZE + 10+2*diffOffsetY);
                break;
            case 2 :
                upperbound = 40;
                diffOffsetX = rand.nextInt(upperbound)-20;
                diffOffsetY = rand.nextInt(upperbound)-20;
                graphics.setFont(graphics.getFont().deriveFont(gainSize+10f));
                graphics.drawString(gain, 16*UNIT_SIZE+10+3*diffOffsetX, SCREEN_HEIGHT + 1 * UNIT_SIZE+3*diffOffsetY);
                graphics.setFont(graphics.getFont().deriveFont(scoreSize+10f));
                graphics.drawString(score, UNIT_SIZE+10+4*diffOffsetX, SCREEN_HEIGHT + 1 * UNIT_SIZE+2*diffOffsetY);
                graphics.setFont(graphics.getFont().deriveFont(size));
                graphics.drawString(diff, (21-DIFFICULTY)*UNIT_SIZE+7*diffOffsetX, SCREEN_HEIGHT + 1 * UNIT_SIZE + 10+6*diffOffsetY);
                break;
        }
        String stringLives = "lives : ";
        String lives = pacman.getLives()+"";
        graphics.setFont(graphics.getFont().deriveFont(30f));
        graphics.drawString(stringLives, 7*UNIT_SIZE+3*diffOffsetX, SCREEN_HEIGHT+2*diffOffsetY + 1 * UNIT_SIZE);
        graphics.setFont(graphics.getFont().deriveFont(30f+10*(pacman.getLives()+1)));
        graphics.drawString(lives, 11*UNIT_SIZE+3*diffOffsetX, SCREEN_HEIGHT+2*diffOffsetY + 2 + 1 * UNIT_SIZE);

        graphics.setColor(Color.white);
        graphics.setFont(graphics.getFont().deriveFont(30f));
        graphics.drawString(stringLives, 6*UNIT_SIZE, SCREEN_HEIGHT + 2 * UNIT_SIZE);
        graphics.setFont(graphics.getFont().deriveFont(30f+10*(pacman.getLives()+1)));
        graphics.drawString(lives, 10*UNIT_SIZE, SCREEN_HEIGHT + 5 + 2 * UNIT_SIZE);

        //graphics.setFont(graphics.getFont().deriveFont(size+10f));
        //graphics.drawString(diff, 21*UNIT_SIZE+10, SCREEN_HEIGHT + 1 * UNIT_SIZE + 10);

        graphics.setColor(new Color(255 , 255-gainCM, 255-gainCM));
        graphics.setFont(graphics.getFont().deriveFont(gainSize));
        graphics.drawString(gain, 16*UNIT_SIZE, SCREEN_HEIGHT + 2 * UNIT_SIZE);

        graphics.setColor(new Color(255 , 255-scoreCM, 255-scoreCM));
        graphics.setFont(graphics.getFont().deriveFont(scoreSize));
        graphics.drawString(score, UNIT_SIZE, SCREEN_HEIGHT + 2 * UNIT_SIZE);

        int colorOffset = random.nextInt(240);
        graphics.setColor(Color.BLACK);
        switch (DIFFICULTY){
            case 0:
                graphics.setColor(Color.white);
                break;
            case 1:
                size = 120f-colorOffset/26;
                graphics.setColor(new Color(255-colorOffset/3, 0, 0));
                break;
            case 2:
                size = 200f-colorOffset/10;
                graphics.setColor(new Color(255-colorOffset/2, 0, 70+colorOffset/4));
                break;
        }
        graphics.setFont(graphics.getFont().deriveFont(size));
        graphics.drawString(diff, (21-DIFFICULTY)*UNIT_SIZE, SCREEN_HEIGHT + 2 * UNIT_SIZE + 10);

        //TODO : WHEN PACMAN LOSES A LIFE
        /*Random random1 = new Random();
        int bloodX = random1.nextInt(20)-10;
        int bloodY = random1.nextInt(20)-10;
        pacman.setLives(2);
        for (int i = 3; i >= pacman.getLives(); --i){
            ImageIcon blood = new ImageIcon("C:\\UNI\\Code\\Pacman\\SKINS\\PACMAN\\blood.png");
            graphics.drawImage(blood.getImage(), 300+5*bloodX,300+5*bloodY, UNIT_SIZE*10, UNIT_SIZE*10, test);
        }*/

        if (RUNNING == false && pacman.getLives() > 0) {
            ImageIcon img = new ImageIcon("SKINS\\ITEMS\\pause.png");
            graphics.drawImage(img.getImage(), 0,0, test);
        }

        if (pacman.getLives() == 0) {
            pacman.getTheme().stop();
            String over = "GAME OVER";
            graphics.setFont(graphics.getFont().deriveFont(125f));
            graphics.setColor(new Color(255-colorOffset/2, 5, 70+colorOffset/4));
            graphics.drawString(over, 30, 300);
            graphics.drawString(over, 35, 305);
            if (!endGame) {
                Score.recordScore(SCORE, scores);
                endGame = true;
            }
        }
        FRAME_COUNTER += 0.05;
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        try {
            try {
                draw(graphics);
            } catch (FontFormatException e) {
                throw new RuntimeException(e);
            }
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        if (SCORE < 2000) depthOpacity = 0;
        else if (depthOpacity <= 0.50) {
            if (DIFFICULTY == 0  && pacman.getLives() > 0) {
                try {
                    diffStart = System.currentTimeMillis();
                    pacman.getTheme().stop();
                    File transition = new File("thunder.wav");
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(transition);
                    Clip trans = AudioSystem.getClip();
                    trans.open(audioStream);
                    trans.start();
                    File lev2 = new File("lev2.wav");
                    AudioInputStream audioStream2 = AudioSystem.getAudioInputStream(lev2);
                    Clip level2 = AudioSystem.getClip();
                    level2.open(audioStream2);

                    level2.start();
                    pacman.setTheme(level2);
                    ghosts.addAll(CharacterMethods.spawnGhosts());
                } catch (UnsupportedAudioFileException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                }

                DIFFICULTY = 1;
            }
            depthOpacity += 0.001;
        }
        Random rand = new Random();
        ImageIcon depth = new ImageIcon("scenery_scoreEffect2.png");
        Graphics2D G = (Graphics2D) graphics;
        G.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) depthOpacity));
        if (DIFFICULTY != 0) {
            int bound = 15 * DIFFICULTY;
            int offset = 2;
            int scatterX = rand.nextInt(bound) - bound / 2;
            int scatterY = rand.nextInt(bound) - bound / 2;
            G.drawImage(depth.getImage(), -300 + scatterX, -300 + scatterY, SCREEN_WIDTH * offset, SCREEN_HEIGHT * offset, null);
        }
        if (SCORE < 5000) depthOpacityRed = 0;
        else if (depthOpacityRed <= 0.10) {
            if (DIFFICULTY == 1) {
                try {
                    diffStart = System.currentTimeMillis();
                    File transition = new File("scream.wav");
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(transition);
                    Clip trans = AudioSystem.getClip();
                    trans.open(audioStream);
                    trans.start();
                    ghosts.addAll(CharacterMethods.spawnGhosts());
                } catch (UnsupportedAudioFileException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
                DIFFICULTY = 2;
            }
            depthOpacityRed += 0.001;
        }
        if (DIFFICULTY == 2 && pacman.getLives() > 0){
            maxDifCounter++;
            Random random = new Random();
            int colorOffsetH = random.nextInt(200);
            String over = "HORDEMODE";
            graphics.setFont(graphics.getFont().deriveFont(125f));
            graphics.setColor(new Color(255-colorOffsetH/2, 5, 70+colorOffsetH/4));
            graphics.drawString(over, 30, 300);
            graphics.setColor(new Color(255-colorOffsetH/4, 5, 70+colorOffsetH/8));
            graphics.drawString(over, 35, 305);
        }
        if(maxDifCounter / 10 == 1) {
            try {
                ghosts.addAll(CharacterMethods.spawnGhosts());
            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        ImageIcon red = new ImageIcon("scenery_scoreEffect3.png");
        Graphics2D G2 = (Graphics2D)graphics;
        G2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)depthOpacityRed));
        G2.drawImage(red.getImage(), 0, 0, 600, 800, null);
    }

    public void ghostAdditionLogic() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        long currentTime = (System.currentTimeMillis() - diffStart)/1000+1;
        switch (DIFFICULTY) {
            case 0:
                if (MOVE_COUNTER % 300 == 0) {
                    powerUps.add(new PowerUp());
                    ghosts.addAll(CharacterMethods.spawnGhosts());
                    //PowerUp.placePowerUps(powerUps, listGoodTiles());
                }
                break;
            case 1:
                if (MOVE_COUNTER % 200 == 0) {
                    powerUps.add(new PowerUp());
                    ghosts.addAll(CharacterMethods.spawnGhosts());
                    PowerUp.placePowerUps(powerUps, listGoodTiles());
                }
                break;
            case 2:
                if (currentTime % 10 == 0) {
                    powerUps.add(new PowerUp());
                    ghosts.addAll(CharacterMethods.spawnGhosts());
                    ghosts.addAll(CharacterMethods.spawnGhosts());
                    PowerUp.placePowerUps(powerUps, listGoodTiles());
                }
                break;
        }
        currentTime++;
    }

    public boolean withinBoundary(int number, char param){
        if(param == 'x' && number >= 0 && number <= SCREEN_WIDTH-UNIT_SIZE){
            return true;
        }
        if(param == 'y' && number >= 0 && number <= SCREEN_HEIGHT-UNIT_SIZE){
            return true;
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (RUNNING){
            if(pacman.getTaunting() && pacman.getTaunt() != null) {
                pacman.getTaunt().stop();
            }
            try {
                for(int i = 0; i < ghosts.size(); ++i){
                    ArrayList<Ghost> otherGhosts = new ArrayList<>();
                    otherGhosts.addAll(ghosts);
                    otherGhosts.remove(i);
                    ghosts.get(i).collideWithPacman(pacman);
                    ghosts.get(i).move(otherGhosts);
                    ghosts.get(i).collideWithPacman(pacman);
                }
                pacman.move();
                for(int i = 0; i < ghosts.size(); ++i){
                    ghosts.get(i).collideWithPacman(pacman);
                }
                if(pacman.getLives() == 0) {
                    RUNNING = false;
                    ended = false;
                    File endFile = new File("end.wav");
                    AudioInputStream as = AudioSystem.getAudioInputStream(endFile);
                    Clip endClip = AudioSystem.getClip();
                    endClip.open(as);
                    endClip.start();
                }
                ghostAdditionLogic();
            } catch (UnsupportedAudioFileException ex) {
                throw new RuntimeException(ex);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        repaint();
    }
    public class CustomKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent event) {
            pacman.setDirPrev(pacman.getDir());
            if (pacman.getLives() > 0) {
                switch (event.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (withinBoundary(pacman.getX() - MOVE_FACTOR, 'x')) {
                            pacman.setInvincible(false);
                            if (pacman.checkIfBlocked('l') || pacman.illegalX()) {
                                pacman.setDirSave('l');
                            } else {
                                pacman.setDir('l');
                            }
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        pacman.setInvincible(false);
                        if (withinBoundary(pacman.getX() + MOVE_FACTOR, 'x')) {
                            if (pacman.checkIfBlocked('r') || pacman.illegalX()) {
                                pacman.setDirSave('r');
                            } else {
                                pacman.setDir('r');
                            }
                        }
                        break;
                    case KeyEvent.VK_UP:
                        pacman.setInvincible(false);
                        if (withinBoundary(pacman.getY() - MOVE_FACTOR, 'y')) {
                            if (pacman.checkIfBlocked('u') || pacman.illegalY()) {
                                pacman.setDirSave('u');
                            } else {
                                pacman.setDir('u');
                            }
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        pacman.setInvincible(false);
                        if (withinBoundary(pacman.getY() + MOVE_FACTOR, 'y')) {
                            if (pacman.checkIfBlocked('d') || pacman.illegalY() && pacman.getDir() != 'u') {
                                pacman.setDirSave('d');
                            } else {
                                pacman.setDir('d');
                            }
                        }
                        break;
                    case KeyEvent.VK_SPACE:
                        pacman.setInvincible(true);
                        pacman.setDir('t');
                        break;
                    case KeyEvent.VK_E:
                        if (pacman.getBombs().size() != 0)
                            liveBombs.add(pacman.placeBomb());
                        break;
                    case KeyEvent.VK_Q:
                        if (RUNNING) {
                            RUNNING = false;
                            paused.start();
                        } else {
                            RUNNING = true;
                            paused.stop();
                        }
                        pacman.getTheme().stop();
                        break;
                }
                if (pacman.getDirSave() == 'u' && pacman.getDir() == 'd') {
                    pacman.setDirSave('n');
                } else if (pacman.getDirSave() == 'd' && pacman.getDir() == 'u') {
                    pacman.setDirSave('n');
                } else if (pacman.getDirSave() == 'r' && pacman.getDir() == 'l') {
                    pacman.setDirSave('n');
                } else if (pacman.getDirSave() == 'l' && pacman.getDir() == 'r') {
                    pacman.setDirSave('n');
                }
            }
        }
    }
}