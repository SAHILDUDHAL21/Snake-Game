import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener
{

    static final int sw = 600;
    static final int sh = 600;
    static final int us = 25;
    static final int gu = (sw*sh)/(us*us);
    static final int DELAY = 175;
    final int[] x = new int[gu];
    final int[] y = new int[gu];
    int bodyParts = 3;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel()
    {
        random = new Random();
        this.setPreferredSize(new Dimension(sw, sh));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame()
    {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g)
    {

        if (running) {
            for (int i = 0; i < sh/us; i++) {
                g.drawLine(i*us, 0, i*us, sh);
                g.drawLine(0, i*us, sw, i*us);
            }

            g.setColor(Color.red);
            g.fillOval(appleX, appleY, us, us);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], us, us);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], us, us);
                }
            }

            // Draw score
            g.setColor(Color.green);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (sw - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }
    public void newApple() {
        appleX = random.nextInt((int)(sw/us))*us;
        appleY = random.nextInt((int)(sh/us))*us;
    }
    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - us;
                break;
            case 'D':
                y[0] = y[0] + us;
                break;
            case 'L':
                x[0] = x[0] - us;
                break;
            case 'R':
                x[0] = x[0] + us;
                break;
        }
    }
    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions() {
        // head body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        //  head  left border
        // comment cha bosda
        if (x[0] < 0)
        {
            running = false;
        }

        //  head  right border
        if (x[0] > sw)
        {
            running = false;
        }

        //  head  right border
        if (x[0] < 0)
        {
            running = false;
        }

        //  head  bottom border
        if (y[0] > sh)
        {
            running = false;
        }

        if (!running)
        {
            timer.stop();
        }

    }
    public void gameOver(Graphics g) {
        // Draw score
        g.setColor(Color.green);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (sw - metrics1.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());

        // Game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (sw - metrics2.stringWidth("Game Over"))/2, sh/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if(running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}