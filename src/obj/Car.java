package obj;

public class Car extends GameObj {

    private double speedx, speedy, maxSpeed, acceleratex, acceleratey;// 速度、最大行驶速度、加速度、方向（与y轴所成夹角）
    private double dir, rspeed, raccelerate, maxrSpeed;// 方向、角速度、角加速度、最大旋转角速度

    public double getSpeedX() {
        return speedx;
    }

    public double getAccelerateX() {
        return acceleratex;
    }

    public double getDir() {
        return dir;
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

    // 更新汽车每一时刻的数值
    public void update() {
        // 平动计算
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
        raccelerate = 0;
    }

    public Car(double x, double y, String ImgPath) {
        super(x, y, ImgPath);
        speedx = 0;
        speedy = 0;
        acceleratex = 0;
        acceleratey = 0;
        dir = 0;
        maxSpeed = 1.5;
        rspeed = 0;
        raccelerate = 0;
        maxrSpeed = 1.5;
    }
}
