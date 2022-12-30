package games;

import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;

import games.GameWin.GamePanel;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import obj.*;

public class GameClient implements Runnable {
    private Socket socket;// 套接字
    private boolean connectionState = false;// 连接状态
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private GamePanel panel;// 与面板交互数据
    private Car userInfo;// 客户端用户信息
    private boolean start = false;

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
                        userInfo = new Car();
                        userInfo.setName(data.getString("name"));
                        userInfo.setAttribute(null, data.getInt("id"), data.getDouble("x"), data.getDouble("y"),
                                data.getDouble("dir"), data.getString("imgPath"), data.getBoolean("online"),
                                data.getBoolean("ready"));
                        success = true;

                    } else if (data.getString("type").equals("reject")) {

                        // 提示用户失败原因
                        JOptionPane.showMessageDialog(panel, data.getString("reason"), "消息提示",
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

    @Override
    public synchronized void run() {
        try {
            // 连接到服务器端
            connect();

            // 判断是否连接成功
            if (connectionState) {

                // 利用接收的用户初始信息更新myCar的位置和id
                panel.myCar.setPosition(userInfo.getx(), userInfo.gety());
                panel.myCar.setDir(userInfo.getDir());
                panel.myCar.setid(userInfo.getId());
                panel.objectList.put(userInfo.getId(), userInfo);

                // 当处于游戏中时，不断执行
                while (GameWin.status != Status.Waiting) {

                    // 重绘
                    panel.repaint();

                    // 更新汽车信息并写入userInfo来据此进行通信
                    try {
                        outputStream = new DataOutputStream(socket.getOutputStream());
                        userInfo.updatePosition(panel.myCar.getx(), panel.myCar.gety(), panel.myCar.getDir());
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

                    if (start) {

                        // 判断是否达到胜利条件
                        if (panel.myCar.gety() <= 122 && GameWin.status != Status.Suceeded) {
                            // 更改状态
                            GameWin.status = Status.Suceeded;
                            // 客户端显示
                            System.out.println("You win");
                            JOptionPane.showMessageDialog(null, "恭喜您到达了终点", "消息提示", JOptionPane.INFORMATION_MESSAGE);
                            // 将胜利信息发送给服务器
                            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                            JSONObject data = new JSONObject();
                            data.put("type", "arrival");
                            data.put("name", userInfo.getName());
                            outputStream.writeUTF(data.toString());
                        }

                    } else {

                        // 判断是否更改准备状态
                        if (panel.isReadyStatusChanged()) {
                            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                            JSONObject data = new JSONObject();
                            data.put("type", "ready");
                            data.put("name", userInfo.getName());
                            outputStream.writeUTF(data.toString());
                            userInfo.setReady(!userInfo.isReady());
                            panel.setReadyStatusChanged(false);
                        }

                    }

                    // 线程休眠
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

                // 结束后关闭套接字
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
                while (GameWin.status != Status.Waiting) {
                    inputStream = new DataInputStream(socket.getInputStream());
                    String json = inputStream.readUTF();
                    JSONObject data = JSONObject.fromObject(json);

                    if (data.getString("type").equals("user")) {

                        // 接收玩家信息
                        Car user = new Car();
                        user.setName(data.getString("name"));
                        user.setAttribute(null, data.getInt("id"), data.getDouble("x"), data.getDouble("y"),
                                data.getDouble("dir"), data.getString("imgPath"), data.getBoolean("online"),
                                data.getBoolean("ready"));
                        panel.objectList.put(user.getId(), user);
                        panel.updatePlayerInfoPanel(user.getId());

                    } else if (data.getString("type").equals("msg")) {

                        // 接收消息类信息
                        // 打印信息
                        panel.chatPane.appendMsg(new Date() + "<br>" + data.getString("content"));
                        // 更改信息栏状态
                        if (data.getString("content").contains("加入服务器")) {
                            panel.playerInfos.get(data.getInt("id")).setStatusText("未准备");
                        } else if (data.getString("content").contains("准备就绪")) {
                            panel.playerInfos.get(data.getInt("id")).setStatusText("已准备");
                        } else if (data.getString("content").contains("取消了准备")) {
                            panel.playerInfos.get(data.getInt("id")).setStatusText("未准备");
                        } else if (data.getString("content").contains("断开连接")) {
                            panel.playerInfos.get(data.getInt("id")).setStatusText("离线");
                        }
                        // 打印服务器人数
                        if (data.containsKey("concurrent")) {
                            panel.chatPane.appendMsg(new Date() + "<br>服务器在线人数：" + data.getInt("concurrent"));
                        }

                    } else if (data.getString("type").equals("start")) {
                        // 接收开始信号后的处理
                        JSONArray array = data.getJSONArray("barrier");
                        for (Object o : array) {
                            JSONObject jsonobj = (JSONObject) o;
                            Barrier barrier = new Barrier(jsonobj.getInt("id"), jsonobj.getInt("x"),
                                    jsonobj.getInt("y"), jsonobj.getInt("width"), jsonobj.getInt("height"));
                            panel.objectList.put(barrier.getId(), barrier);
                        }
                        panel.playerInfos.get(userInfo.getId()).hideButton();
                        panel.countDown();
                        panel.myCar.setPosition(120 + panel.myCar.getId() * 120, 2800);
                        panel.myCar.setDir(0);
                        panel.myCar.clearSpeed();
                        start = true;
                    } else if (data.getString("type").equals("arrival")) {
                        // 接收到达终点后的信号处理
                        panel.chatPane.appendMsg(new Date() + "<br>" + data.getString("msg"));
                        if (data.getInt("id") == userInfo.getId()) {
                            panel.setTime(data.getLong("time"));
                            panel.setRank(data.getInt("rank"));
                        }
                        if (data.containsKey("settlement")) {
                            panel.generateSettlementPanel();
                            panel.getCardLayout().show(panel.getGameWinContainer(), "settlement");
                            connectionState = false;
                            start = false;
                            GameWin.status = Status.Waiting;
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
                while (GameWin.status != Status.Waiting) {
                    System.out.println("客户端：发送心跳包");
                    // 写入心跳包信息
                    outputStream = new DataOutputStream(socket.getOutputStream());
                    JSONObject data = new JSONObject();
                    data.put("type", "heart");
                    data.put("from", from);
                    outputStream.writeUTF(data.toString());
                    outputStream.flush();
                    // 每间隔7s生成一个心跳包对象并发送给服务器
                    try {
                        Thread.sleep(7000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("客户端：心跳包正常退出");
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
