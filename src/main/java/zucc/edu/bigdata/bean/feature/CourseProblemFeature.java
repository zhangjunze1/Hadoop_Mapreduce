package zucc.edu.bigdata.bean.feature;

public class CourseProblemFeature {

    private int problemCnt = 0;           // 问题个数
    private int ProblemAllTimes = 0;      // 问题被回答次数
    private int ProblemRightTimes = 0;      // 问题被答对次数
    private String ProblemContent = null;      // 问题的内容

    public int getProblemCnt() {
        return problemCnt;
    }

    public void setProblemCnt(int problemCnt) {
        this.problemCnt = problemCnt;
    }

    public int getProblemAllTimes() {
        return ProblemAllTimes;
    }

    public void setProblemAllTimes(int problemAllTimes) {
        ProblemAllTimes = problemAllTimes;
    }

    public int getProblemRightTimes() {
        return ProblemRightTimes;
    }

    public void setProblemRightTimes(int problemRightTimes) {
        ProblemRightTimes = problemRightTimes;
    }

    public String getProblemContent() {
        return ProblemContent;
    }

    public void setProblemContent(String problemContent) {
        ProblemContent = problemContent;
    }
}
