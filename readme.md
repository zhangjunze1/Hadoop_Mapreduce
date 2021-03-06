# 大数据计算与技术
+ 框架: Hdfs + Mapreduce + Hbase + Zookeeper + Yarn 
+ 后端框架 : SpringBoot + Mybatis-plus + Swagger2-UI + Knife4j
+ 前端框架 : Vue + elementUI + Echarts

+ 忠告 PS:非常重要  如需处理的数据量小 请使用Spark框架 以及 Hive或者MongoDB数据库

## MapReduce

+ CourseProblemDriver : 每个课程中的问题数量
+ CourseVideoDriver : 每个课程中的视频数量
+ AverageVideoDriver : 求视频平均长度  425s -- VideoInfoJob 
+ ProblemAllActTrainDriver : 求每道题对应的答题次数
+ ProblemWrongActTrainDriver : 求每道题对应的答题错误次数
+ ProblemRightActTrainDriver : 求每道题对应的答题正确次数
+ PreRequistiteDriver : 每个概念的先修概念统计
+ StudentConceptAllDriver : 学生的某个概念的总答题数目
+ StudentConceptDriver : 学生的某个概念的总答题数目的正确个数

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
    + 列: "problemId" "studentId" "courseId" "time" "content" "label" "concept"
+ ProblemInfoJob : 处理problem_info.json 数据 对应的题目概念
    + 表名: "problem"
    + 列簇: "info"
    + 列: "concept"      
+ ProblemRequistiteJob : 处理prerequisite.json 的数据获得 先修先修概念
    + 表名: "problem"
    + 列簇: "info"
    + 列: "preConcept" : 内部格式为：例如"数据结构 树 B树 搜索 二叉树 红黑树 "(以空格分割)
+ StudentConceptJob1: problem_activity.json的数据
    + 表名: "student"
    + 列簇: "info"
    + 列 : "conceptGrasp" "conceptAlmostGrasp" "conceptNoGrasp" 
    (掌握的：格式"U_10429140	堆 复杂度 数据结构 树 二叉树 哈希 排序 B树 向量")
    (会一点的：格式"U_1043017	序列 二叉树 复杂度 树 排序 栈")
    (未掌握的：格式"U_10438365	树 排序 伸展树 二叉树 数据结构 栈 复杂度")
+ StudentRightRateJob： problem_activity.json的数据
    + 表名: "student"
    + 列簇: "info"
    + 列 : "all" "right" 学生答题的次数和正确次数
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
9. ProblemInfoJob
10. ProblemRequistiteJob
11. StudentConceptJob1
12. StudentRightRateJob
-------------

### HBase表

#### 1.Video
<table>
    <caption>video</caption>
    <tr>
        <td align="center">info</td>    
    </tr>
        <tr>
        <td align="center">duration</td>
   </tr>
</table>


#### 2.Course
<table>
    <caption>course</caption>
    <tr>
        <td colspan="2" align="center">video</td>
        <td colspan="5" align="center">problem</td>
        <td colspan="7" align="center">problemActivity</td>
    </tr>
        <tr>
        <td align="center">videoCount</td>
        <td align="center">videoDuration</td>
        <td align="center">count</td>
        <td align="center">allTimes</td>
        <td align="center">rightTimes</td>
        <td align="center">content</td>
        <td align="center">courseId</td>
        <td align="center">problemId</td>
        <td align="center">studentId</td>
        <td align="center">courseId</td>
        <td align="center">time</td>
        <td align="center">content</td>
        <td align="center">concept</td>
        <td align="center">label</td>
   </tr>
</table>
 
#### 3.Problem
<table>
    <caption>problem</caption>
    <tr>
        <td colspan="3" align="center">times</td>
        <td colspan="2" align="center">info</td>
    </tr>
        <tr>
        <td align="center">all</td>
        <td align="center">right</td>
        <td align="center">wrong</td>
        <td align="center">concept</td>
        <td align="center">preConcept</td>
   </tr>
</table>

#### 4.Student
<table>
    <caption>student</caption>
    <tr>
        <td colspan="5" align="center">info</td>    
    </tr>
        <tr>
        <td align="center">conceptGrasp</td>
        <td align="center">conceptAlmostGrasp</td>
        <td align="center">conceptNoGrasp</td>
        <td align="center">all</td>
        <td align="center">right</td>
   </tr>
</table>