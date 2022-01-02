# AlbertGame.AVG
恋爱灯笼  

## 游戏主体 Content
1.  界面
    - 开始界面
    - 游戏界面
    - 存档界面
2.  存档数据
3.  主体功能接口
4.  剧本解析器

## 剧本编辑器 Editor
1.  界面
    - 菜单栏
    - 全局剧本设计视图
    - 剧本脚本设计视图
2.  剧本数据分析器
3.  存档数据
4.  剧本编辑器功能接口

## 剧本格式
剧本命令 两个空格

剧本命令格式  

- 对话
  - Dialog  Word  #Text  旁白文字
  - Dialog  Word  @S  #Text  对话文字，使用名称？？？，以后都如此
  - Dialog  Word  @M  #Text  对话文字，以‘我’为名称，以后都如此
  - Dialog  Word  @DataId  #Text  对话文字,以id的人物名称，以后都如此
  - Dialog  Open  打开对话框
  - Dialog  Clear  清空对话框及名称
  - Dialog  Close  关闭对话框
  
- 人物
  - Person  In  #DataId 人物进入场景缓存
  - Person  Out  #DataId 人物换出场景缓存
  - Person  Show  #Position(L/C/R)  #DataId 在左边、右边显示人物
  - Person  No.Show  #Position(L/C/R)  去掉显示的人物位置
  - Person  Hide  #Position(L/C/R) 隐藏左边、右边
  - Person  Change.State  #Position(L/C/R)  #newState 改变左边、右边人物的状态
  
- 属性存储
  - Storage  Save#Name  #Value 保存为某个值
  - Storage  Plus#Name  #Value 增加一个值
  - Storage  Minus#Name  #Value 减少一个值

- 音乐
  - Audio  Bgm.Play  #BgmName
  - Audio  Bgm.Pause
  - Audio  Bgm.Resume
  - Audio  Bgm.Stop

  - Audio  Sound.Play #SoundName

- 选项
    - Select  Go  #SelectId  #[Option1,Option2,Option3]  选择问题保存Id
  `
  Select Go  Question.ID.A  我不选择,你选择吧,选你个头!  则保存在选择记录里,ID.A为0-2，并且在属存储里有个同名的属性值
  `

- 界面
  - View  Scene  #Name 更换场景图片
  - View  Shake  #L/C/R 抖动人物
  - View  Darking  #3000(ms)  渐黑
  - View  Lighting  #3000(ms)  渐亮

剧本剧情切换模式：剧本间切换、剧本内故事切换  
剧本结构例  
```
#Info
  id
  name
  Play
#Body
  >>Begin  
    #View  Scene  Name1
    #Audio  Bgm.Play  bgm1
    #View  Lighting  3000
  >>End  
    #Audio  Bgm.Stop
    #View  Darking  3000
  >>Start0  
  	...  
  >>Start1  
  	@M  这是哪里？  
  	@M  好黑，好黑……  
  	@M  我是谁？  
  	@M  我在干什么？  
    @S  这是你的末日
    @M  我的末日?
    #Audio  Sound.Play  sound1
    @M  啊，什么声音？
  >>Start2  
  	...  
  >>Start10  
  	...  
  	...  
#Progress  
  Start1->OptionSelect1->Start2,Start3
  Start3->OptionSelect2->Start6,Start7,Start8
  ...
#Options
  >>OptionSelect2
    A=2 | D=2 | S=3
    A>2 & D<2>
    Else
  ...   

章节剧本配置
```
#Info
  id
  name
  StartPlayName
  Chapter
#Progress
  play1->NextOption1->play2,play3,play4
  play1->NextOption2->play2,play3,play4
#Options
  >>NextOption1
    A=3 | D=2 | S=8
    S=1 & D>3 a=3
    Else
  >>NextOption2
    A=3 | D=2 | S=8
    S=1 & D>3 a=3
    Else
```


总局配置  
#Info
  StartName
  Global
#Progress
  Chapter0->NextOption1->Chapter1,Chapter2
  Chapter1->NextOption2->Chapter3,Chapter4
  Chapter2->NextOption3->Chapter3,Chapter4
  Chapter3->NextOption4->Chapter4,Chapter5
#Options
  >>NextOption1
    A=3 | D=2 | S=8
    S=1 & D>3 a=3
    Else
  >>NextOption2
    A=3 | D=2 | S=8
    S=1 & D>3 a=3
    Else
#PersonData
  >>PersonId
    name
    1,2,3,4,5,6
  >>PersonId
    name
    1,2,3,4,6,7

缓存
year,month,day,hour,minute,second
chapter,play,struck,index,chaptername,playname
scene,bgm,name,word,wordtype[M,S,P,@DataId]
leftp,centerp,rightp,personin,
wordpanelshow,maskshow