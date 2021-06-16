package zucc.edu.bigdata.bean.jsonobject;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;


/**
*       course_info.json
* */


public class Course {
    private List<String> item  = new ArrayList<String>();
    private String course_id;

    public Course() {
    }

    public Course(String json) {
        Course course = JSON.parseObject(json, Course.class);
        this.item = course.item;
        this.course_id = course.course_id;
    }

    public List<String> getItem() {
        return item;
    }

    public void setItem(List<String> item) {
        this.item = item;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    @Override
    public String toString() {
        return "Course{" +
                "item=" + item +
                ", course_id='" + course_id + '\'' +
                '}';
    }
}
