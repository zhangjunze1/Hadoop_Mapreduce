package zucc.edu.bigdata.mapreduce.Job;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import zucc.edu.bigdata.bean.jsonobject.ProblemBehavior;

import java.io.IOException;

public class ProblemRightActJob extends Configured implements Tool {

    static class ProblemRightActMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

        // MapOutK
        private Text problem_id = new Text();

        // MapOutV
        private IntWritable labelright = new IntWritable(1);
        private IntWritable labelwrong = new IntWritable(0);

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            ProblemBehavior problemBehavior = JSON.parseObject(value.toString(), ProblemBehavior.class);
            problem_id.set(problemBehavior.getProblem_id());

            // 对每个题目 判定是对 还是 错误
            if (problemBehavior.getLabel() == 1) {
                context.write(problem_id, labelright);
            } else {
                context.write(problem_id, labelwrong);
            }
        }
    }

    static class ProblemRightActReducer extends TableReducer<Text, IntWritable, NullWritable> {
        private byte[] FAMILY = Bytes.toBytes("times"); // 列簇
        private byte[] TIMES_ALL = Bytes.toBytes("all"); // 列
        private byte[] TIMES_RIGHT = Bytes.toBytes("right"); // 列
        private byte[] TIMES_WRONG = Bytes.toBytes("wrong"); // 列

        // ReducerOutV
        private IntWritable outV = new IntWritable();

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

            int sum = 0;
            for (IntWritable value : values) {
                sum += value.get();
            }

            byte[] row = Bytes.toBytes(String.valueOf(key));

            Put put = new Put(row);
            put.addColumn(FAMILY, TIMES_RIGHT, Bytes.toBytes(sum));
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
        job.setJarByClass(ProblemRightActJob.class);

        FileInputFormat.setInputPaths(job, inputPath);

        job.setMapperClass(ProblemRightActMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // Hbase提供的工具 自动设置mapreduce提交到hbase里
        TableMapReduceUtil.initTableReducerJob(tableName, ProblemRightActReducer.class, job);
        job.setNumReduceTasks(1);

        return (job.waitForCompletion(true) ? 0 : 1);
    }

    public static void main(String[] args) throws Exception {
        //两个参数一个是Hadoop 中文件的路径    一个是要传入数据的表名
        String arg1 = "/dataset/problem_act"; //problem_act_train.json 和problem_act_train_2.json都放在这个problem_act文件夹下
        String arg2 = "problem";
        int status = ToolRunner.run(HBaseConfiguration.create(), new ProblemRightActJob(), new String[]{arg1, arg2});
        System.exit(status);
    }

}
