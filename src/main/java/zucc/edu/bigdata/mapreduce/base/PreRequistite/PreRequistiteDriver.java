package zucc.edu.bigdata.mapreduce.base.PreRequistite;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import zucc.edu.bigdata.mapreduce.base.CourseVideo.CourseVideoDriver;
import zucc.edu.bigdata.mapreduce.base.CourseVideo.CourseVideoMapper;
import zucc.edu.bigdata.mapreduce.base.CourseVideo.CourseVideoReducer;

import java.io.IOException;

public class PreRequistiteDriver {

    private static String input = "D:\\ALL\\bigdata\\data\\prerequisite.json";
    private static String output = "D:\\ALL\\bigdata\\data\\produce\\problem_concept_";

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 1.获取job
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        // 2.设置jar路径
        job.setJarByClass(PreRequistiteDriver.class);

        // 3.关联mapper 和reducer
        job.setMapperClass(PreRequistiteMapper.class);
        job.setReducerClass(PreRequistiteReducer.class);

        // 4.设置map输出的kv类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        // 5.设置最终输出kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // 6.设置输入路径和输出路径
        FileInputFormat.setInputPaths(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        // 7.提交job
        boolean result = job.waitForCompletion(true);
        System.exit(result ? 0 : 1);
    }

}
