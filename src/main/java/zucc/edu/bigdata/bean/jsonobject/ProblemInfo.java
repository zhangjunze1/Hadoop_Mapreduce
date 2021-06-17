package zucc.edu.bigdata.bean.jsonobject;


import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * problem_info.json
 */
public class ProblemInfo {
    private String detail;               // 题干及其选项
    private String concept ;         // 题目考察知识点
    private String problem_id ;            // 题目Id

    public ProblemInfo() {}

    public ProblemInfo(String json) {
        ProblemInfo problemInfo = JSON.parseObject(json, ProblemInfo.class);
        this.detail = problemInfo.detail;
        this.concept = problemInfo.concept;
        this.problem_id = problemInfo.problem_id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getProblem_id() {
        return problem_id;
    }

    public void setProblem_id(String problem_id) {
        this.problem_id = problem_id;
    }

    @Override
    public String toString() {
        return "ProblemInfo{" +
                "detail='" + detail + '\'' +
                ", concept='" + concept + '\'' +
                ", problem_id=" + problem_id +
                '}';
    }

}
