package games;

import java.net.*;

import games.GameWin.GamePanel;
import net.sf.json.JSONObject;

import java.io.*;
import obj.*;

public class GameClient implements Runnable {
    private Socket socket;
    public boolean connectionState = false;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private GamePanel panel;
    public static int curConnections = 0;
    public User userInfo;

    GameClient(GamePanel panel) {
        this.panel = panel;
    }

    // 利用套接字连接到服务器
    private void connect() {
        try {
            // 连接套接字
            socket = new Socket("localhost", 18888);

            // 等待服务器响应
            boolean success = false;
            while (!success) {
                Thread.sleep(1000);
                try {
                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                    String json = inputStream.readUTF();
                    JSONObject data = JSONObject.fromObject(json);
                    if (data.getString("type").equals("myInfo")) {
                        userInfo = new User();
                        userInfo.setAttribute(null, data.getInt("id"), data.getDouble("x"), data.getDouble("y"),
                                data.getDouble("dir"), data.getString("imgPath"));
                        System.out.println(
                                "客户端：id = " + userInfo.getId() + ",x = " + userInfo.getX() + ",y = " + userInfo.getY());
                        success = true;
                    }
                } catch (Exception e) {
                    success = false;
                    e.printStackTrace();
                }
            }

            // 创建客户端读取线程
            new Thread(new socketListenThread()).start();

            // 创建心跳包线程
            new Thread(new socketHeart(userInfo.getId())).start();

            // 更改连接状态
            this.connectionState = true;
            System.out.println("客户端：连接服务器成功！");
        } catch (Exception e) {
            e.printStackTrace();
            this.connectionState = false;
            System.out.println("客户端：连接失败");
        }
    }

    // 重连服务器
    public void reconnect() {
        while (!connectionState) {
            System.out.println("客户端：正在尝试重新连接");
            connect();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public synchronized void run() {
        try {
            // 连接到服务器端
            connect();

            if (connectionState) {

                panel.myCar.setPosition(userInfo.getX(), userInfo.getY());
                panel.myCar.setid(userInfo.getId());
                panel.userList.put(userInfo.getId(), userInfo);

                // 当处于游戏中时，不断执行
                while (GameWin.status == Status.InGame) {

                    // 重绘
                    panel.repaint();

                    // 将汽车信息写入userInfo并据此进行通信
                    try {
                        outputStream = new DataOutputStream(socket.getOutputStream());
                        userInfo.update(panel.myCar.getx(), panel.myCar.gety(), panel.myCar.getDir());
                        JSONObject data = new JSONObject();

                        data.put("type", "user");
                        userInfo.writeInData(data);
                        outputStream.writeUTF(data.toString());
                        outputStream.flush();
                    } catch (NotActiveException e) {
                        continue;
                    }
                    // System.out.println("客户端：发送数据{id:" + panel.myCar.getid() + ",x:" +
                    // panel.myCar.getx() + ",y:"
                    // + panel.myCar.gety() + ",dir:"
                    // + panel.myCar.getDir() + "}");

                    // 判断是否达到胜利条件
                    if (panel.myCar.gety() < -100) {
                        GameWin.status = Status.Suceeded;
                        System.out.println("You win");
                    }

                    // 线程休眠
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

                // 结束后关闭流和套接字
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            try {
                System.out.println("客户端：客户端异常断开连接");
                socket.close();
                connectionState = false;
                reconnect();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }

    // 客户端监听类
    class socketListenThread implements Runnable {

        @Override
        public synchronized void run() {
            try {
                // 从服务端读取数据
                while (GameWin.status == Status.InGame) {
                    inputStream = new DataInputStream(socket.getInputStream());
                    String json = inputStream.readUTF();
                    JSONObject data = JSONObject.fromObject(json);

                    if (data.getString("type").equals("user")) {
                        // 接收每个玩家位置
                        User user = new User();
                        user.setAttribute(null, data.getInt("id"), data.getDouble("x"), data.getDouble("y"),
                                data.getDouble("dir"), data.getString("imgPath"));
                        System.out.println("客户端：接收玩家" + user.getId() + "的信息");
                        panel.userList.put(user.getId(), user);
                    } else if (data.getString("type").equals("msg")) {
                        System.out.println(data.getString("content"));
                        if (data.containsKey("concurrent")) {
                            System.out.println("客户端：服务器在线人数：" + data.getInt("concurrent"));
                        }
                    }

                    // 接收当前在线人数
                    // int concurrentPlayer = (int) data.get("concurrent");
                    // System.out.println("当前在线人数：" + concurrentPlayer);

                    // 接收信息
                    // String msg = (String) data.get("msg");
                    // System.out.println("客户端：" + msg);
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("客户端：数据接收失败");
            }

        }

    }

    // 心跳包线程
    class socketHeart implements Runnable {
        private int from;

        socketHeart(int from) {
            this.from = from;
        }

        @Override
        public synchronized void run() {
            try {
                System.out.println("客户端：心跳包线程已启动");
                while (true) {
                    // 每间隔8s生成一个心跳包对象并发送给服务器
                    try {
                        Thread.sleep(8000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("客户端：发送心跳包");
                    outputStream = new DataOutputStream(socket.getOutputStream());
                    JSONObject data = new JSONObject();
                    data.put("type", "heart");
                    data.put("from", from);
                    outputStream.writeUTF(data.toString());
                    outputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    System.out.println("客户端：心跳包异常");
                    socket.close();
                    connectionState = false;
                    reconnect();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }

        }

    }

}
