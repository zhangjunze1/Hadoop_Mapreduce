package zucc.edu.bigdata.mapreduce.base.PreRequistite;


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class PreRequistiteReducer extends Reducer<Text, Text, Text, Text> {

    private Text outV = new Text("");

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        String sum = new String("");

        for (Text value : values) {
            sum += value;
            sum += " ";
        }
        sum = sum.substring(0, sum.length() - 1);// 删除多余的空格
        outV.set(sum);
        context.write(key,outV);

    }
}
