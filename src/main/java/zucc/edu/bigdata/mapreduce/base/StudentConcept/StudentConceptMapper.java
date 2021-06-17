package zucc.edu.bigdata.mapreduce.base.StudentConcept;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import zucc.edu.bigdata.bean.jsonobject.ProblemActivity;

import java.io.IOException;

/**
 * problem_activity
 */
public class StudentConceptMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    // outK studentId + problemId + concept
    private Text student_idAnd = new Text();

    // outV 答题情况（正确）
    private IntWritable label = new IntWritable();


    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        ProblemActivity problemActivity = new ProblemActivity(value.toString());

        String sum = new String("");
        sum = problemActivity.getStudent_id() + " " + problemActivity.getConcept();
        student_idAnd.set(sum);
        int labelTemp = problemActivity.getLabel();
        label.set(labelTemp);
        context.write(student_idAnd,label);

    }
}
