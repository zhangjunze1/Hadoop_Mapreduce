## MapReduce

+ CourseProblemDriver : 每个课程中的问题数量
+ CourseVideoDriver : 每个课程中的视频数量
+ AverageVideoDriver : 求视频平均长度  425s -- VideoInfoJob 
+ ProblemAllActTrainDriver : 求每道题对应的答题次数
+ ProblemWrongActTrainDriver : 求每道题对应的答题错误次数
+ ProblemRightActTrainDriver : 求每道题对应的答题正确次数

## Job
+ VideoInfoJob : 处理视频 Video_info.json 中的 各个视频长度 存入HBase 
    + 表名: "video"
    + 列簇: "info"
    + 列: "duration" (每个视频的时长)
+ CourseJob : 处理Course_info.json 中对应所有视频 个数总和 AND 时长统计总和 --
    + 表名: "course"
    + 列簇: "video"
    + 列: "videoCount","videoDuration" (视频数量,这个课程视频长度总和)
+ CourseJob2 : 处理Course_info.json 课程中的每个问题 问题的回答次数  --
    + 表名: "course"
    + 列簇: "problem"
    + 列: "count","allTimes" (课程中题目的数量,课程题目做题次数总和)
+ CourseJob3 : 处理Course_info.json 课程中的每个问题 问题的回答次数  --
    + 表名: "course"
    + 列簇: "problem"
    + 列: "rightTimes" (课程题目做题正确次数总和)
+ ProblemAllActJob : 处理problem_act_train.json 和 problem_act_train_2.json 中所有题目的被答题的总次数
    + 表名: "problem"
    + 列簇: "times"
    + 列: "all" (每个问题的答题被总次数)
+ ProblemRightActJob : 处理problem_act_train.json 和 problem_act_train_2.json 中所有题目的被答题的正确总次数
    + 表名: "problem"
    + 列簇: "times"
    + 列: "right" (每个问题的答题正确总次数)
+ ProblemWrongActJob : 处理problem_act_train.json 和 problem_act_train_2.json 中所有题目的被答题的错误总次数
    + 表名: "problem"
    + 列簇: "times"
    + 列: "wrong" (每个问题的答题错误总次数)
+ ProblemActivityJob : 处理 problem_activity.json 数据
    + 表名: "course"
    + 列簇: "problem"
    + 列: "content" "courseId"
    - And
    + 表名: "course"
    + 列簇: "problemActivity"
    + 列: "problemId" "studentId" "courseId" "time" "content" "label"
    
## 流程
-----------
### 1 - 2 顺序不可乱

1. VideoInfoJob 
2. CourseJob
### 345 - 67 顺序不能乱

3. ProblemAllActJob
4. ProblemRightActJob
5. ProblemWrongActJob
6. CourseJob2
7. CourseJob3

### 8
8. ProblemActivityJob
-------------

### HBase表

#### 1.Video
<table>
    <caption>video</caption>
    <tr>
        <td align="center">info</td>    
    </tr>
        <tr>
        <td>duration</td>
   </tr>
</table>


#### 2.Course
<table>
    <caption>course</caption>
    <tr>
        <td colspan="2" align="center">video</td>
        <td colspan="5" align="center">problem</td>
        <td colspan="6" align="center">problemActivity</td>
    </tr>
        <tr>
        <td>videoCount</td>
        <td>videoDuration</td>
        <td>count</td>
        <td>allTimes</td>
        <td>rightTimes</td>
        <td>content</td>
        <td>courseId</td>
        <td>problemId</td>
        <td>studentId</td>
        <td>courseId</td>
        <td>time</td>
        <td>content</td>
        <td>label</td>
   </tr>
</table>

#### 3.Problem
<table>
    <caption>problem</caption>
    <tr>
        <td colspan="3" align="center">times</td>
    </tr>
        <tr>
        <td>all</td>
        <td>right</td>
        <td>wrong</td>
   </tr>
</table>
