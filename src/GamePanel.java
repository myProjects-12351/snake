import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int BOARD_WIDTH = 1200;
    static final int BOARD_HEIGHT = 600;
    static final int UNIT_SIZE = 50;
    static final int AMOUNT_OF_UNITS = (BOARD_WIDTH * BOARD_WIDTH) / UNIT_SIZE;
    static final int DELAY = 1000;
    final int[] x = new int[AMOUNT_OF_UNITS];
    final int[] y = new int[AMOUNT_OF_UNITS];
    int bodyParts = 5;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean isRunning = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKyeAdapter());
        startGame();
    }
    public void startGame(){
        newApple();
        isRunning = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if(bodyParts == AMOUNT_OF_UNITS){
            isRunning = false;
            win(g);
        }
        if(isRunning) {
            //matrix
            for (int i = 0; i < BOARD_WIDTH / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, BOARD_HEIGHT);
            }
            for (int i = 0; i < BOARD_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(0, i * UNIT_SIZE, BOARD_WIDTH, i * UNIT_SIZE);
            }

            // apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            //snake
            //g.setColor(Color.green);
            for (int i = 0; i < bodyParts; i++) {
//                if(i==0 || i==bodyParts-1){
//                    g.setColor(Color.yellow);
//                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
//                    continue;
//                }
                int R = random.nextInt(255);
                int G = random.nextInt(255);
                int B = random.nextInt(255);
                int A = random.nextInt(255);
                int shape = random.nextInt(1);
                g.setColor(Color.green);
                switch(shape) {
                    case 0 -> g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    case 1 -> g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
        }else{
            gameOver(g);
        }
    }
    public void move(){
        for(int i=bodyParts; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction){
            case 'R' -> x[0] += UNIT_SIZE;
            case 'L' -> x[0] -= UNIT_SIZE;
            case 'U' -> y[0] -= UNIT_SIZE;
            case 'D' -> y[0] += UNIT_SIZE;
        }
    }
    public void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void newApple(){
        appleX = random.nextInt((int) (BOARD_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int) (BOARD_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void checkCollisions(){
        for(int i=bodyParts; i>0; i--){
            if(x[0]==x[i] && y[0]==y[i]){
                isRunning = false;
            }
        }
        if(x[0]<-1 || y[0]<-1 || x[0]>BOARD_WIDTH || y[0]>BOARD_HEIGHT){
            isRunning = false;
        }

        if(!isRunning)
            timer.stop();
    }
    public void gameOver(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (BOARD_WIDTH - metrics.stringWidth("Game Over"))/2, BOARD_HEIGHT/2);
    }
    public void win(Graphics g){
        g.setColor(Color.green);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("YOU WIN", (BOARD_WIDTH - metrics.stringWidth("YOU WIN"))/2, BOARD_HEIGHT/2);
    }

    public class MyKyeAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            if(e.getKeyCode() == KeyEvent.VK_UP && direction != 'D'){
                direction = 'U';
            }
            else if(e.getKeyCode() == KeyEvent.VK_DOWN && direction != 'U'){
                direction = 'D';
            }
            if(e.getKeyCode() == KeyEvent.VK_LEFT && direction != 'R'){
                direction = 'L';
            }
            if(e.getKeyCode() == KeyEvent.VK_RIGHT && direction != 'L'){
                direction = 'R';
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(isRunning){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
}
