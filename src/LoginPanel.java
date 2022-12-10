import javax.swing.*;

import utils.GameUtils;

import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JPanel {

    LoginPanel(GameWin gamewin) {
        this.setLayout(null);

        // 添加显示标题
        Font titleFont = new Font("仿宋", Font.BOLD, 60);
        JLabel titleLabel = new JLabel("我的飞车");
        titleLabel.setFont(titleFont);
        titleLabel.setBounds(370, 150, 300, 80);

        // 开始按钮
        JButton startBtn = new JButton("开始游戏");
        startBtn.setBounds(400, 370, 200, 60);
        startBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                gamewin.status = GameWin.Status.InGame;
                // 隐藏初始面板
                setVisible(false);
                // 新建并添加游戏面板
                GamePanel gamepanel = new GamePanel(gamewin);

                gamewin.add(gamepanel);
                gamewin.repaint();
                gamepanel.setVisible(true);
                if (!gamepanel.isFocusable()) {
                    gamepanel.setFocusable(true);
                }
                if (!gamepanel.isFocusOwner()) {
                    gamepanel.myCar.requestFocusInWindow();
                }
            }

        });
        // 游戏介绍按钮
        JButton ruleBtn = new JButton("游戏介绍");
        ruleBtn.setBounds(400, 470, 200, 60);
        // 退出按钮
        JButton exitBtn = new JButton("退出游戏");
        exitBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }

        });
        exitBtn.setBounds(400, 570, 200, 60);

        // 添加面板
        this.add(titleLabel);
        this.add(startBtn);
        this.add(ruleBtn);
        this.add(exitBtn);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(GameUtils.getBgImg(), 0, 0, GameWin.WightWidth, GameWin.WightHeight, this);
    }

}
