package zucc.edu.bigdata.mapreduce.base.ProblemActTrain.all;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import zucc.edu.bigdata.bean.jsonobject.ProblemBehavior;

import java.io.IOException;

public class ProblemAllActTrainMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    // outK
    private Text problem_id = new Text();

    // outV
    private IntWritable lable = new IntWritable(1);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        ProblemBehavior problemBehavior = JSON.parseObject(value.toString(), ProblemBehavior.class);
        problem_id.set(problemBehavior.getProblem_id());

        context.write(problem_id, lable);

    }
}
