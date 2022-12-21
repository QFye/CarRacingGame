package games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import utils.*;
import obj.*;

public class GameWin extends JFrame {

    public static Status status = Status.Waiting;// 游戏状态
    public MenuPanel menuPanel;
    public GamePanel gamePanel;
    public static int WightWidth = 1000, WightHeight = 800;
    public CardLayout cardLayout = new CardLayout();
    public Container container;

    public void launch() {
        // 设置布局
        this.setLayout(cardLayout);

        // 获取底层容器
        this.container = this.getContentPane();

        // 设置窗口可见
        this.setVisible(true);
        // 设置窗口大小
        this.setSize(GameWin.WightWidth, GameWin.WightHeight);
        // 设置窗口位置
        this.setLocationRelativeTo(null);
        // 设置窗口标题
        this.setTitle("我的飞车");
        // 设置不可改变大小
        this.setResizable(false);
        // 设置退出方式
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 初始化面板
        menuPanel = new MenuPanel();
        container.add(menuPanel, "menu");
        cardLayout.show(container, "menu");

    }

    public static void main(String[] args) {
        GameWin gamewin = new GameWin();
        gamewin.launch();
    }

    // 面板一：菜单面板
    public class MenuPanel extends JPanel {

        MenuPanel() {
            this.setLayout(null);

            // 添加显示标题
            JLabel titleLabel = new JLabel("我的飞车");
            titleLabel.setFont(new Font("仿宋", Font.BOLD, 60));
            titleLabel.setBounds(370, 150, 300, 80);

            // 开始按钮
            JButton startButton = new JButton("开始游戏");
            // 游戏介绍按钮
            JButton ruleButton = new JButton("游戏介绍");
            // 退出按钮
            JButton exitButton = new JButton("退出游戏");

            // 设置按钮布局
            startButton.setBounds(400, 370, 200, 60);
            ruleButton.setBounds(400, 470, 200, 60);
            exitButton.setBounds(400, 570, 200, 60);

            // 为按钮添加监听事件
            startButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    // 更改游戏状态
                    GameWin.status = Status.InGame;

                    // 隐藏初始面板
                    setVisible(false);

                    // 新建并添加游戏面板
                    gamePanel = new GamePanel();
                    container.add(gamePanel, "game");
                    cardLayout.show(container, "game");

                    // 更改键盘焦点
                    if (!gamePanel.isFocusable()) {
                        gamePanel.setFocusable(true);
                    }
                    if (!gamePanel.isFocusOwner()) {
                        gamePanel.myCar.requestFocusInWindow();
                    }
                }

            });
            exitButton.addActionListener(e -> {
                System.exit(0);
            });

            // 添加面板
            this.add(titleLabel);
            this.add(startButton);
            this.add(ruleButton);
            this.add(exitButton);
        }

        @Override
        public void paintComponent(Graphics g) {
            g.drawImage(GameUtils.getBgImg(), 0, 0, GameWin.WightWidth, GameWin.WightHeight, this);
        }

    }

    public class GamePanel extends JPanel {
        public Car myCar;
        public Map<Integer, User> userList = new TreeMap<>();

        GamePanel() {
            this.myCar = new Car();
            myCar.setAttribute(1, 240, 600, 0, "imgs/car.bmp", GameUtils.CarWidth, GameUtils.CarHeight);
            this.setLayout(null);

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
                        rightRotate();
                    }
                    if (down && right) {
                        downMove();
                        leftRotate();
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

            // 创建游戏运行线程，不断链接服务器与重绘
            new Thread(new GameClient(this)).start();

        }

        @Override
        public void paintComponent(Graphics g) {
            myCar.update();
            g.drawImage(GameUtils.getBgImg(), 0, 0, GameWin.WightWidth, GameWin.WightHeight, this);
            Graphics2D g2d = (Graphics2D) g;
            userList.forEach((id, user) -> {
                System.out.println("客户端：绘制来自玩家" + id + "的汽车");
                g2d.rotate(Math.toRadians(user.getDir()), user.getX() + (GameUtils.CarWidth >> 1),
                        user.getY() + (GameUtils.CarHeight >> 1));
                g2d.drawImage(GameUtils.getObjImg(user.getImgPath()), (int) user.getX(),
                        (int) user.getY(), GameUtils.CarWidth, GameUtils.CarHeight, this);
            });
            // objs.forEach((id, obj) -> {
            // System.out.println("客户端：绘制物品" + obj.getid());
            // g2d.rotate(Math.toRadians(obj.getDir()), obj.getx() + (obj.getImgWidth() >>
            // 1),
            // obj.gety() + (obj.getImgHeight() >> 1));
            // g2d.drawImage(GameUtils.getObjImg(obj.getImgPath()), (int) obj.getx(), (int)
            // obj.gety(),
            // obj.getImgWidth(),
            // obj.getImgHeight(), this);
            // });
        }

    }

}