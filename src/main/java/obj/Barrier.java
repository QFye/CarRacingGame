package obj;

public class Barrier extends GameObj {

    private static String path = "imgs/barrier1.png";

    // 获取图像中心的x坐标
    @Override
    public double getCenterX() {
        return x + (ImgWidth >> 1);
    }

    // 获取图像中心的y坐标
    @Override
    public double getCenterY() {
        return y + (ImgHeight >> 1);
    }

    // 获取碰撞体积的width
    @Override
    public double getBoxWidth() {
        return ImgWidth;
    }

    // 获取碰撞体积的height
    @Override
    public double getBoxHeight() {
        return ImgHeight;
    }

    public Barrier(int id, double x, double y, int ImgWidth, int ImgHeight) {
        super(id, x, y, 0, path, ImgWidth, ImgHeight);
    }

}
