package zucc.edu.bigdata.mapreduce.Job;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import zucc.edu.bigdata.bean.jsonobject.ProblemActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StudentRightRateJob  extends Configured implements Tool {

    static class StudentRightRateJobMapper extends Mapper<LongWritable, Text, Text, Text> {
        // outK studentId
        private Text student_id = new Text();

        private Text outV = new Text();
        // 总次数
        private int allLabel = 0;
        private int rightLabel = 0;
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            allLabel=0;
            rightLabel=0;

            ProblemActivity problemActivity = new ProblemActivity(value.toString());
            student_id.set(problemActivity.getStudent_id());

            String sum = new String("");
            int lable = problemActivity.getLabel();
            if (lable == 1) {
                allLabel = 1;
                rightLabel = 1;
            } else {
                allLabel = 1;
                rightLabel = 0;
            }
            sum = String.valueOf(allLabel)+" "+String.valueOf(rightLabel);
            outV.set(sum);

            context.write(student_id, outV);
        }
    }

    static class StudentRightRateJobReducer extends TableReducer<Text, Text, NullWritable> {
        // 学生答题记录总数 答题正确数量
        private byte[] family = Bytes.toBytes("info");

        private byte[] column_all = Bytes.toBytes("all");
        private byte[] column_right = Bytes.toBytes("right");

        private double allNumber;
        private double rightNumber;
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            allNumber=0;
            rightNumber=0;
            for (Text value : values) {
                String[] numbers = value.toString().split(" ");
                allNumber++;
                if (Integer.valueOf(numbers[1])==1){
                    rightNumber++;
                }
            }

            Put put = new Put(Bytes.toBytes(String.valueOf(key)));
            put.addColumn(family, column_all, Bytes.toBytes(allNumber));
            put.addColumn(family, column_right, Bytes.toBytes(rightNumber));
            context.write(NullWritable.get(), put);
        }
    }

    @Override
    public int run(String[] otherArgs) throws Exception {

        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://localhost:9000");
        conf.set("hbase.zookeeper.quorum", "localhost");  //hbase 服务地址
        conf.set("hbase.zookeeper.property.clientPort", "2181"); //端口号
        conf.set("hbase.master", "localhost:16000");
        System.setProperty("HADOOP_USER_NAME", "hadoop");

        Path inputPath = new Path(otherArgs[0]);
        String tableName = otherArgs[1];

        Job job = Job.getInstance(conf, tableName);
        job.setJarByClass(StudentRightRateJob.class);

        FileInputFormat.setInputPaths(job, inputPath);

        job.setMapperClass(StudentRightRateJobMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        // Hbase提供的工具 自动设置mapreduce提交到hbase里
        TableMapReduceUtil.initTableReducerJob(tableName, StudentRightRateJobReducer.class, job);
        job.setNumReduceTasks(1);

        return (job.waitForCompletion(true) ? 0 : 1);
    }

    public static void main(String[] args) throws Exception {
        //两个参数一个是Hadoop 中文件的路径    一个是要传入数据的表名
        String arg1 = "/dataset/problem_activity.json";
        String arg2 = "student";
        int status = ToolRunner.run(HBaseConfiguration.create(), new StudentRightRateJob(), new String[]{arg1,arg2});
        System.exit(status);
    }


}
