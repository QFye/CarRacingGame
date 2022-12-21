CAR RACING DEMO
============================

## 环境依赖
<<<<<<< HEAD
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
---

## 版本更新
---
=======
开发语言：Java

## 目录结构描述
D:.\
│&emsp;  README.md\
│  \
├─data\
│&emsp;  tree.txt\
│      \
├─imgs\
│&emsp;  bg.jpg\
│&emsp;  car.bmp\
│      \
└─src\
&emsp;&emsp;  │ &emsp; GamePanel.java\
&emsp;&emsp;  │ &emsp; GameWin.java\
&emsp;&emsp;  │ &emsp; LoginPanel.java\
&emsp;&emsp;    │  \
&emsp;&emsp;    ├─obj\
&emsp;&emsp;    │ &emsp;     Car.java\
&emsp;&emsp;    │ &emsp;     GameObj.java\
&emsp;&emsp;    │      \
&emsp;&emsp;    └─utils\
&emsp;&emsp;&emsp;&emsp;           GameUtils.java
>>>>>>> 3e8aa70bd30f8375f8a918b956def5dfe030944b

###### $Test\ V0.0.1\ 版本内容更新$
1. 建立了软件初始架构
2. 搭建了初始界面

<<<<<<< HEAD
---

=======
>>>>>>> 3e8aa70bd30f8375f8a918b956def5dfe030944b
###### $Test\ V0.0.2\ 版本内容更新$
1. 优化了软件架构，增加了对LoginPanel和GamePanel的编写
2. 完成了基类GameObj的代码编写，实现了子类Car的部分方法
3. 实现了面板跳转
4. 实现了对键盘的监听，现在可以控制汽车上下方向移动
<<<<<<< HEAD
   
---
=======
>>>>>>> 3e8aa70bd30f8375f8a918b956def5dfe030944b

###### $Test\ V0.0.3\ 版本内容更新$
1. 实现了物理引擎，为汽车添加加速度、旋转属性等，并完成状态更新的编写
2. 实现了多键位键盘监听，现在可以控制汽车向各个方向移动了
<<<<<<< HEAD
   
---

###### $Test\ V0.1.0\ 版本内容更新$
1. 使用maven重构了项目结构
2. 实现了服务器，并添加了用户类，通过用户类实现服务器与客户端的通信
   
---

## 待更新或修复内容
1. 服务器现存bug：先进入服务器的玩家可以控制后进入服务器玩家的车移动
2. 移动控制从车移动转换为场景移动
3. 添加物体碰撞判定与其他障碍物或道具
4. 用户可以自行设置局内昵称
5. 添加局内信息提示框（聊天栏）
=======
>>>>>>> 3e8aa70bd30f8375f8a918b956def5dfe030944b
