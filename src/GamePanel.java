import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import utils.GameUtils;
import obj.*;

public class GamePanel extends JPanel {
    public Car myCar;

    GamePanel(GameWin gamewin) {
        this.setLayout(null);

        myCar = new Car(240, 600, "imgs/car.bmp");
        myCar.addKeyListener(new KeyListener() {
            Boolean up = false, down = false, left = false, right = false;

            private void upMove() {
                myCar.setAccelerate(0.1 * Math.sin(Math.toRadians(myCar.getDir())),
                        -0.1 * Math.cos(Math.toRadians(myCar.getDir())));
            }

            private void downMove() {
                myCar.setAccelerate(-0.1 * Math.sin(Math.toRadians(myCar.getDir())),
                        0.1 * Math.cos(Math.toRadians(myCar.getDir())));
            }

            private void leftRotate() {
                myCar.setrAccelerate(-0.03);
            }

            private void rightRotate() {
                myCar.setrAccelerate(0.03);
            }

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

                // 检测上下键：控制汽车前进/后退 左右键：控制汽车方向
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    up = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    down = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    left = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    right = true;
                }

                if (up) {
                    upMove();
                }
                if (down) {
                    downMove();
                }
                if (left) {
                    leftRotate();
                }
                if (right) {
                    rightRotate();
                }
                if (up && left) {
                    upMove();
                    leftRotate();
                }
                if (up && right) {
                    upMove();
                    rightRotate();
                }
                if (down && left) {
                    downMove();
                    leftRotate();
                }
                if (down && right) {
                    downMove();
                    rightRotate();
                }
                repaint();

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    up = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    down = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    left = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    right = false;
                }
            }

        });
        this.add(myCar);
        Thread driveThread = new Thread(new Runnable() {

            @Override
            public void run() {

                while (gamewin.status == GameWin.Status.InGame) {
                    repaint();
                    if (myCar.gety() < -100) {
                        gamewin.status = GameWin.Status.Suceeded;
                        System.out.println("You win");
                    }
                }

            }

        });
        driveThread.start();

    }

    @Override
    public void paintComponent(Graphics g) {
        myCar.update();
        g.drawImage(GameUtils.getBgImg(), 0, 0, GameWin.WightWidth, GameWin.WightHeight, this);
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(Math.toRadians(myCar.getDir()), myCar.getx() + 25, myCar.gety() + 35);
        g2d.drawImage(GameUtils.getObjImg(myCar.getImgPath()), (int) myCar.getx(), (int) myCar.gety(), 50, 70, this);
    }

}
