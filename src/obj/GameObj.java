package obj;

import javax.swing.*;

public class GameObj extends JComponent {

    protected double x, y;
    protected String ImgPath;// 图像路径

    public double getx() {
        return x;
    }

    public double gety() {
        return y;
    }

    public String getImgPath() {
        return ImgPath;
    }

    GameObj(double x, double y, String ImgPath) {
        this.x = x;
        this.y = y;
        this.ImgPath = ImgPath;
    }

}
