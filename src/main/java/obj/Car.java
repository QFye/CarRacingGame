package obj;

import java.net.*;

import net.sf.json.JSONObject;
import utils.GameUtils;

public class Car extends GameObj {
    private String userName;
    private Socket socket;
    private boolean online;
    private boolean ready;

    // 获取图像中心的x坐标
    @Override
    public double getCenterX() {
        return x + (GameUtils.CarWidth >> 1);
    }

    // 获取图像中心的y坐标
    @Override
    public double getCenterY() {
        return y + (GameUtils.CarHeight >> 1);
    }

    // 获取碰撞体积的width
    @Override
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
    @Override
    public double getBoxHeight() {
        double b = dir;
        if (b >= 180)
            b -= 180;
        if (b >= 90) {
            b -= (b - 90) * 2;
        }
        return GameUtils.CarWidth * Math.sin(Math.toRadians(b)) + GameUtils.CarHeight * Math.cos(Math.toRadians(b));
    }

    public void setName(String userName) {
        this.userName = userName;
    }

    public void setAttribute(Socket socket, int id, double x, double y, double dir, String imgPath, boolean online,
            boolean ready) {
        this.socket = socket;
        this.id = id;
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.ImgPath = imgPath;
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
        return userName;
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
        data.put("name", userName);
        data.put("id", id);
        data.put("x", x);
        data.put("y", y);
        data.put("dir", dir);
        data.put("imgPath", ImgPath);
        data.put("ready", ready);
        data.put("online", online);
    }

    public Car(int id) {
        super(id, 0, 0, 0, null, GameUtils.CarWidth, GameUtils.CarHeight);
    }

    public Car() {
        super(1, 0, 0, 0, null, GameUtils.CarWidth, GameUtils.CarHeight);
    }
}
