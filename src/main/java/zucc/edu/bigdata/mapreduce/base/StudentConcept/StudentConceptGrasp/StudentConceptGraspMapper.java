package zucc.edu.bigdata.mapreduce.base.StudentConcept.StudentConceptGrasp;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import zucc.edu.bigdata.bean.jsonobject.ProblemActivity;

import java.io.IOException;

public class StudentConceptGraspMapper extends Mapper<LongWritable, Text, Text, Text>{
    // outK studentId
    private Text student_id = new Text();

    // outV concept +" " +label  答题情况
    private Text conceptAndLabel = new Text();

    @Override
    protected void map(LongWritable key, Text value, Mapper.Context context) throws IOException, InterruptedException {
        ProblemActivity problemActivity = new ProblemActivity(value.toString());

        String sum = new String("");
        student_id.set(problemActivity.getStudent_id());

        sum = problemActivity.getConcept().substring(2,problemActivity.getConcept().length()-2) + " " + problemActivity.getLabel();

        conceptAndLabel.set(sum);
        context.write(student_id,conceptAndLabel);
    }

}
