package server;

import java.net.*;
import java.util.*;

import net.sf.json.JSONObject;

import java.io.*;
import obj.*;

public class GameServer {

    public ArrayList<User> userList = new ArrayList<>();

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

                // 将当前用户信息加入列表中
                int currentUser = userList.size();
                User newUser = new User();
                newUser.setAttribute(socket, currentUser + 1, 140 + (currentUser + 1) * 150, 600, 0, "imgs/car.bmp");
                userList.add(newUser);
                System.out.println("服务器端：服务器与客户端成功连接,当前人数：" + userList.size());

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
                    data.put("content", "玩家" + user.getId() + "已上线");
                    data.put("concurrent", userList.size());
                    outputStream.writeUTF(data.toString());
                    outputStream.flush();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("服务器端：服务器断开连接");
                for (User user : userList) {
                    user.getSocket().close();
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
                    if (data.getString("type").equals("heart")) {
                        int heartsource = data.getInt("from");
                        System.out.println("服务器端：接收玩家" + heartsource + "的心跳包");
                    } else {

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
                                userList.get(i).setOnline(false);
                                System.out.println("服务器端：玩家" + i + "断开连接");
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
