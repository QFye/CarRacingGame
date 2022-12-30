package obj;

import javax.swing.*;

public abstract class GameObj extends JComponent {

    protected int id;// 物品id
    protected double x, y, dir;// x,y坐标、方向角（与y轴负方向夹角）
    protected String ImgPath;// 图像路径
    protected int ImgWidth, ImgHeight;// 图像长宽
    protected boolean collectable;// 是否可以被收集

    public int getId() {
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

    public boolean isCollectable() {
        return collectable;
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

    GameObj(int id, double x, double y, double dir, String ImgPath, int ImgWidth, int ImgHeight, boolean collectable) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.ImgPath = ImgPath;
        this.ImgWidth = ImgWidth;
        this.ImgHeight = ImgHeight;
        this.collectable = collectable;
    }

    // 获取图像中心的x坐标
    abstract public double getCenterX();

    // 获取图像中心的y坐标
    abstract public double getCenterY();

    // 获取碰撞体积的width
    abstract public double getBoxWidth();

    // 获取碰撞体积的height
    abstract public double getBoxHeight();

}
