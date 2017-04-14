package tsc.uea.ac.uk.wearablemotiontracker;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.File;
/**
 * Converts data coming in to ARFF format and writes to text file
 *
 * Created by JCLEMENTS on 14/04/2017.
 */

public class ARFFConverter {
    private PrintWriter writer;
    private File file;
    private String id;


    public ARFFConverter() {
        id = "Test1";
        file = new File("TestFile.txt");
        try {
            writer = new PrintWriter(new FileOutputStream(file));
            writer.write("@relation testfile1");
            writer.write("@attribute id string");
        } catch (Exception e) { //shouldn't ever even be called, but y'know...
            e.printStackTrace();
        }
    }

    public void write(String s){
        writer.write(s);
    }

    public void close(){
        writer.close();
    }


}
