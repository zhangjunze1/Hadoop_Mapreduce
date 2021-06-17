package zucc.edu.bigdata.mapreduce.base.PreRequistite;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import zucc.edu.bigdata.bean.jsonobject.Course;
import zucc.edu.bigdata.bean.jsonobject.PreRequisite;


import java.io.IOException;

public class PreRequistiteMapper extends Mapper<LongWritable, Text, Text, Text> {

    // outK 后修
    private Text concept_B = new Text();

    // outV
    private Text concept_A = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        PreRequisite preRequisite = JSON.parseObject(value.toString(), PreRequisite.class);


        concept_B.set(preRequisite.getConcept_B());
        concept_A.set(preRequisite.getConcept_A());

        context.write(concept_B,concept_A);
    }
}
