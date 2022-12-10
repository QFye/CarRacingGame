import javax.swing.*;

public class GameWin extends JFrame {

    // 定义游戏状态：未开始，游戏中，暂停，失败，胜利
    public enum Status {
        Waiting, InGame, Suspend, Failed, Suceeded;
    }

    public Status status = Status.Waiting;// 游戏状态
    public LoginPanel loginpanel;
    public static int WightWidth = 1000, WightHeight = 800;

    public void launch() {
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
        loginpanel = new LoginPanel(this);
        this.add(loginpanel);

    }

    public static void main(String[] args) {
        GameWin gamewin = new GameWin();
        gamewin.launch();

    }
}
