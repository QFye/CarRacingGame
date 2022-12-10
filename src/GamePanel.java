import javax.swing.*;

import java.awt.event.*;
import utils.GameUtils;
import obj.*;

import java.awt.*;

public class GamePanel extends JPanel {
    public Car myCar;

    GamePanel(GameWin gamewin) {
        this.setLayout(null);

        myCar = new Car(240, 600, "imgs/car.bmp");
        myCar.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    myCar.setAccelerate(0, -0.1);
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    myCar.setAccelerate(0, 0.1);
                    repaint();
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                repaint();
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
        g.drawImage(GameUtils.getObjImg(myCar.getImgPath()), (int) myCar.getx(), (int) myCar.gety(), 50, 70, this);
    }

}
