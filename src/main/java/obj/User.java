package obj;

import java.net.*;

import net.sf.json.JSONObject;

public class User {
    private String name;
    private int id;
    private double x, y;
    private double dir;
    private String imgPath;
    private Socket socket;
    private boolean online;

    public void setName(String name) {
        this.name = name;
    }

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

    public void writeInData(JSONObject data) {
        data.put("name", name);
        data.put("id", id);
        data.put("x", x);
        data.put("y", y);
        data.put("dir", dir);
        data.put("imgPath", imgPath);
    }
}
