package zucc.edu.bigdata.mapreduce.base.StudentConcept;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import zucc.edu.bigdata.mapreduce.base.ProblemActTrain.right.ProblemRightActTrainDriver;
import zucc.edu.bigdata.mapreduce.base.ProblemActTrain.right.ProblemRightActTrainMapper;
import zucc.edu.bigdata.mapreduce.base.ProblemActTrain.right.ProblemRightActTrainReducer;

import java.io.IOException;

public class StudentConceptDriver {

    private static String input = "D:\\ALL\\bigdata\\data\\problem_activity.json";
    private static String output = "D:\\ALL\\bigdata\\data\\produce\\student_problem_concept_label";

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 1.获取job
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        // 2.设置jar路径
        job.setJarByClass(StudentConceptDriver.class);

        // 3.关联mapper 和reducer
        job.setMapperClass(StudentConceptMapper.class);
        job.setReducerClass(StudentConceptReducer.class);

        // 4.设置map输出的kv类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // 5.设置最终输出kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 6.设置输入路径和输出路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // 7.提交job
        boolean result = job.waitForCompletion(true);
        System.exit(result ? 0 : 1);
    }

}
