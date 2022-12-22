CAR RACING DEMO
============================

## 环境依赖
开发语言：Java\
项目管理：maven\
外部依赖：net.sf.json-lib（见pom.xml）

## 目录结构描述
    D:.
    │  pom.xml
    │  README.md
    │      
    ├─src
    │  ├─main
    │  │  ├─java
    │  │  │  ├─games
    │  │  │  │      GameClient.java
    │  │  │  │      GameWin.java
    │  │  │  │      Status.java
    │  │  │  │      
    │  │  │  ├─obj
    │  │  │  │      Car.java
    │  │  │  │      GameObj.java
    │  │  │  │      User.java
    │  │  │  │      
    │  │  │  ├─server
    │  │  │  │      GameServer.java
    │  │  │  │      
    │  │  │  └─utils
    │  │  │          GameUtils.java
    │  │  │          
    │  │  └─resources
    │  │      ├─data
    │  │      │      tree.txt
    │  │      │      
    │  │      └─imgs
    │  │              bg.jpg
    │  │              car.bmp
    │  │              
    │  └─test
    │      └─java
    │          └─games
    │                  AppTest.java
    │                  Student.java
    │                  test.java
    │                  
    └─target
        ├─classes
        │  ├─data
        │  │      tree.txt
        │  │      
        │  ├─games
        │  │      GameClient$socketHeart.class
        │  │      GameClient$socketListenThread.class
        │  │      GameClient.class
        │  │      GameWin$GamePanel$1.class
        │  │      GameWin$GamePanel.class
        │  │      GameWin$MenuPanel$1.class
        │  │      GameWin$MenuPanel.class
        │  │      GameWin.class
        │  │      Status.class
        │  │      
        │  ├─imgs
        │  │      bg.jpg
        │  │      car.bmp
        │  │      
        │  ├─obj
        │  │      Car.class
        │  │      GameObj.class
        │  │      User.class
        │  │      
        │  ├─server
        │  │      GameServer$ServerThread.class
        │  │      GameServer.class
        │  │      
        │  └─utils
        │          GameUtils.class
        │          
        └─test-classes
            └─games
                    AppTest.class
                    Student.class
                    test.class

## 版本更新
---
###### $Test\ V0.0.1\ 版本内容更新$
1. 建立了软件初始架构
2. 搭建了初始界面
   
---

###### $Test\ V0.0.1\ 版本内容更新$
1. 建立了软件初始架构
2. 搭建了初始界面

---

###### $Test\ V0.0.2\ 版本内容更新$
1. 优化了软件架构，增加了对LoginPanel和GamePanel的编写
2. 完成了基类GameObj的代码编写，实现了子类Car的部分方法
3. 实现了面板跳转
4. 实现了对键盘的监听，现在可以控制汽车上下方向移动
   
---

###### $Test\ V0.0.3\ 版本内容更新$
1. 实现了物理引擎，为汽车添加加速度、旋转属性等，并完成状态更新的编写
2. 实现了多键位键盘监听，现在可以控制汽车向各个方向移动了
   
---

###### $Test\ V0.1.0\ 版本内容更新$
1. 使用maven重构了项目结构
2. 实现了服务器，并添加了用户类，通过用户类实现服务器与客户端的通信
   
---

##### $Test\ V0.1.1\ 版本内容更新$
1. 修复了汽车控制的bug
2. 增加了聊天栏，按T键可以显示或隐藏聊天栏，玩家可以通过聊天栏向其他玩家发送信息，也可以再次点击游戏界面获取键盘焦点
3. 每当有用户加入或退出服务器时，服务器都会通过聊天栏将信息发送给所有用户
4. 增加了用户名设置，玩家可以在汽车上方看到对应玩家的用户名
5. 新增了客户端断连后的消息框提示
6. 新增了在线用户列表，现在的在线人数更加准确了

---

##### $Test\ V0.1.2\ 版本内容更新$
1. 更改了背景素材
2. 实现了场景跟随汽车移动
3. 对用户名注册时添加了重名检测

## 待更新或修复内容
1. 移动控制从车移动转换为场景移动
2. 添加物体碰撞判定与其他障碍物或道具
3. 游戏设置面板及功能
4. 游戏美术素材
