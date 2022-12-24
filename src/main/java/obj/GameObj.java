package obj;

import javax.swing.*;

public class GameObj extends JComponent {

    protected int id;// 物品id
    protected double x, y, dir;// x,y坐标、方向角（与y轴负方向夹角）
    protected String ImgPath;// 图像路径
    protected int ImgWidth, ImgHeight;// 图像长宽

    public boolean isInfoObj() {
        return dir == -1;
    }

    public int getid() {
        return id;
    }

    public double getx() {
        return x;
    }

    public double gety() {
        return y;
    }

    public double getDir() {
        return dir;
    }

    public String getImgPath() {
        return ImgPath;
    }

    public int getImgWidth() {
        return ImgWidth;
    }

    public int getImgHeight() {
        return ImgHeight;
    }

    public void setAttribute(int id, double x, double y, double dir, String ImgPath, int ImgWidth, int ImgHeight) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.ImgPath = ImgPath;
        this.ImgWidth = ImgWidth;
        this.ImgHeight = ImgHeight;
    }

}
