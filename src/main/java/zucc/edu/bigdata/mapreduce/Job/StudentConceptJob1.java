package zucc.edu.bigdata.mapreduce.Job;

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
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import zucc.edu.bigdata.bean.jsonobject.ProblemActivity;

import java.io.IOException;

/**
 * 学生答对的题目的知识点 情况
 */
public class StudentConceptJob1 extends Configured implements Tool {


    static class StudentConceptMapper1 extends Mapper<LongWritable, Text, Text, Text>{

        // outK studentId
        private Text student_id = new Text();

        // outV concept + label  答题情况总次数
        private Text conceptAndLabel = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            ProblemActivity problemActivity = new ProblemActivity(value.toString());

            String sum = new String("");
            student_id.set(problemActivity.getStudent_id());

            sum = problemActivity.getConcept().substring(2,problemActivity.getConcept().length()-2) + " " + problemActivity.getLabel();

            conceptAndLabel.set(sum);
            context.write(student_id,conceptAndLabel);
        }
    }

    static class StudentConceptReducer1 extends TableReducer<Text, Text, NullWritable> {

        private byte[] family_info = Bytes.toBytes("info");

        private byte[] column_conceptRate = Bytes.toBytes("conceptRate");

        private Text VconceptAndLabel = new Text();

        private int label ;
        // 存储String 的concept + label
        String stringValue = "";
        // 最后一位 答题正确与否
        String stringlabel = "";
        // 总次数
        int allLabel = 0;
        int rightLabel = 0;

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for (Text value : values) {
                String[] words = value.toString().split(" ");
                stringValue = value.toString();
                stringlabel = stringValue.substring(stringValue.length()-1, stringValue.length());
                if (Integer.valueOf(stringlabel)==1){
                    allLabel++;
                    rightLabel++;
                }else {
                    allLabel++;
                }
                VconceptAndLabel.set(words[0]+" "+allLabel+" "+rightLabel);
            }
            Put put = new Put(Bytes.toBytes(String.valueOf(key)));
            put.addColumn(family_info,column_conceptRate,Bytes.toBytes(String.valueOf(VconceptAndLabel)));
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
        job.setJarByClass(StudentConceptJob1.class);

        FileInputFormat.setInputPaths(job, inputPath);

        job.setMapperClass(StudentConceptMapper1.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        // Hbase提供的工具 自动设置mapreduce提交到hbase里
        TableMapReduceUtil.initTableReducerJob(tableName, StudentConceptReducer1.class, job);
        job.setNumReduceTasks(1);

        return (job.waitForCompletion(true) ? 0 : 1);
    }

    public static void main(String[] args) throws Exception {
        //两个参数一个是Hadoop 中文件的路径    一个是要传入数据的表名
        String arg1 = "/dataset/problem_activity";
        String arg2 = "student";
        int status = ToolRunner.run(HBaseConfiguration.create(), new ProblemWrongActJob(), new String[]{arg1, arg2});
        System.exit(status);
    }
}
