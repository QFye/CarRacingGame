package obj;

public class Car extends GameObj {

    private double speedx, speedy, maxSpeed, acceleratex, acceleratey, dir;// 速度、最大行驶速度、加速度、方向（与y轴所成夹角）

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

    // 更新汽车每一时刻的数值
    public void update() {
        speedx += acceleratex;
        if (Math.abs(speedx) > 1.5)
            speedx = speedx > 0 ? 1.5 : -1.5;
        speedy += acceleratey;
        if (Math.abs(speedy) > 1.5)
            speedy = speedy > 0 ? 1.5 : -1.5;
        x += speedx;
        y += speedy;
        acceleratex = 0;
        acceleratey = 0;
    }

    public Car(double x, double y, String ImgPath) {
        super(x, y, ImgPath);
        speedx = 0;
        speedy = 0;
        acceleratex = 0;
        acceleratey = 0;
        dir = 0;
    }
}
