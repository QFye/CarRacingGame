package games;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import utils.*;
import obj.*;

public class GameWin extends JFrame {

    public static Status status = Status.Waiting;// 游戏状态
    private MenuPanel menuPanel;
    private InitPanel initPanel;
    private GamePanel gamePanel;
    public static int WightWidth = 1000, WightHeight = 800;// 窗体长宽
    private CardLayout cardLayout = new CardLayout();
    private Container container;
    private String playerName;

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

    // 客户端程序入口
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

                    // 新建并添加用户名设置界面
                    if (initPanel == null) {
                        initPanel = new InitPanel();
                    }
                    initPanel.setVisible(true);
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

        // 绘制
        @Override
        public void paintComponent(Graphics g) {
            g.drawImage(GameUtils.getMenuBgImg(), 0, 0, GameWin.WightWidth, GameWin.WightHeight, this);
        }

    }

    // 面板二：用户名设置界面
    public class InitPanel extends JFrame {
        private JTextField textField;

        InitPanel() {
            // 设置面板布局
            GridBagLayout gbl = new GridBagLayout();
            GridBagConstraints gbc = new GridBagConstraints();
            this.setLayout(gbl);

            // 设置文字提示
            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridx = gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.gridheight = 1;
            gbc.insets = new Insets(5, 20, 5, 20);
            JLabel label = new JLabel("请设置您的游戏昵称：", JLabel.CENTER);
            this.add(label);
            gbl.setConstraints(label, gbc);

            // 设置文本输入框
            gbc.gridy = 1;
            gbc.insets = new Insets(5, 70, 5, 70);
            textField = new JTextField(10);
            this.add(textField);
            gbl.setConstraints(textField, gbc);

            // 设置确定按钮
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            gbc.insets = new Insets(5, 100, 5, 60);
            JButton yButton = new JButton("确定");
            yButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 如果没有游戏面板则创建，否则则启动
                    if (gamePanel == null) {
                        gamePanel = new GamePanel();
                        container.add(gamePanel, "game");
                    } else {
                        new Thread(new GameClient(gamePanel)).start();
                        ;
                    }
                    // 更新用户名
                    playerName = textField.getText();
                    // 切换游戏面板
                    cardLayout.show(container, "game");
                    // 更改键盘焦点
                    gamePanel.getFocused();
                    // 隐藏当前面板
                    initPanel.setVisible(false);
                }
            });
            this.add(yButton);
            gbl.setConstraints(yButton, gbc);

            // 设置取消按钮
            gbc.gridx = 1;
            gbc.insets = new Insets(5, 60, 5, 100);
            JButton cancelButton = new JButton("取消");
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    initPanel.setVisible(false);
                }
            });
            this.add(cancelButton);
            gbl.setConstraints(cancelButton, gbc);

            // 设置窗口属性
            this.setSize(400, 200);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }
    }

    // 面板三：游戏面板
    public class GamePanel extends JLayeredPane {
        public Car myCar;
        public TreeMap<Integer, User> userList = new TreeMap<>();
        public JButton settingsButton;
        public ChatPane chatPane = new ChatPane();
        private StopPanel stopPanel = new StopPanel();
        private int originX = 0, originY = -2200;// 长地图的相对原点坐标（设地图左上角为原点）
        private boolean readyStatusChanged = false;
        public TreeMap<Integer, PlayerInfoPanel> playerInfos = new TreeMap<>();
        private boolean showCollisionVolumn = false;

        public void countDown() {
            JLabel countDownNumber = new JLabel("3", JLabel.CENTER);
            countDownNumber.setFont(new Font("黑体", Font.BOLD, 80));
            countDownNumber.setSize(400, 400);
            countDownNumber.setLocation(300, 200);
            this.add(countDownNumber);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownNumber.setText("2");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownNumber.setText("1");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownNumber.setText("start");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.remove(countDownNumber);
        }

        public void updatePlayerInfoPanel(Integer i) {
            if (!playerInfos.get(i).isVisible()) {
                PlayerInfoPanel playerInfoPanel = new PlayerInfoPanel(userList.get(i));
                playerInfos.put(i, playerInfoPanel);
                this.add(playerInfoPanel);
                playerInfos.get(i).setVisible(true);
                System.out.println("更新玩家 " + userList.get(i).getName() + " 的信息面板");
            }
        }

        public String getPlayerName() {
            return playerName;
        }

        public CardLayout getCardLayout() {
            return cardLayout;
        }

        public Container getGameWinContainer() {
            return container;
        }

        public boolean isReadyStatusChanged() {
            return readyStatusChanged;
        }

        public void setReadyStatusChanged(boolean readyStatusChanged) {
            this.readyStatusChanged = readyStatusChanged;
        }

        public void getFocused() {
            if (!this.isFocusable()) {
                this.setFocusable(true);
            }
            if (!this.isFocusOwner()) {
                this.requestFocusInWindow();
            }
        }

        public void launch() {
            // 生成myCar
            if (myCar == null) {
                this.myCar = new Car();
                myCar.setAttribute(1, 240, 2800, 0, GameUtils.getCarPathString(1), GameUtils.CarWidth,
                        GameUtils.CarHeight);
            }
            this.setLayout(null);

            // 设置暂停/继续按钮
            settingsButton = new JButton("暂停");
            settingsButton.setLocation(900, 20);
            settingsButton.setSize(60, 30);
            settingsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (GameWin.status == Status.InGame) {
                        stopPanel.getFocused();
                        stopPanel.setVisible(true);
                        GameWin.status = Status.Suspend;
                        settingsButton.setText("继续");
                    } else if (GameWin.status == Status.Suspend) {
                        // 切换键盘焦点
                        gamePanel.getFocused();
                        // 隐藏面板
                        stopPanel.setVisible(false);
                        // 更改游戏状态
                        GameWin.status = Status.InGame;
                        // 更改按钮状态
                        settingsButton.setText("暂停");
                    }
                }
            });
            this.add(settingsButton, JLayeredPane.MODAL_LAYER);

            // 聊天面板
            chatPane.setLocation(30, 550);
            this.add(chatPane, JLayeredPane.MODAL_LAYER);

            // 增加暂停面板
            this.add(stopPanel, JLayeredPane.DRAG_LAYER);

            // 初始化玩家信息界面
            for (int i = 1; i <= 5; i++) {
                playerInfos.put(i, new PlayerInfoPanel(new User(i)));
                playerInfos.get(i).setVisible(false);
                this.add(playerInfos.get(i), JLayeredPane.MODAL_LAYER);
            }

            // 键盘监听
            this.addKeyListener(new KeyListener() {
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
                    // 用T键控制聊天面板的显示
                    if (e.getKeyChar() == 't') {
                        chatPane.setVisible(!chatPane.isVisible());
                    }
                    // 用P键更改准备状态
                    if (e.getKeyChar() == 'p') {
                        setReadyStatusChanged(true);
                        playerInfos.forEach((id, panel) -> {
                            if (panel.getUserInfo().getId() == myCar.getid()) {
                                String status = panel.getButtonText() == "准备" ? "取消" : "准备";
                                panel.setButtonText(status);
                            }
                        });
                    }
                    // 用U键显示碰撞体积
                    if (e.getKeyChar() == 'u') {
                        showCollisionVolumn = !showCollisionVolumn;
                    }
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
                    // ESC键打开暂停面板
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        GameWin.status = Status.Suspend;
                        stopPanel.setVisible(true);
                        stopPanel.getFocused();
                        settingsButton.setText("继续");
                    }
                }

            });
            this.add(myCar);

            // 点击游戏面板时将键盘焦点从聊天栏转移到游戏
            this.addMouseListener(new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // 这里需要判断是否处于暂停状态
                    if (GameWin.status != Status.Suspend)
                        gamePanel.getFocused();
                    super.mouseClicked(e);
                }
            });

            // 创建游戏运行线程，不断链接服务器与重绘
            new Thread(new GameClient(this)).start();

        }

        GamePanel() {
            launch();
        }

        // 组件绘制接口
        @Override
        public void paintComponent(Graphics g) {
            // 更新汽车信息
            if (GameWin.status == Status.InGame || GameWin.status == Status.Suspend)
                myCar.update(userList);
            // 更新原点坐标（移动背景图片来实现汽车的移动效果）
            if (myCar.gety() > 500 && myCar.gety() < 2500) {
                originY = 300 - (int) myCar.gety();
            }
            // 绘制背景
            g.drawImage(GameUtils.getBgImg(), originX, originY, 1000, 3000, this);
            // 绘制汽车
            Graphics2D g2d = (Graphics2D) g;
            userList.forEach((id, user) -> {

                // 旋转画笔
                g2d.rotate(Math.toRadians(user.getDir()), user.getX() + originX + (GameUtils.CarWidth >> 1),
                        user.getY() + originY + (GameUtils.CarHeight >> 1));
                // 绘制图片
                g2d.drawImage(GameUtils.getObjImg(user.getImgPath()), (int) user.getX() + originX,
                        (int) user.getY() + originY, GameUtils.CarWidth, GameUtils.CarHeight, this);
                // 回溯画笔角度
                g2d.rotate(-Math.toRadians(user.getDir()), user.getX() + originX + (GameUtils.CarWidth >> 1),
                        user.getY() + originY + (GameUtils.CarHeight >> 1));
                if (showCollisionVolumn) {
                    // 绘制碰撞体积（会丢失精度）
                    g2d.drawImage(GameUtils.getCollisionDisplayerImg(), (int) user.getX() + originX,
                            (int) user.getY() + originY, (int) user.getBoxWidth(), (int) user.getBoxHeight(), this);
                }

                // 绘制用户名
                if (user.getName() != null) {
                    // 抗锯齿
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    // 设置字体
                    Font font = new Font("黑体", Font.PLAIN, 18);
                    g2d.setFont(font);
                    // 设置颜色
                    if (user.getName().equals(playerName)) {
                        g2d.setColor(Color.BLUE);
                    } else {
                        g2d.setColor(Color.BLACK);
                    }
                    // 获取字符串长度
                    FontMetrics fm = g2d.getFontMetrics(font);
                    int textWidth = fm.stringWidth(user.getName());
                    // 绘制字符串
                    g2d.drawString(user.getName(),
                            (float) user.getX() + originX + (GameUtils.CarWidth - textWidth >> 1),
                            (float) user.getY() + originY - 10);
                }

            });
        }

        // 聊天面板
        public class ChatPane extends JPanel {
            private JScrollPane chatRecorder = new JScrollPane();// 聊天记录容器
            private JLabel chatLabel = new JLabel("<html></html>");// 聊天记录显示
            private JLabel chatTitle = new JLabel("聊天栏", JLabel.CENTER);// 聊天面板标题
            private JTextField textField = new JTextField(50);// 消息输入框
            private boolean send = false;// 判断是否有消息发送
            private String sendContent;// 记录发送内容

            public boolean isSend() {
                return send;
            }

            public void setSend(boolean send) {
                this.send = send;
            }

            public String getSendContent() {
                return sendContent;
            }

            // 重置输入框消息
            public void resetContent() {
                textField.setText("");
            }

            // 在聊天显示中添加消息
            public void appendMsg(String msg) {
                String s = chatLabel.getText();
                String ret = s.substring(0, s.indexOf("</html>"));
                ret += msg + "<br></html>";
                chatLabel.setText(ret);
            }

            ChatPane() {
                // 设置背景颜色
                this.setBackground(new Color(192, 192, 192));
                // 设置字体
                chatLabel.setFont(new Font("宋体", Font.PLAIN, 10));
                // 绑定滚动条
                chatRecorder.setViewportView(chatLabel);
                // 设置面板布局
                GridBagLayout cgbl = new GridBagLayout();
                GridBagConstraints cgbc = new GridBagConstraints();
                this.setLayout(cgbl);
                // 设置标题
                cgbc.fill = GridBagConstraints.BOTH;
                cgbc.gridx = 0;
                cgbc.gridy = 0;
                cgbc.gridheight = 1;
                cgbc.gridwidth = 3;
                cgbc.weighty = 0.2;
                this.add(chatTitle);
                cgbl.setConstraints(chatTitle, cgbc);
                // 设置聊天记录显示
                cgbc.gridy = 1;
                cgbc.weighty = 0.7;
                cgbc.weightx = 1;
                this.add(chatRecorder);
                cgbl.setConstraints(chatRecorder, cgbc);
                // 设置输入框
                cgbc.gridy = 2;
                cgbc.weighty = 0.1;
                cgbc.weightx = 0.9;
                cgbc.gridwidth = 2;
                this.add(textField);
                cgbl.setConstraints(textField, cgbc);
                // 设置发送按钮
                cgbc.gridx = 2;
                cgbc.gridwidth = 1;
                cgbc.weightx = 0.1;
                JButton sendButton = new JButton("发送");
                sendButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setSend(true);
                        sendContent = textField.getText();
                    }
                });
                this.add(sendButton);
                cgbl.setConstraints(sendButton, cgbc);
                // 设置面板大小
                this.setSize(200, 200);
            }

        }

        // 暂停面板
        public class StopPanel extends JPanel {

            // 按钮名称
            private final String[] buttonName = { "返回游戏", "查看帮助", "设置", "返回主菜单" };

            // 获取焦点
            public void getFocused() {
                if (!this.isFocusable()) {
                    this.setFocusable(true);
                }
                if (!this.isFocusOwner()) {
                    this.requestFocusInWindow();
                }
            }

            StopPanel() {
                GridBagLayout gbl = new GridBagLayout();
                GridBagConstraints gbc = new GridBagConstraints();
                this.setLayout(gbl);

                // 暂停面板标题
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.gridx = gbc.gridy = 0;
                gbc.gridwidth = gbc.gridheight = 1;
                gbc.weightx = gbc.weighty = 0.5;
                JLabel title = new JLabel("暂停", JLabel.CENTER);
                title.setFont(new Font("宋体", Font.BOLD, 35));
                this.add(title);
                gbl.setConstraints(title, gbc);

                // 添加按钮
                for (String name : buttonName) {
                    gbc.gridy++;
                    gbc.insets = new Insets(0, 100, 5, 100);
                    JButton button = new JButton(name);
                    button.setFont(new Font("黑体", Font.BOLD, 28));
                    button.setSize(200, 70);
                    button.setBackground(new Color(65, 105, 225));
                    button.setForeground(Color.WHITE);
                    this.add(button);
                    gbl.setConstraints(button, gbc);

                    if (name.equals("返回游戏")) {
                        button.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // 切换键盘焦点
                                gamePanel.getFocused();
                                // 隐藏面板
                                stopPanel.setVisible(false);
                                // 更改游戏状态
                                GameWin.status = Status.InGame;
                                // 更改按钮状态
                                settingsButton.setText("暂停");
                            }
                        });
                    } else if (name.equals("查看帮助")) {

                    } else if (name.equals("设置")) {

                    } else if (name.equals("返回主菜单")) {
                        button.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // 隐藏暂停面板
                                stopPanel.setVisible(false);
                                // 更改游戏状态
                                GameWin.status = Status.Waiting;
                                // 切换页面
                                cardLayout.show(container, "menu");
                            }
                        });
                    }
                }

                // 设置键盘监听
                this.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        // ESC：返回游戏
                        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                            // 切换键盘焦点
                            gamePanel.getFocused();
                            // 隐藏面板
                            stopPanel.setVisible(false);
                            // 更改游戏状态
                            GameWin.status = Status.InGame;
                            // 更改按钮状态
                            settingsButton.setText("暂停");
                        }
                    }
                });

                this.setVisible(false);
                this.setSize(400, 500);
                this.setLocation(300, 150);
                this.setBackground(new Color(128, 138, 135));
            }

        }

        public class PlayerInfoPanel extends JPanel {
            private User userInfo;
            private JLabel statusLabel;
            private JButton preparedButton;

            public User getUserInfo() {
                return userInfo;
            }

            public void setStatusText(String status) {
                String s = "玩家状态：";
                statusLabel.setText(s + status);
            }

            public void setButtonText(String status) {
                preparedButton.setText(status);
            }

            public String getButtonText() {
                return preparedButton.getText();
            }

            PlayerInfoPanel(User userInfo) {
                this.userInfo = userInfo;

                this.setLayout(null);
                this.setSize(180, 100);

                // 添加玩家名称
                JLabel playerName = new JLabel(userInfo.getName(), JLabel.CENTER);
                playerName.setSize(180, 40);
                playerName.setLocation(0, 20);
                playerName.setFont(new Font("宋体", Font.BOLD, 28));
                this.add(playerName);

                // 添加玩家状态信息
                String status;
                if (!userInfo.isOnline()) {
                    status = "离线";
                } else if (userInfo.isReady()) {
                    status = "已准备";
                } else {
                    status = "未准备";
                }
                statusLabel = new JLabel("玩家状态：" + status, JLabel.CENTER);
                statusLabel.setSize(110, 40);
                statusLabel.setLocation(0, 50);
                this.add(statusLabel);

                // 添加准备按钮
                preparedButton = new JButton("准备");
                preparedButton.setSize(55, 30);
                preparedButton.setLocation(107, 55);
                preparedButton.setFont(new Font("黑体", Font.PLAIN, 10));
                preparedButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setReadyStatusChanged(true);
                        playerInfos.forEach((id, panel) -> {
                            if (panel.getUserInfo().getId() == myCar.getid()) {
                                String status = panel.getButtonText() == "准备" ? "取消" : "准备";
                                panel.setButtonText(status);
                            }
                        });
                    }
                });
                this.add(preparedButton);
                if (userInfo.getId() != myCar.getid()) {
                    preparedButton.setVisible(false);
                }

                this.setBackground(new Color(135, 206, 235));
                this.setLocation(820, 120 * userInfo.getId() - 20);
            }
        }
    }

}