package zucc.edu.bigdata.mapreduce.base.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * KEYIN: reduce阶段输入的Key的类型：Text（文字）
 * VALUEIN: reduce阶段输入Value的类型: IntWritable(一次)
 * KEYOUT: reduce阶段输出的Key的类型: Text（文字）
 * VALUEOUT: reduce阶段输出的Value的类型: IntWritable(叠加的结果)
 */
public class WordCountReducer extends Reducer<Text, IntWritable,Text,IntWritable> {

    private IntWritable outV = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0;

        // 累加
        for (IntWritable value : values) {
            sum += value.get();
        }

        outV.set(sum);

        // 写出
        context.write(key,outV);

    }
}
