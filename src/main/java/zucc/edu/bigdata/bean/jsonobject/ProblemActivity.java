package zucc.edu.bigdata.bean.jsonobject;


import com.alibaba.fastjson.JSON;

/**
 * problem_activity.json
 */
public class ProblemActivity {

    private String student_id;      // 学生id
    private String act_id;          // 做题行为id
    private String problem_id;      // 题目id
    private String course_id;       // 每道题所属课程id
    private String time;            // 学生的本次行为的时间
    private String content;         // 题目的内容
    private String concept;         // 题目的知识点
    private int label;           // 学生每道题的正确与否

    public ProblemActivity() {
    }

    public ProblemActivity(String json) {
        ProblemActivity problemActivity = JSON.parseObject(json, ProblemActivity.class);
        this.student_id = problemActivity.student_id;
        this.act_id = problemActivity.act_id;
        this.problem_id = problemActivity.problem_id;
        this.course_id = problemActivity.course_id;
        this.time = problemActivity.time;
        this.content = problemActivity.content;
        this.concept = problemActivity.concept;
        this.label = problemActivity.label;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getAct_id() {
        return act_id;
    }

    public void setAct_id(String act_id) {
        this.act_id = act_id;
    }

    public String getProblem_id() {
        return problem_id;
    }

    public void setProblem_id(String problem_id) {
        this.problem_id = problem_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "ProblemActivity{" +
                "student_id='" + student_id + '\'' +
                ", act_id='" + act_id + '\'' +
                ", problem_id=" + problem_id +
                ", course_id=" + course_id +
                ", time=" + time +
                ", content=" + content +
                ", concept=" + concept +
                ", label=" + label +
                '}';
    }

}
