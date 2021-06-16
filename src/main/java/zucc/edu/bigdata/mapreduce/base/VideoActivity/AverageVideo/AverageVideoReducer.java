package zucc.edu.bigdata.mapreduce.base.VideoActivity.AverageVideo;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class AverageVideoReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    // 平均视频长度
    private IntWritable aveDuration = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sumDuration = 0;
        int n = 0;
        for (IntWritable value : values) {
            n++;
            sumDuration += value.get();
        }
        int aveVideo = (int) (sumDuration / n);
        aveDuration.set(aveVideo);
        context.write(key, aveDuration);
    }
}
