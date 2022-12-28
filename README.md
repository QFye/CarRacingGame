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
    │  │              car1.png
    │  │              car2.png
    │  │              car3.png
    │  │              car4.png
    │  │              car5.png
    |  |              menubg.jpeg
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
        │  │      car1.png
        │  │      car2.png
        │  │      car3.png
        │  │      car4.png
        │  │      car5.png
        |  |      menubg.jpeg
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

---

##### $Test\ V0.1.3\ 版本内容更新$
1. 修复了上次更新后无法与聊天栏交互的bug
2. 添加汽车样式至五种，每个玩家都能拥有不同外形的汽车了
3. 增加了服务器人数上限检测（5人）

---

##### $Test\ V0.1.4\ 版本内容更新$
1. 添加了暂停面板，并实现了暂停与返回游戏及返回主菜单的逻辑，并设置了暂停快捷键ESC
2. 将服务器端的用户列表由ArrayList修改为了TreeMap，实现了用户账号与用户名一一对应的机制，并且玩家可以在掉线后通过同一用户名进行重连
3. 完善了部分胜利判断机制，取得胜利后将会弹出消息提示框并在聊天栏告知所有玩家
4. 书写了部分碰撞检测代码（待完善）
5. 将GamePanel改为了继承分层窗格JLayPane，以便更好地布局组件的图层

---

##### $Test\ V0.1.5\ 版本内容更新$
1. 修复了当服务器内有同名玩家时继续申请进入会导致服务器崩溃的bug
2. 增加了游戏开始前各个玩家的信息面板
3. 添加了准备状态，玩家可以通过P键来准备或取消准备状态

---

##### $Test\ V0.2.0\ 版本内容更新$
1. 完善了准备机制，当所有玩家进入准备状态时（超过1名玩家），游戏将会在三秒倒计时后开始，所有玩家会被传回初始位置
2. 准备面板会实时更新和显示所有玩家状态（离线、未准备、已准备）
3. 在属于自己的准备面板上有一个按钮，玩家可以通过此按钮来更新准备状态了
4. 完善了碰撞检测机制，玩家在碰撞其他物体时会立刻停下
5. 玩家可以通过U键来显示或隐藏物体的碰撞箱，不过碰撞箱显示会因为精度问题而不准确

---

##### $Test\ V0.2.1\ 版本内容更新$
1. 修复了碰撞箱显示与实际不符的bug

---

## 待更新或修复内容
1. 添加其他障碍物或道具
2. 游戏美术素材
3. 增加进度条
4. 继续完善胜利判断机制，并给玩家积分
