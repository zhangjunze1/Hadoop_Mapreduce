package zucc.edu.bigdata.bean.jsonobject;

import com.alibaba.fastjson.JSON;

/**
 * problem_act_train.json
 */
public class ProblemBehavior {
    private String problem_id; //问题的id
    private String student_id; //学生的id
    private int label;     //分别代表答题错误和正确

    public ProblemBehavior() {}

    public ProblemBehavior(String json) {
        ProblemBehavior problemBehavior = JSON.parseObject(json, ProblemBehavior.class);
        this.problem_id = problemBehavior.problem_id;
        this.student_id = problemBehavior.student_id;
        this.label = problemBehavior.label;
    }

    public String getProblem_id() {
        return problem_id;
    }

    public void setProblem_id(String problem_id) {
        this.problem_id = problem_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "ProblemBehavior{" +
                "problem_id='" + problem_id + '\'' +
                ", student_id='" + student_id + '\'' +
                '}';
    }
}
