package utils;

import javax.swing.*;

public class GameUtils {

    private static ImageIcon bgImg = new ImageIcon("imgs/bg.jpg");
    private static ImageIcon carImg = new ImageIcon("imgs/car.jpg");

    // 获得车图片
    public static ImageIcon getCarImg() {
        return carImg;
    }

    // 获得背景图片
    public static ImageIcon getBgImg() {
        return bgImg;
    }

}
