package zucc.edu.bigdata.bean.jsonobject;

/**
 *          video_activity.json
 * */


public class Activity {
    private String course_id;               // 课程id
    private String video_id;                // 视频id
    private double watching_count;            // 观看次数
    private double video_duration;          // 本视频的总长度
    private double local_watching_time;     // 实际观看时长(停留在本视频的时间)
    private double video_progress_time;     // 考虑了倍速的播放时长
    private double video_start_time;        // 对于本视频，该用户看的最早的时间点
    private double video_end_time;          // 对于本视频，该用户看的最晚的时间点
    private String local_start_time;        // 实际观看开始的现实时间
    private String local_end_time;          // 实际观看结束的现实时间

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public double getWatching_count() {
        return watching_count;
    }

    public void setWatching_count(double watching_count) {
        this.watching_count = watching_count;
    }

    public double getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(double video_duration) {
        this.video_duration = video_duration;
    }

    public double getLocal_watching_time() {
        return local_watching_time;
    }

    public void setLocal_watching_time(double local_watching_time) {
        this.local_watching_time = local_watching_time;
    }

    public double getVideo_progress_time() {
        return video_progress_time;
    }

    public void setVideo_progress_time(double video_progress_time) {
        this.video_progress_time = video_progress_time;
    }

    public double getVideo_start_time() {
        return video_start_time;
    }

    public void setVideo_start_time(double video_start_time) {
        this.video_start_time = video_start_time;
    }

    public double getVideo_end_time() {
        return video_end_time;
    }

    public void setVideo_end_time(double video_end_time) {
        this.video_end_time = video_end_time;
    }

    public String getLocal_start_time() {
        return local_start_time;
    }

    public void setLocal_start_time(String local_start_time) {
        this.local_start_time = local_start_time;
    }

    public String getLocal_end_time() {
        return local_end_time;
    }

    public void setLocal_end_time(String local_end_time) {
        this.local_end_time = local_end_time;
    }

    @Override
    public String toString() {
        return "TrainActivity{" +
                "course_id='" + course_id + '\'' +
                ", video_id='" + video_id + '\'' +
                ", watch_count=" + watching_count +
                ", video_duration=" + video_duration +
                ", local_watching_time=" + local_watching_time +
                ", video_progress_time=" + video_progress_time +
                ", video_start_time=" + video_start_time +
                ", video_end_time=" + video_end_time +
                ", local_start_time='" + local_start_time + '\'' +
                ", local_end_time='" + local_end_time + '\'' +
                '}';
    }
}
