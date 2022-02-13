# 恋爱灯笼 / AlbertGame.AVG

@Author - 夏文纯一 Albert Flex  
@Albert Game Plans.  
@Project -  A bunch of loVe story paGes.  

## 怎样运行 / How To Run

1. 安装好JDK 17并且配置好了环境变量.  
   Install JDK17 and Set PATH

2. 如果想运行游戏的主程序，进入项目Content所在的目录。用终端键入如下命令:  
    ```
    > cd Content
    > gradlew run 
   ```
    如果想打包，输入如下命令:  
    If you want to package the game, input the command,
    ```
    > gradlew jpackageImage
    ```
    你会看到build/jpackage里有一个 avg.content 文件夹，此为绿色运行包，点击avg.content.exe 运行(Windows 为例)，将文件夹压缩，可分发给指定平台的用户。
    You will found a folder called avg.content under build/jpackage, Simple Click the avg.content.exe(Windows For example) executable file If your Want,   
    You can compress this folder, and distribute to other users.

3. 如果想运行游戏的编辑器来写剧本……以后有时间回头写写看
   If you want something like a editor to write the story,emmm ... some days in the future.
4. 欢迎fork学习及pr。  
   welcome fork, study the code, and pr. 