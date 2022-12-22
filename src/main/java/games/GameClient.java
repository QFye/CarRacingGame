package games;

import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;

import games.GameWin.GamePanel;
import net.sf.json.JSONObject;

import java.io.*;
import obj.*;

public class GameClient implements Runnable {
    private Socket socket;// 套接字
    private boolean connectionState = false;// 连接状态
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private GamePanel panel;// 与面板交互数据
    private User userInfo;// 客户端用户信息

    GameClient(GamePanel panel) {
        this.panel = panel;
    }

    // 利用套接字连接到服务器
    private void connect() {
        try {
            // 连接套接字
            if (socket == null)
                socket = new Socket("localhost", 18888);

            // 通过用户名向服务器发送连接申请
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            JSONObject apply = new JSONObject();
            apply.put("type", "apply");
            apply.put("username", panel.getPlayerName());
            outputStream.writeUTF(apply.toString());

            // 等待服务器响应，成功则跳出
            boolean success = false;
            while (!success) {
                Thread.sleep(1000);
                try {
                    // 尝试接收初始用户信息
                    inputStream = new DataInputStream(socket.getInputStream());
                    String json = inputStream.readUTF();
                    JSONObject data = JSONObject.fromObject(json);
                    if (data.getString("type").equals("myInfo")) {
                        // 接收成功则更新本地用户信息
                        userInfo = new User();
                        userInfo.setName(data.getString("name"));
                        userInfo.setAttribute(null, data.getInt("id"), data.getDouble("x"), data.getDouble("y"),
                                data.getDouble("dir"), data.getString("imgPath"));
                        // System.out.println(
                        // "客户端：id = " + userInfo.getId() + ",x = " + userInfo.getX() + ",y = " +
                        // userInfo.getY());
                        success = true;
                    } else if (data.getString("type").equals("reject")) {
                        // 提示用户更改名称
                        JOptionPane.showMessageDialog(null, "该用户名已被注册，请更换名称后重试", "消息提示",
                                JOptionPane.INFORMATION_MESSAGE);
                        // 更换面板
                        panel.getCardLayout().show(panel.getGameWinContainer(), "menu");
                        return;
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

            // 判断是否连接成功
            if (connectionState) {

                // 利用接收的用户初始信息更新myCar的位置和id
                panel.myCar.setPosition(userInfo.getX(), userInfo.getY());
                panel.myCar.setDir(userInfo.getDir());
                panel.myCar.setid(userInfo.getId());
                panel.userList.put(userInfo.getId(), userInfo);

                // 当处于游戏中时，不断执行
                while (GameWin.status == Status.InGame) {

                    // 重绘
                    panel.repaint();

                    // 更新汽车信息并写入userInfo来据此进行通信
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

                    // 判断并发送聊天信息
                    if (panel.chatPane.isSend()) {
                        // 发送信息
                        JSONObject data = new JSONObject();
                        data.put("type", "msg");
                        data.put("content", "[" + userInfo.getName() + "] " + panel.chatPane.getSendContent());
                        outputStream.writeUTF(data.toString());
                        outputStream.flush();
                        // 重置发送状态
                        panel.chatPane.setSend(false);
                        panel.chatPane.resetContent();

                    }

                    // 判断是否达到胜利条件（随便写的，后面会改）
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
            // 断连处理
            try {
                JOptionPane.showInternalMessageDialog(null, "客户端与服务器断开连接", "消息提示", JOptionPane.INFORMATION_MESSAGE);
                panel.getCardLayout().show(panel.getGameWinContainer(), "menu");
                System.out.println("客户端：客户端异常断开连接");
                socket.close();
                connectionState = false;
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
                        // 接收玩家信息
                        User user = new User();
                        user.setName(data.getString("name"));
                        user.setAttribute(null, data.getInt("id"), data.getDouble("x"), data.getDouble("y"),
                                data.getDouble("dir"), data.getString("imgPath"));
                        panel.userList.put(user.getId(), user);
                    } else if (data.getString("type").equals("msg")) {
                        // 接收消息类信息
                        panel.chatPane.appendMsg(new Date() + "<br>" + data.getString("content"));
                        if (data.containsKey("concurrent")) {
                            panel.chatPane.appendMsg(new Date() + "<br>服务器在线人数：" + data.getInt("concurrent"));
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("客户端：数据接收失败");
            }

        }

    }

    // 心跳包线程
    class socketHeart implements Runnable {
        private int from;// 相关联的玩家编号

        socketHeart(int from) {
            this.from = from;
        }

        @Override
        public synchronized void run() {
            try {
                System.out.println("客户端：心跳包线程已启动");
                while (true) {
                    // 每间隔10s生成一个心跳包对象并发送给服务器
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("客户端：发送心跳包");
                    // 写入心跳包信息
                    outputStream = new DataOutputStream(socket.getOutputStream());
                    JSONObject data = new JSONObject();
                    data.put("type", "heart");
                    data.put("from", from);
                    outputStream.writeUTF(data.toString());
                    outputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                // 异常处理
                try {
                    JOptionPane.showInternalMessageDialog(null, "客户端与服务器断开连接", "消息提示", JOptionPane.INFORMATION_MESSAGE);
                    panel.getCardLayout().show(panel.getGameWinContainer(), "menu");
                    System.out.println("客户端：心跳包异常");
                    socket.close();
                    connectionState = false;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }

        }

    }

}
