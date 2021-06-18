package zucc.edu.bigdata.mapreduce.base.StudentConcept.StudentConceptGrasp;


import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StudentConceptGraspReducer extends Reducer<Text, Text, Text, Text> {

    private Text VconceptAndLabel = new Text();

    private int label;
    // 存储String 的concept + label
    private String stringValue = "";
    // concept
    private String concept= "";
    // 最后一位 答题正确与否
    private String stringlabel = "";
    // 总次数
    private int allLabel = 0;
    private int rightLabel = 0;

    private Map<String, String> map = new HashMap<String, String>();

    private String TotalGrasp = "";
    private String almostGrasp = "";
    private Text outV = new Text();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        label = 0;
        stringValue ="";
        stringlabel ="";
        concept="";
        allLabel=0;
        rightLabel=0;
        map.clear();
        TotalGrasp = "";
        almostGrasp ="";
        for (Text value : values) {
            String[] words = value.toString().split(" ");
            concept = words[0];
            stringValue = value.toString();
            stringlabel = words[1];

            if (Integer.valueOf(stringlabel) == 1) {
                allLabel = 1;
                rightLabel = 1;
            } else {
                allLabel = 1;
                rightLabel = 0;
            }
            // 没有 这个concept就存值为1
            if (!map.containsKey(key.toString()+ " "+ concept)) {
                map.put(key.toString()+ " "+ concept, concept + " " + String.valueOf(allLabel) + " " + String.valueOf(rightLabel));
            } else {
                String[] threeParts = map.get(key.toString()+ " "+ concept).split(" ");
                allLabel = Integer.valueOf(threeParts[1]) + allLabel;
                rightLabel = Integer.valueOf(threeParts[2]) + rightLabel;
                map.put(key.toString()+ " "+ concept, concept + " " + String.valueOf(allLabel) + " " +  String.valueOf(rightLabel));
            }
        }

        for (String mapkey : map.keySet()) {
            String[] temp3 = map.get(mapkey).split(" ");

            if ((Integer.valueOf(temp3[2])*100) / Integer.valueOf(temp3[1]) >= 80) {
                TotalGrasp = TotalGrasp + temp3[0] + " ";
            } else if ((Integer.valueOf(temp3[2])*100) / Integer.valueOf(temp3[1]) >= 50) {
                almostGrasp = almostGrasp + temp3[0] + " ";
            }

        }
        if (!TotalGrasp.equals("")){
            TotalGrasp = TotalGrasp.substring(0, TotalGrasp.length() - 1);// 删除多余的空格
        }
        if (!almostGrasp.equals("")){
            almostGrasp = almostGrasp.substring(0, almostGrasp.length() - 1);// 删除多余的空格
        }

        outV.set(TotalGrasp);
        context.write(key, outV);
    }
}
