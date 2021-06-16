package zucc.edu.bigdata.tools;

import java.io.FileWriter;
import java.io.IOException;

public class LineWriter extends FileWriter {
    public LineWriter(String fileName) throws IOException {
        super(fileName);
    }

    @Override
    public void write(String str) throws IOException {
        super.write(str + "\r\n");
    }

    public static void main(String[] args) throws Exception {
        LineWriter writer = new LineWriter("test.json");
        for (int i = 0; i < 5; i++) writer.write("123");
        writer.close();
    }
}