package zucc.edu.bigdata.mapreduce.Job;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import zucc.edu.bigdata.bean.jsonobject.Course;
import zucc.edu.bigdata.tools.HbaseClient;

import java.io.IOException;

/**
 * 处理 课程中的每个问题 题目的个数 题目总回答正确的次数  存入HBase
 * 获取的是 course_info.json的数据
 */
public class CourseJob3 extends Configured implements Tool {

    static class CourseMapper3 extends Mapper<LongWritable, Text, Text, DoubleWritable> {
        /** outK --> course_id **/
        /**
         * outV --> 从hbase中取出来的 problem的答题正确次数 二进制数据
         **/
        private Text outK = new Text();
        private DoubleWritable outV = new DoubleWritable();
        private HbaseClient hbase = new HbaseClient();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            Course course = JSON.parseObject(value.toString(), Course.class);
            outK.set(course.getCourse_id());

            for (String item : course.getItem()) {
                if (item.substring(0, 1).equals("P")) {
                    byte[] res = hbase.get(TableName.valueOf("problem"), Bytes.toBytes(item), Bytes.toBytes("times"), Bytes.toBytes("right"));
                    if (res!=null){
                        outV.set(Bytes.toDouble(res));
                        context.write(outK, outV);
                    }
                }
            }
        }
    }

    static class CourseReducer3 extends TableReducer<Text, DoubleWritable, NullWritable> {

        private byte[] family = Bytes.toBytes("info");

        private byte[] column_problemRightTimes = Bytes.toBytes("rightTimes");

        @Override
        protected void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
            double rightTimes = 0;
            String rightTimes1 ="";
            for (DoubleWritable value : values) {
                rightTimes += value.get();
            }
            rightTimes1 = Double.toString(rightTimes);
            Put put = new Put(Bytes.toBytes(String.valueOf(key)));
            put.addColumn(family, column_problemRightTimes, Bytes.toBytes(rightTimes1));
            context.write(NullWritable.get(), put);
        }
    }

    public int run(String[] otherArgs) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://localhost:9000");
        conf.set("hbase.zookeeper.quorum", "localhost");  //hbase 服务地址
        conf.set("hbase.zookeeper.property.clientPort", "2181"); //端口号
        conf.set("hbase.master", "localhost:16000");
        System.setProperty("HADOOP_USER_NAME", "hadoop");

        Path inputPath = new Path(otherArgs[0]);
        String tableName = otherArgs[1];

        Job job = new Job(conf, tableName);
        job.setJarByClass(CourseJob3.class);

        FileInputFormat.setInputPaths(job, inputPath);

        job.setMapperClass(CourseMapper3.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(DoubleWritable.class);

        TableMapReduceUtil.initTableReducerJob(tableName, CourseReducer3.class, job);
        job.setNumReduceTasks(1);

        return (job.waitForCompletion(true) ? 0 : 1);
    }

    public static void main(String[] args) throws Exception {
        //两个参数一个是Hadoop 中文件的路径    一个是要传入数据的表名
        String arg1 = "/dataset/course_info.json";
        String arg2 = "course";
        int status = ToolRunner.run(HBaseConfiguration.create(), new CourseJob3(), new String[]{arg1, arg2});
        System.exit(status);
    }

}
