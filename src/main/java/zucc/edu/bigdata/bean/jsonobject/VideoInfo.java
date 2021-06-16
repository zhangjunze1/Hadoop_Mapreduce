package zucc.edu.bigdata.bean.jsonobject;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
*       video_info.json
* */


public class VideoInfo {
    private String id;
    private String name;
    private List<Double> start = new ArrayList<Double>();
    private List<Double> end = new ArrayList<Double>();
    private List<String> text = new ArrayList<String>();

    public VideoInfo() {}

    public VideoInfo(String json) {
        VideoInfo video = JSON.parseObject(json, VideoInfo.class);
        this.id = video.id;
        this.name = video.name;
        this.start = video.start;
        this.end = video.end;
        this.text = video.text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Double> getStart() {
        return start;
    }

    public void setStart(List<Double> start) {
        this.start = start;
    }

    public List<Double> getEnd() {
        return end;
    }

    public void setEnd(List<Double> end) {
        this.end = end;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", text=" + text +
                '}';
    }
}
