# Editor项目保存格式

## 项目文件 在Assets根目录下
### 项目目录格式示例
- /
    - /audio
        - au1.wav
        - au2.wav
    - /bgm
        - bgm1.mp3
        - bgm2.mp3
    - /scene
        - scene1.jpg
        - scene2.jpg
    - /person
        - person1_state1.png
        - person1_state2.png
    - /story
        - /chapter0
            - play1.avg
            - play2.avg
        - /chapter1
            - play3.avg
            - play4.avg
        - /chapter2
            - end.avg
        - chapter0.avg
        - chapter1.avg
        - chapter2.avg
        - global.avg

### 缓存的格式
1. avg.editor.properties
```
ProjectIds=xxxa;xxxb;xxxc;xxxd
Last.ProjectId=xxxa
```

2. _idxx.properties
```
RandomId=aaaa0000
NowPath=xxxxxxx

Global.StartChapter=XXX
Global.PersonDataId=XXX;XXX;XXX;XXX
Global.PersonDataName=XXX;XXX;XXX;XXX
Global.PersonId.States=XXX,XXX,XXX

Story.AudioId=XXX;XXX;XXX;XXX
Story.AudioName=XXX;XXX;XXX;XXX
Story.BgmId=XXX;XXX;XXX;XXX
Story.BgmName=XXX;XXX;XXX;XXX
Story.SceneId=XXX;XXX;XXX;XXX
Story.SceneName=XXX;XXX;XXX;XXX

Story.ChapterId=XXX;XXX;XXX
Story.ChapterName=XXX;XXX;XXX
Story.ChapterPos=x,y;x,y;x,y;x,y
Story.ChapterTo=a,b,c;a,b;a,b,c;a,b

Story.ChapterId.PlayId=XXX;XXX;XXX
Story.ChapterId.PlayName=XXX;XXX;XXX
Story.ChapterId.PlayPos=x,y;x,y;x,y
Story.ChapterId.PlayTo=x,y;x,y;x,y

Story.ChapterId.PlayId.StruckId=XX;XX;XX
Story.ChapterId.PlayId.StruckName=XX;XX;XX
Story.ChapterId.PlayId.StruckPos=x,y;x,y;x,y
Story.ChapterId.PlayId.StruckTo=x,y;a,x,q;a,s

```