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
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import zucc.edu.bigdata.bean.jsonobject.PreRequisite;


import java.io.IOException;

/**
 * 存储该问题的
 */

public class ProblemRequistiteJob  extends Configured implements Tool {

    static class ProblemRequistiteMapper extends Mapper<LongWritable, Text, Text, Text> {

        // outK 后修
        private Text concept_B = new Text();

        // outV
        private Text concept_A = new Text();
        
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            PreRequisite preRequisite = JSON.parseObject(value.toString(), PreRequisite.class);
            
            concept_B.set(preRequisite.getConcept_B());
            concept_A.set(preRequisite.getConcept_A());

            context.write(concept_B,concept_A);
        }
    }

    static class ProblemRequistiteReducer extends TableReducer<Text, Text, NullWritable> {

        private byte[] problem_family = Bytes.toBytes("info"); // 列簇

        private byte[] column_preContent = Bytes.toBytes("preConcept"); // 列

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String sum = new String("");

            for (Text value : values) {
                sum += value;
                sum += " ";
            }
            sum = sum.substring(0, sum.length() - 1);// 删除多余的空格
            Put put = new Put(Bytes.toBytes(key.toString()));
            put.addColumn(problem_family,column_preContent,Bytes.toBytes(sum));
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
        job.setJarByClass(ProblemRequistiteJob.class);

        FileInputFormat.setInputPaths(job, inputPath);

        job.setMapperClass(ProblemRequistiteMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        TableMapReduceUtil.initTableReducerJob(tableName, ProblemRequistiteReducer.class,job);
        job.setNumReduceTasks(1);

        return (job.waitForCompletion(true) ? 0 : 1);
    }

    public static void main(String[] args) throws Exception {
        //两个参数一个是Hadoop 中文件的路径    一个是要传入数据的表名
        String arg1 = "/dataset/prerequisite.json";
        String arg2 = "problem";
        int status = ToolRunner.run(HBaseConfiguration.create(), new ProblemRequistiteJob(), new String[]{arg1,arg2});
        System.exit(status);
    }

}
