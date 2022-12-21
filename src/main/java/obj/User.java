package obj;

import java.net.*;

import net.sf.json.JSONObject;

public class User {
    private int id;
    private double x, y;
    private double dir;
    private String imgPath;
    private Socket socket;
    private boolean online;

    public void setAttribute(Socket socket, int id, double x, double y, double dir, String imgPath) {
        this.socket = socket;
        this.id = id;
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.imgPath = imgPath;
        this.online = true;
    }

    public void update(double x, double y, double dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public Socket getSocket() {
        return socket;
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

    public void writeInData(JSONObject data) {
        data.put("id", id);
        data.put("x", x);
        data.put("y", y);
        data.put("dir", dir);
        data.put("imgPath", imgPath);
    }
}
