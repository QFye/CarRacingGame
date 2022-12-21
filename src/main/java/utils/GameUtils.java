package utils;

import java.awt.*;

public class GameUtils {

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

    public static int CarWidth = 50;
    public static int CarHeight = 70;

}