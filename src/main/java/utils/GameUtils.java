package utils;

import java.awt.*;

public class GameUtils {

    // 获得汽车路径字符串
    public static String getCarPathString(int id) {
        return "imgs/car" + Integer.toString(id) + ".png";
    }

    // 获得物体图片
    public static Image getObjImg(String path) {
        String targetPath = GameUtils.class.getClassLoader().getResource(path).getPath();
        return Toolkit.getDefaultToolkit().getImage(targetPath);
    }

    // 获得背景图片
    public static Image getBgImg() {
        String path = GameUtils.class.getClassLoader().getResource("imgs/bg.jpg").getPath();
        return Toolkit.getDefaultToolkit().getImage(path);
    }

    // 获得主菜单背景图片
    public static Image getMenuBgImg() {
        String path = GameUtils.class.getClassLoader().getResource("imgs/menubg.jpeg").getPath();
        return Toolkit.getDefaultToolkit().getImage(path);
    }

    // 获得碰撞体积显示图片
    public static Image getCollisionDisplayerImg() {
        String path = GameUtils.class.getClassLoader().getResource("imgs/collision_displayer.png").getPath();
        return Toolkit.getDefaultToolkit().getImage(path);
    }

    public static int CarWidth = 35;
    public static int CarHeight = 70;

}
