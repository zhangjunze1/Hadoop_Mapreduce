package zucc.edu.bigdata.mapreduce.Job;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.Tool;
import zucc.edu.bigdata.bean.jsonobject.ProblemActivity;
import zucc.edu.bigdata.bean.jsonobject.ProblemBehavior;
import zucc.edu.bigdata.bean.jsonobject.VideoInfo;

import java.io.IOException;

/**
 * 处理 problem_activity.json 信息存储 存入HBase
 */
public class ProblemActivityJob extends Configured implements Tool {

    static class ProblemActivityMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {

        private byte[] FAMILY = Bytes.toBytes("activity"); // 列簇
        private byte[] column_problemId = Bytes.toBytes("problemId"); // 列
        private byte[] column_studentId = Bytes.toBytes("studentId"); // 列
        private byte[] column_courseId = Bytes.toBytes("courseId"); // 列
        private byte[] column_time = Bytes.toBytes("time"); // 列
        private byte[] column_content = Bytes.toBytes("content"); // 列
        private byte[] column_label = Bytes.toBytes("label"); // 列

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            ProblemActivity video = new ProblemActivity(value.toString());
        }
    }

    @Override
    public int run(String[] strings) throws Exception {
        return 0;
    }
}
