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
        double w = GameUtils.CarWidth, h = GameUtils.CarHeight;
        double s = Math.sqrt(w * w + h * h) / 2;
        double c = Math.atan(w / h);
        double a = c - dir;
        return x + s * Math.sin(Math.toRadians(a));
    }

    // 获取图像中心的y坐标
    public double getCenterY() {
        double w = GameUtils.CarWidth, h = GameUtils.CarHeight;
        double s = Math.sqrt(w * w + h * h) / 2;
        double c = Math.atan(w / h);
        double a = c - dir;
        return y + s * Math.cos(Math.toRadians(a));
    }

    // 获取碰撞体积的width
    public double getBoxWidth() {
        return GameUtils.CarWidth * Math.cos(Math.toRadians(dir)) + GameUtils.CarHeight * Math.sin(Math.toRadians(dir));
    }

    // 获取碰撞体积的height
    public double getBoxHeight() {
        return GameUtils.CarWidth * Math.sin(Math.toRadians(dir)) + GameUtils.CarHeight * Math.cos(Math.toRadians(dir));
    }

    // 判断是否碰撞
    public boolean isCollidingWith(User another) {
        if (Math.abs(getCenterX() - another.getCenterX()) <= (getBoxWidth() / 2) + (another.getBoxWidth() / 2)
                && Math.abs(getCenterY() - another.getCenterY()) <= (getBoxHeight() / 2)
                        + (another.getBoxHeight() / 2)) {
            System.out.println("crush");
            return true;
        } else {
            return false;
        }
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

    public void update(double x, double y, double dir) {
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
