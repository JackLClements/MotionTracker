package tsc.uea.ac.uk.wearablemotiontracker;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.File;

import static android.os.Environment.DIRECTORY_DOCUMENTS;

/**
 * Converts data coming in to ARFF format and writes to text file
 *
 * Created by JCLEMENTS on 14/04/2017.
 */

public class ARFFConverter {
    private File saveDir;
    private PrintWriter writer;
    private File file, fileX, fileY, fileZ;
    private String id;

    //test
    FileOutputStream outputStream;


    public ARFFConverter(Context context) {
        if(isExternalStorageWritable()){
            //id = "Test1";
            saveDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Test Directory");
            file = new File(saveDir, "TestFile.txt");
            /*
            fileX = new File(saveDir, "TestX.txt");
            fileY = new File(saveDir, "TestY.txt");
            fileZ = new File(saveDir, "TestZ.txt");*/
            if(!saveDir.mkdirs()){
                Log.e("Error", "Dir. not created");
            }
            try {
                outputStream = new FileOutputStream(file);
                writer = new PrintWriter(outputStream);
                writer.write("@relation testfile1 \n");
                writer.write("@attribute id string \n");
                id = "File Written";
                Toast toast = Toast.makeText(context, id, Toast.LENGTH_SHORT);
                toast.show();
            } catch (Exception e) { //shouldn't ever even be called, but y'know...
                Toast toast = Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT);
                toast.show();
                e.printStackTrace();
            }
        }
    }

    public void write(String s){
        writer.write(s);
    }

    public void close(){
        try{
            writer.flush();
            writer.close();
            outputStream.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}
