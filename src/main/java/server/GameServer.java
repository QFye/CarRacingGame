package server;

import java.net.*;
import java.util.*;
import net.sf.json.JSONObject;
import java.io.*;
import obj.*;

public class GameServer {

    // 用户列表
    public ArrayList<User> userList = new ArrayList<>();
    // 在线用户列表
    public ArrayList<String> onlineList = new ArrayList<>();

    public static void main(String[] args) {
        new GameServer();
    }

    GameServer() {
        ServerSocket server = null;
        try {
            // 通过18888端口连接
            server = new ServerSocket(18888);
            System.out.println("服务器端：服务器开始运行");
            Socket socket = null;
            while (true) {
                // 接收用户连接（阻塞）
                socket = server.accept();

                // 读取用户申请
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                JSONObject apply = JSONObject.fromObject(inputStream.readUTF());

                // 将当前用户信息加入列表中
                if (apply.getString("type").equals("apply")) {
                    // 检测是否重名
                    boolean isRegistered = false;
                    for (User user : userList) {
                        if (user.getName().equals(apply.getString("username"))) {
                            isRegistered = true;
                            break;
                        }
                    }
                    // 若重名，则反馈客户端
                    if (isRegistered) {
                        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                        JSONObject reject = new JSONObject();
                        reject.put("type", "reject");
                        outputStream.writeUTF(reject.toString());
                    } else {
                        // 分配用户信息（id和汽车位置等）
                        int currentUser = userList.size();
                        User newUser = new User();
                        newUser.setName(apply.getString("username"));
                        newUser.setAttribute(socket, currentUser + 1, 120 + (currentUser + 1) * 120, 2800, 0,
                                "imgs/car.bmp");
                        userList.add(newUser);
                        onlineList.add(newUser.getName());

                        // 向客户端发送当前玩家信息
                        DataOutputStream curOutputStream = new DataOutputStream(socket.getOutputStream());
                        JSONObject myData = new JSONObject();
                        myData.put("type", "myInfo");
                        newUser.writeInData(myData);
                        curOutputStream.writeUTF(myData.toString());
                        curOutputStream.flush();

                        // 连接成功后创建服务器端线程
                        new Thread(new ServerThread(newUser)).start();

                        // 向所有客户端发送玩家上线信息及游戏人数
                        for (User user : userList) {
                            DataOutputStream outputStream = new DataOutputStream(user.getSocket().getOutputStream());
                            JSONObject data = new JSONObject();
                            data.put("type", "msg");
                            data.put("content", "玩家 " + newUser.getName() + " 加入服务器");
                            data.put("concurrent", onlineList.size());
                            outputStream.writeUTF(data.toString());
                            outputStream.flush();
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("服务器端：服务器断开连接");
                for (User user : userList) {
                    // 关闭用户套接字
                    user.getSocket().close();

                    // 判断是否在线
                    if (!user.isOnline())
                        continue;

                    // 向在线用户发送断连消息
                    DataOutputStream outputStream = new DataOutputStream(user.getSocket().getOutputStream());
                    JSONObject data = new JSONObject();
                    data.put("type", "msg");
                    data.put("content", "服务器断开连接");
                    outputStream.writeUTF(data.toString());
                    outputStream.flush();
                }
                userList.clear();
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 线程类:监听该用户的信息并发送给其他客户端
    class ServerThread implements Runnable {

        // 线程对应用户
        private User curUser;

        public ServerThread(User curUser) {
            this.curUser = curUser;
        }

        @Override
        public synchronized void run() {
            try {

                while (true) {

                    // 从客户端读取数据
                    DataInputStream inputStream = new DataInputStream(curUser.getSocket().getInputStream());
                    String json = inputStream.readUTF();
                    JSONObject data = JSONObject.fromObject(json);

                    // 读入客户端数据
                    // 处理心跳包信息
                    if (data.getString("type").equals("heart")) {
                        int heartsource = data.getInt("from");
                        System.out.println("服务器端：接收玩家" + heartsource + "的心跳包");
                    } else {
                        // 处理用户信息及聊天信息
                        // 向每个客户端发送数据
                        for (int i = 0; i < userList.size(); i++) {
                            // 不在线则跳过
                            if (!userList.get(i).isOnline())
                                continue;
                            try {
                                DataOutputStream outputStream = new DataOutputStream(
                                        userList.get(i).getSocket().getOutputStream());
                                outputStream.writeUTF(json);
                                outputStream.flush();
                            } catch (Exception e) {
                                // 更改用户连接状态
                                userList.get(i).setOnline(false);
                                onlineList.remove(userList.get(i).getName());
                                // 向其他用户发送断连提示
                                for (User user : userList) {
                                    // 判断是否在线
                                    if (!user.isOnline())
                                        continue;
                                    // 向在线用户发送断连消息
                                    DataOutputStream outputStream = new DataOutputStream(
                                            user.getSocket().getOutputStream());
                                    JSONObject msg = new JSONObject();
                                    msg.put("type", "msg");
                                    msg.put("content", "玩家 " + userList.get(i).getName() + " 断开连接");
                                    msg.put("concurrent", onlineList.size());
                                    outputStream.writeUTF(msg.toString());
                                    outputStream.flush();
                                }
                            }
                        }
                    }

                    // 线程休眠
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    // 服务器异常断开时将套接字关闭，避免浪费资源
                    System.out.println("服务器端：服务器线程异常断开");
                    curUser.setOnline(false);
                    curUser.getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
