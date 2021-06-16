package zucc.edu.bigdata.bean.feature;

public class CourseFeature {

    private double videoCnt = 0;           // 视频个数
    private double videoDuration = 0;      // 视频内容长度
    private double problemCnt = 0;           // 题目个数


    public CourseFeature() {

    }
    public CourseFeature( double videoCnt, double videoDuration) {
        this.videoCnt = videoCnt;
        this.videoDuration = videoDuration;
        this.problemCnt = problemCnt;
    }

    public double getProblemCnt() {
        return problemCnt;
    }

    public void setProblemCnt(double problemCnt) {
        this.problemCnt = problemCnt;
    }

    public double getVideoCnt() {
        return videoCnt;
    }

    public void setVideoCnt(double videoCnt) {
        this.videoCnt = videoCnt;
    }

    public double getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(double videoDuration) {
        this.videoDuration = videoDuration;
    }

    @Override
    public String toString() {
        return "CourseFeature{" +
                ", videoCnt=" + videoCnt +
                ", videoDuration=" + videoDuration +
                ", problemCnt=" + problemCnt +
                '}';
    }
}
