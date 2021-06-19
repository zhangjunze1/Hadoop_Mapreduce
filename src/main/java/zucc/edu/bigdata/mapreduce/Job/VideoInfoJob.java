package zucc.edu.bigdata.mapreduce.Job;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import zucc.edu.bigdata.bean.jsonobject.VideoInfo;

import java.io.IOException;


/**
 * 处理视频_info中的 视频长度 存入HBase
 * 问题点：
 * 1. name字段中会有重复的 (id 没有重复)
 * 2. 还有有的视频信息是没有start end text的(三个--用视频长度平均值补充)
 * 3. Hbase中建表 对应列簇、列
 */
public class VideoInfoJob extends Configured implements Tool {

    static class VideoInfoMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {
        private byte[] FAMILY = Bytes.toBytes("info"); // 列簇
        private byte[] QUALIFIER = Bytes.toBytes("duration"); // 列
        private double AVERAGE_DURATION = 425.672;  // 得出过程由AverageVideo得出,补充空数据

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            VideoInfo video = new VideoInfo(value.toString());
            byte[] row = Bytes.toBytes(video.getId());
            Put put = new Put(row);
            int n = video.getStart().size();

            // 视频时长
            double duration = AVERAGE_DURATION;
            if (n != 0) {
                duration = video.getEnd().get(n - 1) / 1000.0;
            }
            put.addColumn(FAMILY, QUALIFIER, Bytes.toBytes(duration));

            context.write(new ImmutableBytesWritable(row), put);
        }
    }


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
        job.setJarByClass(VideoInfoJob.class);

        FileInputFormat.setInputPaths(job, inputPath);

        job.setMapperClass(VideoInfoMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(DoubleWritable.class);

        // Hbase提供的工具 自动设置mapreduce提交到hbase里
        TableMapReduceUtil.initTableReducerJob(tableName, null, job);
        job.setNumReduceTasks(0);
        return (job.waitForCompletion(true) ? 0 : 1);
    }

    public static void main(String[] args) throws Exception {
        //两个参数一个是Hadoop 中文件的路径    一个是要传入数据的表名
        String arg1 = "/dataset/video_info.json";
        String arg2 = "video";
        int status = ToolRunner.run(HBaseConfiguration.create(), new VideoInfoJob(), new String[]{arg1, arg2});
        System.exit(status);
    }


}
