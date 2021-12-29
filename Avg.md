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
  - Dialog  Word  #Text  默认文字,延续上一次对话的名称
  - Dialog  Word  [S]  #Text  对话文字，使用名称？？？，以后都如此
  - Dialog  Word  [M]  #Text  对话文字，以‘我’为名称，以后都如此
  - Dialog  Word  [DataId]  #Text  对话文字,以id的人物名称，以后都如此
  - Dialog  Open  打开对话框
  - Dialog  Clear  清空对话框及名称
  - Dialog  Close  关闭对话框
  
- 人物
  - Person  In  #DataId 人物进入场景缓存
  - Person  Out  #DataId 人物换出场景缓存
  - Person  Show  #Position(L/R)  #DataId 在左边、右边显示人物
  - Person  Hide  #Position(L/R) 隐藏左边、右边
  - Person  Change.State  #Position(L/R)  #newState 改变左边、右边人物的状态
  
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
  - Select  Go#QuestionId  #Question  #[Option1,Option2,Option3]  选择问题保存Id
  `
  Select Go  Question.ID.A  你要选择谁? [我不选择,你选择吧,选你个头!]  则保存在选择记录里,ID.A为0-2，并且在属存储里有个同名的属性值
  `

- 界面
  - View  Background  #Name
  - View  Title  #newText
  - View  Title.Show
  - View  Title.Hide
  - View  Time  #newText
  - View  Time.Show
  - View  Time.Hide

剧本剧情切换模式：剧本间切换、剧本内故事切换  

剧本结构例:  
```
#Begin:  
  View  Background  Back1  
  View  Title  Title1  
  View  Time  Time1  
  View  Title.Show  
  View  Time.Show  
#End:  
  View  Title.Hide  
  View  Time.Hide  
  Dialog  End  
  Dialog  Close  
#Body  
  Start0:  
  	...  
  Start1:  
  	[M]这是哪里？  
  	[M]好黑，好黑……  
  	[M]我是谁？  
  	[M]我在干什么？  
  Start2:  
  	...  
  Start10:  
  	...  
  	...  
#Progress:  
  Start0->Start1
  Start1->[OptionSelect1]->[Start2,Start3] 
  Start2->Start4
  Start3->[OptionSelect2]->[Start6,Start7,Start8]
  Start4->Start5  
  ...  
  Start9->Start10 
#Options:
  OptionSelect1:
    A=B+C, D=B*C+2, S=F/2-3, K=9%2
    [0]={A=2 | D=2 | S=3}
    [1]={A>2 & D<2>}
    [2]={S>3 & K=1}
    [3]={else}
  ...   

```

系统剧本配置:
```
#Begin
  Head
#End
  Tail
#Progress:  
  [Fade] Start0->Start1
  [Block] Start1->[OptionSelect1]->[Start2,Start3] 
  [Fade] Start2->Start4
  [Fade] Start3->[OptionSelect2]->[Start6,Start7,Start8]
  [Block] Start4->Start5  
  ...  
  Start9->Start10 
#Options:
  OptionSelect1:
    A=B+C, D=B*C+2, S=F/2-3, K=9%2
    [0]={A=2 | D=2 | S=3}
    [1]={A>2 & D<2>}
    [2]={S>3 & K=1}
    [3]={else}
  OptionSelect2:
    ...  
  ...   

```
