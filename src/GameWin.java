import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import utils.GameUtils;

public class GameWin extends JFrame {

    // 定义游戏状态：未开始，游戏中，暂停，失败，胜利
    enum Status {
        Waiting, InGame, Suspend, Failed, Suceeded;
    }

    public static Status status = Status.Waiting;// 游戏状态
    private static JPanel bgPanel = new JPanel();
    private static JLayeredPane layeredPane = new JLayeredPane();// 分层窗格
    public static int WightWidth = 1000, WightHeight = 800;

    public void launch() {

        // 添加显示标题
        Font titleFont = new Font("仿宋", Font.BOLD, 60);
        JLabel titleLabel = new JLabel("我的飞车");
        titleLabel.setFont(titleFont);
        titleLabel.setBounds(370, 150, 300, 80);

        // 添加背景图片
        JLabel bgImgLabel = new JLabel(GameUtils.getBgImg());
        bgPanel.add(bgImgLabel);
        bgPanel.setBounds(0, 0, GameUtils.getBgImg().getIconWidth(), GameUtils.getBgImg().getIconHeight());
        this.add(bgPanel);

        // 开始按钮
        JButton startBtn = new JButton("开始游戏");
        startBtn.setBounds(400, 370, 200, 60);
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
        layeredPane.add(titleLabel, JLayeredPane.MODAL_LAYER);
        layeredPane.add(bgPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(startBtn, JLayeredPane.MODAL_LAYER);
        layeredPane.add(ruleBtn, JLayeredPane.MODAL_LAYER);
        layeredPane.add(exitBtn, JLayeredPane.MODAL_LAYER);

        // 设置窗口可见
        this.setVisible(true);
        // 设置窗口大小
        this.setSize(WightWidth, WightHeight);
        // 设置窗口位置
        this.setLocationRelativeTo(null);
        // 设置窗口标题
        this.setTitle("我的飞车");
        // 设置退出方式
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        // 设置分层窗格
        this.setLayeredPane(layeredPane);
    }

    public static void main(String[] args) {
        GameWin gamewin = new GameWin();
        gamewin.launch();
    }
}
