package zucc.edu.bigdata.bean.feature;

public class StudentVideoFeature {

    private double rate;                // 用户每个视频的实际观看时长占比
    private double watching_count;          // 观看视频个数
    private double video_duration;          // 看过的视频的时长和（包括看了的部分和没看完的）
    private double local_watching_time;      // 看视频的现实时长和(停留在视频的现实时间)
    private double video_progress_time;      // 视频播放时长和（考虑倍速，即观看部分时长，10s的视频2倍速看了3s记6s）

    public StudentVideoFeature(double rate, double watching_count, double video_duration, double local_watching_time, double video_progress_time) {
        this.rate = rate;
        this.watching_count = watching_count;
        this.video_duration = video_duration;
        this.local_watching_time = local_watching_time;
        this.video_progress_time = video_progress_time;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
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

}
