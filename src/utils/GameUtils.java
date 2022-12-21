package utils;

import java.awt.*;

public class GameUtils {

    private static Image bgImg = Toolkit.getDefaultToolkit().getImage("imgs/bg.jpg");

    // 获得物体图片
    public static Image getObjImg(String path) {
        return Toolkit.getDefaultToolkit().getImage(path);
    }

    // 获得背景图片
    public static Image getBgImg() {
        return bgImg;
    }

}
