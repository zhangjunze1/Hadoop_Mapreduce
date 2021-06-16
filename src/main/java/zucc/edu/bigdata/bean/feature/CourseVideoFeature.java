package zucc.edu.bigdata.bean.feature;

public class CourseVideoFeature {

    private int videoCnt = 0;           // 视频个数
    private double videoDuration = 0;      // 视频内容长度


    public CourseVideoFeature() {

    }
    public CourseVideoFeature(int videoCnt, double videoDuration) {
        this.videoCnt = videoCnt;
        this.videoDuration = videoDuration;
    }



    public double getVideoCnt() {
        return videoCnt;
    }

    public void setVideoCnt(int videoCnt) {
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
                '}';
    }
}
