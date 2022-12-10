TEST
============================

## 环境依赖
开发语言：$Java$

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

###### $Test\ V0.0.1\ 版本内容更新$
1. 建立了软件初始架构
2. 搭建了初始界面

###### $Test\ V0.0.2\ 版本内容更新$
1. 优化了软件架构，增加了对LoginPanel和GamePanel的编写
2. 完成了基类GameObj的代码编写，实现了子类Car的部分方法
3. 实现了面板跳转
4. 实现了对键盘的监听，现在可以控制汽车上下方向移动