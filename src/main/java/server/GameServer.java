package server;

import java.net.*;
import java.util.*;
import net.sf.json.JSONObject;
import java.io.*;
import obj.*;
import utils.GameUtils;

public class GameServer {

    // 用户列表
    public TreeMap<String, Car> userList = new TreeMap<>();
    // 在线用户列表
    public ArrayList<String> onlineList = new ArrayList<>();
    // 游戏是否开始
    private boolean isStart = false;

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

                    // 检测是否达到服务器人数上限
                    if (userList.size() >= 5) {
                        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                        JSONObject reject = new JSONObject();
                        reject.put("type", "reject");
                        reject.put("reason", "服务器人数已达上限");
                        outputStream.writeUTF(reject.toString());
                        // 检测游戏是否开始
                    } else if (isStart) {
                        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                        JSONObject reject = new JSONObject();
                        reject.put("type", "reject");
                        reject.put("reason", "游戏已开始");
                        outputStream.writeUTF(reject.toString());
                    } else {
                        Car newUser = new Car();
                        boolean success = true;
                        // 检测重名
                        if (userList.containsKey(apply.getString("username"))) {
                            newUser = userList.get(apply.getString("username"));
                            // 判断是否在线
                            if (newUser.isOnline()) {
                                // 将登录失败信息反馈给客户端
                                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                                JSONObject reject = new JSONObject();
                                reject.put("type", "reject");
                                reject.put("reason", "该玩家已进入服务器，登录失败");
                                outputStream.writeUTF(reject.toString());
                                success = false;
                            } else {
                                // 添加在线信息
                                newUser.setOnline(true);
                                newUser.setSocket(socket);
                                onlineList.add(newUser.getName());
                            }
                        } else {
                            // 分配用户信息（id和汽车位置等）
                            int currentUser = userList.size();
                            newUser.setName(apply.getString("username"));
                            newUser.setAttribute(socket, currentUser + 1, 120 + (currentUser + 1) * 120, 2800, 0,
                                    GameUtils.getCarPathString(currentUser + 1), true, false);
                            userList.put(newUser.getName(), newUser);
                            onlineList.add(newUser.getName());
                        }

                        if (success) {

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
                            String content = "玩家 " + newUser.getName() + " 加入服务器";
                            int id = newUser.getId();
                            userList.forEach((name, user) -> {
                                if (user.isOnline()) {
                                    try {
                                        DataOutputStream outputStream = new DataOutputStream(
                                                user.getSocket().getOutputStream());
                                        JSONObject data = new JSONObject();
                                        data.put("type", "msg");
                                        data.put("content", content);
                                        data.put("concurrent", onlineList.size());
                                        data.put("id", id);
                                        outputStream.writeUTF(data.toString());
                                        outputStream.flush();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("服务器端：服务器断开连接");
            userList.forEach((name, user) -> {
                try {
                    // 关闭用户套接字
                    user.getSocket().close();

                    // 判断是否在线
                    if (user.isOnline()) {
                        // 向在线用户发送断连消息
                        DataOutputStream outputStream = new DataOutputStream(user.getSocket().getOutputStream());
                        JSONObject data = new JSONObject();
                        data.put("type", "msg");
                        data.put("content", "服务器断开连接");
                        outputStream.writeUTF(data.toString());
                        outputStream.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            userList.clear();
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 线程类:监听该用户的信息并发送给其他客户端
    class ServerThread implements Runnable {

        // 线程对应用户
        private Car curUser;

        public ServerThread(Car curUser) {
            this.curUser = curUser;
        }

        @Override
        public synchronized void run() {
            try {

                while (curUser.isOnline()) {

                    // 从客户端读取数据
                    DataInputStream inputStream = new DataInputStream(curUser.getSocket().getInputStream());
                    String json = inputStream.readUTF();
                    JSONObject data = JSONObject.fromObject(json);

                    // 读入客户端数据
                    // 处理心跳包信息
                    if (data.getString("type").equals("heart")) {
                        int heartsource = data.getInt("from");
                        System.out.println("服务器端：接收玩家" + heartsource + "的心跳包");
                    } else if (data.getString("type").equals("msg")) {
                        // 处理消息发送
                        userList.forEach((name, user) -> {
                            // 判断是否在线
                            if (user.isOnline()) {
                                try {
                                    // 向在线用户发送断连消息
                                    DataOutputStream outputStream = new DataOutputStream(
                                            user.getSocket().getOutputStream());
                                    outputStream.writeUTF(json);
                                    outputStream.flush();
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                            }
                        });
                    } else if (data.getString("type").equals("user")) {
                        // 处理用户信息及聊天信息
                        // 向每个客户端发送数据
                        for (int i = 0; i < onlineList.size(); i++) {
                            // 不在线则跳过
                            if (!userList.get(onlineList.get(i)).isOnline())
                                continue;
                            try {
                                DataOutputStream outputStream = new DataOutputStream(userList.get(
                                        onlineList.get(i)).getSocket().getOutputStream());
                                outputStream.writeUTF(json);
                                outputStream.flush();
                            } catch (Exception e) {
                                // 固定发送消息
                                String content = "玩家 " + onlineList.get(i) + " 断开连接";
                                int id = userList.get(onlineList.get(i)).getId();
                                // 更改用户连接状态
                                userList.get(onlineList.get(i)).setOnline(false);
                                onlineList.remove(i);
                                // 向其他用户发送断连提示
                                userList.forEach((name, user) -> {
                                    // 判断是否在线
                                    if (user.isOnline()) {
                                        try {
                                            // 向在线用户发送断连消息
                                            DataOutputStream outputStream = new DataOutputStream(
                                                    user.getSocket().getOutputStream());
                                            JSONObject msg = new JSONObject();
                                            msg.put("type", "msg");
                                            msg.put("content", content);
                                            msg.put("concurrent", onlineList.size());
                                            msg.put("id", id);
                                            outputStream.writeUTF(msg.toString());
                                            outputStream.flush();
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                    } else if (data.getString("type").equals("ready")) {

                        // 更改准备状态
                        String curName = data.getString("name");
                        userList.get(curName).setReady(!userList.get(curName).isReady());
                        String content = "玩家 " + curName
                                + (userList.get(curName).isReady() ? " 已准备就绪" : " 取消了准备");
                        int id = userList.get(data.getString("name")).getId();

                        // 向其他用户发送消息
                        userList.forEach((name, user) -> {
                            // 判断是否在线
                            if (user.isOnline()) {
                                try {
                                    // 向在线用户发送断连消息
                                    DataOutputStream outputStream = new DataOutputStream(
                                            user.getSocket().getOutputStream());
                                    JSONObject msg = new JSONObject();
                                    msg.put("type", "msg");
                                    msg.put("content", content);
                                    msg.put("id", id);
                                    outputStream.writeUTF(msg.toString());
                                    outputStream.flush();
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                            }
                        });

                        // 判断游戏是否开始
                        int preparedCount = 0;
                        Iterator<Car> it = userList.values().iterator();
                        while (it.hasNext()) {
                            Car user = it.next();
                            if (user.isReady()) {
                                preparedCount++;
                            }
                        }
                        System.out.println(preparedCount);
                        // 若准备人数超过两人且与注册人数相等时开始游戏
                        if (preparedCount >= 2 && preparedCount == userList.size()) {
                            isStart = true;
                            userList.forEach((name, user) -> {
                                // 判断是否在线
                                if (user.isOnline()) {
                                    try {
                                        // 向客户端发送游戏开始的请求
                                        DataOutputStream outputStream = new DataOutputStream(
                                                user.getSocket().getOutputStream());
                                        JSONObject start = new JSONObject();
                                        start.put("type", "start");
                                        outputStream.writeUTF(start.toString());
                                        outputStream.flush();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
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
