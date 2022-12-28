package obj;

import java.net.*;

import net.sf.json.JSONObject;
import utils.GameUtils;

public class User {
    private String name;
    private int id;
    private double x, y;
    private double dir;
    private String imgPath;
    private Socket socket;
    private boolean online;
    private boolean ready;

    // 获取图像中心的x坐标
    public double getCenterX() {
        return x + (GameUtils.CarWidth >> 1);
    }

    // 获取图像中心的y坐标
    public double getCenterY() {
        return y + (GameUtils.CarHeight >> 1);
    }

    // 获取碰撞体积的width
    public double getBoxWidth() {
        double b = dir;
        if (b >= 180)
            b -= 180;
        if (b >= 90) {
            b -= (b - 90) * 2;
        }
        return GameUtils.CarWidth * Math.cos(Math.toRadians(b)) + GameUtils.CarHeight * Math.sin(Math.toRadians(b));
    }

    // 获取碰撞体积的height
    public double getBoxHeight() {
        double b = dir;
        if (b >= 180)
            b -= 180;
        if (b >= 90) {
            b -= (b - 90) * 2;
        }
        return GameUtils.CarWidth * Math.sin(Math.toRadians(b)) + GameUtils.CarHeight * Math.cos(Math.toRadians(b));
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttribute(Socket socket, int id, double x, double y, double dir, String imgPath, boolean online,
            boolean ready) {
        this.socket = socket;
        this.id = id;
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.imgPath = imgPath;
        this.online = online;
        this.ready = ready;
    }

    public void updatePosition(double x, double y, double dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDir() {
        return dir;
    }

    public String getImgPath() {
        return imgPath;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean status) {
        this.online = status;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void writeInData(JSONObject data) {
        data.put("name", name);
        data.put("id", id);
        data.put("x", x);
        data.put("y", y);
        data.put("dir", dir);
        data.put("imgPath", imgPath);
        data.put("ready", ready);
        data.put("online", online);
    }

    public User(int id) {
        this.id = id;
    }

    public User() {

    }
}
