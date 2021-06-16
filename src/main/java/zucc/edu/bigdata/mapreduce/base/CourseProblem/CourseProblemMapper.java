package zucc.edu.bigdata.mapreduce.base.CourseProblem;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import zucc.edu.bigdata.bean.jsonobject.Course;

import java.io.IOException;

public class CourseProblemMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    // outK
    private Text course_id = new Text();

    // outV
    private IntWritable ifPro = new IntWritable(1);
    private IntWritable ifnotPro = new IntWritable(0);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        Course course = JSON.parseObject(value.toString(), Course.class);
        course_id.set(course.getCourse_id());
        // 对每个item 判定是V 还是 P
        for (String item : course.getItem()) {
            if (item.substring(0, 1).equals("P")) {
                context.write(course_id, ifPro);
            } else {
                context.write(course_id, ifnotPro);
            }
        }
    }
}
