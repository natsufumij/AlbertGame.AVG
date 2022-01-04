# AlbertGame.AVG
A Love story Page.

# 怎样去运行
# How To Run
1. 安装好JDK 11并且配置好了环境变量。
2. 安装好Gradle 5.6到本地路径，并将其bin目录配置到环境变量里，
目前只支持JDK 11 与 Gradle 5.6协同工作，如何使更高版本的话可以工作但打包有问题。

3. 如果想运行游戏的主程序，进入项目Content所在的目录。用终端键入如下命令:
    ```
    gradle run
    ```
    如果想打包，输入如下命令:
    ```
    gradle jlink
    ```
    打完包之后，需要将资源文件即主目录下的Play文件夹，复制粘贴到Content项目的build/image的文件夹下，
    启动程序为build/image/bin/albertgame.avg.content.bat,点击此批处理文件既能运行成功。

4. 如果想运行游戏的编辑器来写剧本……目前正在实现中，还要等个把星期左右。
5. 欢迎fork学习及pr。