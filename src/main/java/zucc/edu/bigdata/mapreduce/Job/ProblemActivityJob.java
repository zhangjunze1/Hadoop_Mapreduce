package zucc.edu.bigdata.mapreduce.Job;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import zucc.edu.bigdata.bean.jsonobject.ProblemActivity;
import zucc.edu.bigdata.bean.jsonobject.ProblemBehavior;
import zucc.edu.bigdata.bean.jsonobject.VideoInfo;

import java.io.IOException;

/**
 * 处理 problem_activity.json 信息存储 存入HBase
 */
public class ProblemActivityJob extends Configured implements Tool {

    static class ProblemActivityMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {
        private byte[] activity_family = Bytes.toBytes("problemActivity"); // 列簇
        private byte[] problem_family = Bytes.toBytes("problem"); // 列簇

        private byte[] column_problemId = Bytes.toBytes("problemId"); // 列
        private byte[] column_studentId = Bytes.toBytes("studentId"); // 列
        private byte[] column_courseId = Bytes.toBytes("courseId"); // 列
        private byte[] column_time = Bytes.toBytes("time"); // 列
        private byte[] column_content = Bytes.toBytes("content"); // 列
        private byte[] column_label = Bytes.toBytes("label"); // 列

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            ProblemActivity problemActivity = new ProblemActivity(value.toString());
            byte[] row = Bytes.toBytes(problemActivity.getAct_id());

            Put put = new Put(row);

            // course--activity创建
            put.addColumn(activity_family, column_problemId, Bytes.toBytes(problemActivity.getProblem_id()));
            put.addColumn(activity_family, column_studentId, Bytes.toBytes(problemActivity.getStudent_id()));
            put.addColumn(activity_family, column_courseId, Bytes.toBytes(problemActivity.getCourse_id()));
            put.addColumn(activity_family, column_time, Bytes.toBytes(problemActivity.getTime()));
            put.addColumn(activity_family, column_content, Bytes.toBytes(problemActivity.getContent()));
            put.addColumn(activity_family, column_label, Bytes.toBytes(problemActivity.getLabel()));

            // course--problem添加
            put.addColumn(problem_family, column_courseId, Bytes.toBytes(problemActivity.getCourse_id()));
            put.addColumn(problem_family, column_content, Bytes.toBytes(problemActivity.getContent()));

            context.write(new ImmutableBytesWritable(row), put);
        }
    }

    public int run(String[] otherArgs) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://localhost:9000");
        conf.set("hbase.zookeeper.quorum", "localhost");  //hbase 服务地址
        conf.set("hbase.zookeeper.property.clientPort", "2181"); //端口号
        conf.set("hbase.master", "localhost:16000");
        System.setProperty("HADOOP_USER_NAME", "hadoop");

        Path inputPath = new Path(otherArgs[0]);
        String tableName = otherArgs[1];
        Job job = new Job(conf, tableName);

        job.setJarByClass(ProblemActivityJob.class);
        FileInputFormat.setInputPaths(job, inputPath);

        job.setMapperClass(ProblemActivityMapper.class);
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);
        job.setMapOutputValueClass(Put.class);
        TableMapReduceUtil.initTableReducerJob(tableName, null, job);
        job.setNumReduceTasks(0);

        return (job.waitForCompletion(true) ? 0 : 1);
    }

    public static void main(String[] args) throws Exception {
        //两个参数一个是Hadoop 中文件的路径    一个是要传入数据的表名
        String arg1 = "/dataset/problem_activity.json";
        String arg2 = "course";
        int status = ToolRunner.run(HBaseConfiguration.create(), new ProblemActivityJob(), new String[]{arg1,arg2});
        System.exit(status);
    }

}
