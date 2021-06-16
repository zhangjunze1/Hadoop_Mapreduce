package zucc.edu.bigdata.mapreduce.base.ProblemActTrain.right;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import zucc.edu.bigdata.bean.jsonobject.ProblemBehavior;

import java.io.IOException;

public class ProblemRightActTrainMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    // outK
    private Text problem_id = new Text();

    // outV
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
