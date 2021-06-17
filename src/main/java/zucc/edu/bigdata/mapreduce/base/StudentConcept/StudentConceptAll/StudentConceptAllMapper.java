package zucc.edu.bigdata.mapreduce.base.StudentConcept.StudentConceptAll;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import zucc.edu.bigdata.bean.jsonobject.ProblemActivity;

import java.io.IOException;

/**
 * problem_activity
 */
public class StudentConceptAllMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    // outK studentId + concept
    private Text student_idAnd = new Text();

    // outV 答题情况总次数
    private IntWritable label = new IntWritable(1);


    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        ProblemActivity problemActivity = new ProblemActivity(value.toString());

        String sum = new String("");
        sum = problemActivity.getStudent_id() + " " + problemActivity.getConcept();
        student_idAnd.set(sum);
        context.write(student_idAnd,label);

    }
}
