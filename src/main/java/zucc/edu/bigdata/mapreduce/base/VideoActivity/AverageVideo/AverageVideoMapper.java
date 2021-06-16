package zucc.edu.bigdata.mapreduce.base.VideoActivity.AverageVideo;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import zucc.edu.bigdata.bean.jsonobject.VideoInfo;

import java.io.IOException;

public class AverageVideoMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    // outK
    private Text id = new Text();

    // outV 视频长度
    private IntWritable duration = new IntWritable();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        VideoInfo video = new VideoInfo(value.toString());
        id.set(video.getId());

        int n = video.getStart().size();

        if (n != 0) {
            duration = new IntWritable((int) (video.getEnd().get(n - 1) / 1000.0));
        }
        context.write(id,duration);

    }
}
