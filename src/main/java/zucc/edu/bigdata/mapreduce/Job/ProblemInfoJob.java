package zucc.edu.bigdata.mapreduce.Job;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import zucc.edu.bigdata.bean.jsonobject.ProblemInfo;


import java.io.IOException;

public class ProblemInfoJob extends Configured implements Tool {

    static class ProblemInfoMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {

        private byte[] problem_family = Bytes.toBytes("info"); // 列簇

        private byte[] column_content = Bytes.toBytes("concept"); // 列

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            ProblemInfo problemInfo = new ProblemInfo(value.toString());
            byte[] row = Bytes.toBytes(problemInfo.getProblem_id());

            Put put = new Put(row);

            // problem -- info --concept添加
            put.addColumn(problem_family, column_content, Bytes.toBytes(problemInfo.getConcept()));

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

        job.setJarByClass(ProblemInfoJob.class);
        FileInputFormat.setInputPaths(job, inputPath);

        job.setMapperClass(ProblemInfoMapper.class);
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);
        job.setMapOutputValueClass(Put.class);
        TableMapReduceUtil.initTableReducerJob(tableName, null, job);
        job.setNumReduceTasks(0);

        return (job.waitForCompletion(true) ? 0 : 1);
    }

    public static void main(String[] args) throws Exception {
        //两个参数一个是Hadoop 中文件的路径    一个是要传入数据的表名
        String arg1 = "/dataset/problem_info.json";
        String arg2 = "problem";
        int status = ToolRunner.run(HBaseConfiguration.create(), new ProblemInfoJob(), new String[]{arg1,arg2});
        System.exit(status);
    }
}
