package zucc.edu.bigdata.mapreduce.Job;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.Tool;

/**
 * 学生答对的题目的知识点 情况
 */
public class StudentConceptJob extends Configured implements Tool {

    static class StudentConceptMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put>{

    }

    @Override
    public int run(String[] strings) throws Exception {
        return 0;
    }
}
