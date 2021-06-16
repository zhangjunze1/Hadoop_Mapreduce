package zucc.edu.bigdata.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LineReader extends BufferedReader {
    public LineReader(String path) throws Exception {
        super(new FileReader(new File(path)));
    }

    public static void main(String[] args) throws Exception {
        LineReader reader = new LineReader("data/testinput/video_info.json");
        String s;
        int cnt = 0;
        while ((s = reader.readLine()) != null) {
            cnt++;
        }
        System.out.println(cnt);
    }
}
