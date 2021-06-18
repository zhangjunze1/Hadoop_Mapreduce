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
import java.util.HashMap;
import java.util.Map;

/**
 * 学生答对的题目的知识点 情况
 */
public class StudentConceptJob1 extends Configured implements Tool {


    static class StudentConceptMapper1 extends Mapper<LongWritable, Text, Text, Text> {

        // outK studentId
        private Text student_id = new Text();

        // outV concept + label  答题情况总次数
        private Text conceptAndLabel = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            ProblemActivity problemActivity = new ProblemActivity(value.toString());

            String sum = new String("");
            student_id.set(problemActivity.getStudent_id());

            sum = problemActivity.getConcept().substring(2, problemActivity.getConcept().length() - 2) + " " + problemActivity.getLabel();

            conceptAndLabel.set(sum);
            context.write(student_id, conceptAndLabel);
        }
    }

    static class StudentConceptReducer1 extends TableReducer<Text, Text, NullWritable> {

        private byte[] family_info = Bytes.toBytes("info");

        private byte[] column_conceptGrasp = Bytes.toBytes("conceptGrasp");
        private byte[] column_conceptAlmostGrasp = Bytes.toBytes("conceptAlmostGrasp");
        private byte[] column_conceptNoGrasp = Bytes.toBytes("conceptNoGrasp");

        private Text VconceptAndLabel = new Text();

        private int label;
        // 存储String 的concept + label
        private String stringValue = "";
        // concept
        private String concept= "";
        // 最后一位 答题正确与否
        private String stringlabel = "";
        // 总次数
        private int allLabel = 0;
        private int rightLabel = 0;

        private Map<String, String> map = new HashMap<String, String>();

        private String TotalGrasp = "";
        private String almostGrasp = "";
        private String noGrasp = "";
        private Text outV = new Text();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            label = 0;
            stringValue ="";
            stringlabel ="";
            concept="";
            allLabel=0;
            rightLabel=0;
            map.clear();
            TotalGrasp = "";
            almostGrasp ="";

            for (Text value : values) {
                String[] words = value.toString().split(" ");
                concept = words[0];
                stringValue = value.toString();
                stringlabel = words[1];

                if (Integer.valueOf(stringlabel) == 1) {
                    allLabel = 1;
                    rightLabel = 1;
                } else {
                    allLabel = 1;
                    rightLabel = 0;
                }
                // 没有 这个concept就存值为1
                if (!map.containsKey(key.toString()+ " "+ concept)) {
                    map.put(key.toString()+ " "+ concept, concept + " " + String.valueOf(allLabel) + " " + String.valueOf(rightLabel));
                } else {
                    String[] threeParts = map.get(key.toString()+ " "+ concept).split(" ");
                    allLabel = Integer.valueOf(threeParts[1]) + allLabel;
                    rightLabel = Integer.valueOf(threeParts[2]) + rightLabel;
                    map.put(key.toString()+ " "+ concept, concept + " " + String.valueOf(allLabel) + " " +  String.valueOf(rightLabel));
                }
            }

            for (String mapkey : map.keySet()) {
                String[] temp3 = map.get(mapkey).split(" ");

                if ((Integer.valueOf(temp3[2])*100) / Integer.valueOf(temp3[1]) >= 80) {
                    TotalGrasp = TotalGrasp + temp3[0] + " ";
                } else if ((Integer.valueOf(temp3[2])*100) / Integer.valueOf(temp3[1]) >= 50) {
                    almostGrasp = almostGrasp + temp3[0] + " ";
                }else {
                    noGrasp = noGrasp + temp3[0] + " ";
                }

            }
            if (!TotalGrasp.equals("")){
                TotalGrasp = TotalGrasp.substring(0, TotalGrasp.length() - 1);// 删除多余的空格
            }
            if (!almostGrasp.equals("")){
                almostGrasp = almostGrasp.substring(0, almostGrasp.length() - 1);// 删除多余的空格
            }
            if (!noGrasp.equals("")){
                noGrasp = noGrasp.substring(0, noGrasp.length() - 1);// 删除多余的空格
            }

            Put put = new Put(Bytes.toBytes(String.valueOf(key)));
            put.addColumn(family_info, column_conceptGrasp, Bytes.toBytes(TotalGrasp));
            put.addColumn(family_info, column_conceptAlmostGrasp, Bytes.toBytes(almostGrasp));
            put.addColumn(family_info, column_conceptNoGrasp, Bytes.toBytes(noGrasp));
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
        int status = ToolRunner.run(HBaseConfiguration.create(), new StudentConceptJob1(), new String[]{arg1, arg2});
        System.exit(status);
    }
}
