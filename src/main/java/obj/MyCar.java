package obj;

import java.util.*;

public class MyCar extends Car {

    private double speedx, speedy, maxSpeed, acceleratex, acceleratey;// 速度、最大行驶速度、加速度、方向（与y轴所成夹角）
    private double rspeed, raccelerate, maxrSpeed;// 方向、角速度、角加速度、最大旋转角速度
    private double aunit = 0.1;// 加速时每帧增加的最小加速度

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getAunit() {
        return aunit;
    }

    public void setAunit(double aunit) {
        this.aunit = aunit;
    }

    public double getSpeedX() {
        return speedx;
    }

    public double getAccelerateX() {
        return acceleratex;
    }

    public double getSpeedy() {
        return speedy;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getAcceleratey() {
        return acceleratey;
    }

    public double getSpeedx() {
        return speedx;
    }

    public double getAcceleratex() {
        return acceleratex;
    }

    public double getRspeed() {
        return rspeed;
    }

    public double getRaccelerate() {
        return raccelerate;
    }

    public double getMaxrSpeed() {
        return maxrSpeed;
    }

    // 更新x和y上的加速度
    public void setAccelerate(double ax, double ay) {
        acceleratex = ax;
        acceleratey = ay;
    }

    public void setrAccelerate(double a) {
        raccelerate = a;
    }

    public void dirRotate(double d) {
        dir = (dir + 360 + d) % 360;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void clearSpeed() {
        speedx = 0;
        speedy = 0;
        acceleratex = 0;
        acceleratey = 0;
        maxSpeed = 1.5;
        rspeed = 0;
        raccelerate = 0;
        maxrSpeed = 1.5;
    }

    public void setid(int id) {
        this.id = id;
    }

    public void setDir(double dir) {
        this.dir = dir;
    }

    // 更新汽车每一时刻的数值
    public void update(TreeMap<Integer, GameObj> objectList) {
        // 平动计算
        double lastX = x, lastY = y, lastDir = dir;
        speedx *= 0.99;// 设定一定阻力
        speedx += acceleratex;
        if (Math.abs(speedx) > maxSpeed)// 最大限速
            speedx = speedx > 0 ? maxSpeed : -maxSpeed;
        speedy *= 0.99;
        speedy += acceleratey;
        if (Math.abs(speedy) > maxSpeed)
            speedy = speedy > 0 ? maxSpeed : -maxSpeed;
        x += speedx;
        y += speedy;
        acceleratex = 0;
        acceleratey = 0;

        // 转动计算
        rspeed *= 0.99;
        rspeed += raccelerate;
        if (Math.abs(rspeed) > maxrSpeed)
            rspeed = rspeed > 0 ? maxrSpeed : -maxrSpeed;
        dir += rspeed;
        dir = (dir + 360) % 360;
        raccelerate = 0;

        // 判断是否到达边界
        if (getx() <= 142 || getx() + getBoxWidth() >= 900 || gety() <= 100
                || gety() + getBoxHeight() >= 2890) {
            acceleratex = 0;
            speedx = 0;
            x = lastX;
            dir = lastDir;
            acceleratey = 0;
            speedy = 0;
            y = lastY;
            return;
        }

        // 判断是否与其他物体碰撞
        Iterator<GameObj> it = objectList.values().iterator();
        while (it.hasNext()) {
            GameObj another = it.next();
            if (another.getId() == id) {
                continue;
            }
            if (Math.abs(another.getCenterX() - getCenterX()) < (getBoxWidth() / 2) + (another.getBoxWidth() / 2)
                    && Math.abs(another.getCenterY() - getCenterY()) < (getBoxHeight() / 2)
                            + (another.getBoxHeight() / 2)) {
                if (another.isCollectable()) {
                    /* 道具类实现 */
                } else {
                    // 如果相撞，回退至上一次的状态
                    acceleratex = 0;
                    speedx = 0;
                    x = lastX;
                    dir = lastDir;
                    acceleratey = 0;
                    speedy = 0;
                    y = lastY;
                    break;
                }
            }
        }

    }

    @Override
    public void setAttribute(int id, double x, double y, double dir, String ImgPath, int ImgWidth, int ImgHeight) {
        super.setAttribute(null, id, x, y, dir, ImgPath, true, false);
        clearSpeed();
    }

}