# 恋爱灯笼 / AlbertGame.AVG

@Author - 夏文纯一 Albert Flex  
@Albert Game Plans.  
@Project -  A bunch of loVe story paGes.  

## 怎样运行 / How To Run

1. 安装好JDK 17并且配置好了环境变量.  
   Install JDK17 and Set PATH
2. 安装好Gradle 7.2到本地路径，并将其bin目录配置到环境变量里,目前只支持JDK17/Gradle7.2协同工作。  
   Install Gradle7.2 and Set Path.

3. 如果想运行游戏的主程序，进入项目Content所在的目录。用终端键入如下命令:  
   If you want to run the game,then cd the path of content, and simply use the gradle command. Such As
    ```
    > cd content
    > gradle run
    ```

    如果想打包，输入如下命令:  
    If you want to package the game, input the command as follow,
    ```
    > gradle jlinkZip
    ```
    将压缩文件放到任何路径，解压打开，点击bin/avg.content.bat 就可以运行。  
    You will found a zip file in the build folder,unzip it, cd the bin path, and click the avg.content.bat, it works.

4. 如果想运行游戏的编辑器来写剧本……目前正在实现中，还要等个把星期左右。  
   If you want something like a editor to write the story,emmm ... I'm coding for it now,but ... still need a couple of weeks.
5. 欢迎fork学习及pr。  
   welcome fork, study the code, and pr. 