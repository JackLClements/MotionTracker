package tsc.uea.ac.uk.wearablemotiontracker;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.File;
import java.util.ArrayList;

import static android.content.Context.MODE_APPEND;
import static android.os.Environment.DIRECTORY_DOCUMENTS;

/**
 * Converts data coming in to ARFF format and writes to text file
 *
 * Created by JCLEMENTS on 14/04/2017.
 */

public class ARFFConverter {
    private File saveDir;
    private PrintWriter writer;
    private File file;
    private String id;
    private int samples;

    //test
    FileOutputStream outputStream;

    //output
    ArrayList<String> accelX, accelY, accelZ, gyroX, gyroY, gyroZ;

    /**
     * Constructor with context. Deprecated.
     * @param context
     */
    public ARFFConverter(Context context) {
        if(isExternalStorageWritable()){
            //id = "Test1";
            saveDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Test Directory");
            file = new File(saveDir, "MVMotion.arff");
            accelX = new ArrayList<>();
            accelY = new ArrayList<>();
            accelZ = new ArrayList<>();
            gyroX = new ArrayList<>();
            gyroY = new ArrayList<>();
            gyroZ = new ArrayList<>();
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

    /**
     * Constructor that sets no. of samples
     * @param context context to create file, and check directories
     * @param noOfSamples no of samples per dimension
     */
    public ARFFConverter(Context context, int noOfSamples) {
        samples = noOfSamples;
        if(isExternalStorageWritable()){
            //id = "Test1";
            saveDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Test Directory");
            file = new File(saveDir, "MVMotion.arff");
            /*
            fileX = new File(saveDir, "TestX.txt");
            fileY = new File(saveDir, "TestY.txt");
            fileZ = new File(saveDir, "TestZ.txt");*/
            accelX = new ArrayList<>();
            accelY = new ArrayList<>();
            accelZ = new ArrayList<>();
            gyroX = new ArrayList<>();
            gyroY = new ArrayList<>();
            gyroZ = new ArrayList<>();
            if(!saveDir.mkdirs()){
                Log.e("Error", "Dir. not created");
            }
            try {

                if(!file.exists()){
                    outputStream = new FileOutputStream(file);
                    writer = new PrintWriter(outputStream);
                    writer.write("@relation MVMotion \n");
                    writer.write("@attribute id string \n");
                    writer.write("@attribute series relational \n");
                    for(int i = 0; i < noOfSamples; i++){
                        writer.write("@attribute a" + i + " real \n");
                    }
                    writer.write("@end series \n");
                    String [] activities = context.getResources().getStringArray(R.array.activities);
                    write("@attribute activity {");
                    for(int i = 0; i < activities.length; i++){
                        Log.d("N - ", activities[i]);
                        write(activities[i]);
                        if(i < activities.length - 1){
                            write(", ");
                        }
                    }
                    write("} \n @data");
                    /*
                    writer.write("@attribute activity {");
                    for(int i = 0; i < adapter.getCount(); i++){
                        writer.write(" " + adapter.getItem(i) + ",");
                    }*/
                    writer.write("\n");
                    Toast toast = Toast.makeText(context, "File Written", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    outputStream = new FileOutputStream(file, true);
                    writer = new PrintWriter(outputStream);
                }
            } catch (Exception e) { //shouldn't ever even be called, but y'know...
                Toast toast = Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT);
                toast.show();
                e.printStackTrace();
            }
        }
    }

    /**
     * Re-opens file to add subsequent entries. If file has been deleted, it is remade.
     */
    public void open(){
        if(isExternalStorageWritable()){
            //id = "Test1";
            saveDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Test Directory");
            file = new File(saveDir, "MVMotion.arff");
            /*
            fileX = new File(saveDir, "TestX.txt");
            fileY = new File(saveDir, "TestY.txt");
            fileZ = new File(saveDir, "TestZ.txt");*/
            if(!saveDir.mkdirs()){
                Log.e("Error", "Dir. not created");
            }
            try {

                if(!file.exists()){
                    outputStream = new FileOutputStream(file);
                    writer = new PrintWriter(outputStream);
                    writer.write("@relation MVMotion \n");
                    writer.write("@attribute id string \n");
                    writer.write("@attribute series relational \n");
                    for(int i = 0; i < samples; i++){
                        writer.write("@attribute a" + i + " real \n");
                    }
                    /*
                    writer.write("@attribute activity {");
                    for(int i = 0; i < adapter.getCount(); i++){
                        writer.write(" " + adapter.getItem(i) + ",");
                    }*/
                    writer.write("\n");
                }
                else{
                    outputStream = new FileOutputStream(file, true);
                    writer = new PrintWriter(outputStream);
                }
            } catch (Exception e) { //shouldn't ever even be called, but y'know...
                e.printStackTrace();
            }
        }
    }

    /**
     * Writes string to the file directly.
     * @param s string to be written
     */
    public void write(String s){
        writer.write(s);
    }

    /**
     * Adds data to temporary arrays so that they can be formatted upon conclusion of the entry
     * @param accel acceleration
     * @param gyro gyroscope
     */
    public void writeData(float [] accel, float [] gyro){
        accelX.add("" + accel[0]);
        accelY.add("" + accel[1]);
        accelZ.add("" + accel[2]);
        gyroX.add("" + gyro[0]);
        gyroY.add("" + gyro[1]);
        gyroZ.add("" + gyro[2]);
    }

    /**
     * Processes the collected data into the multivariate format.
     */
    public void processEntry(){
        write("\"");
        /*
        for(int i = 0; i < samples; i++){
            write(accelX.get(i) + ", ");
        }
        write("\\n ");
        for(int i = 0; i < samples; i++){
            write(accelY.get(i) + ", ");
        }
        write("\\n ");
        for(int i = 0; i < samples; i++){
            write(accelZ.get(i) + ", ");
        }
        write("\\n ");
        for(int i = 0; i < samples; i++){
            write(gyroX.get(i) + ", ");
        }
        write("\\n ");
        for(int i = 0; i < samples; i++){
            write(gyroY.get(i) + ", ");
        }
        write("\\n ");
        for(int i = 0; i < samples; i++){
            write(gyroZ.get(i) + ", ");
        }
        write("\"");*/
        processDimension(accelX);
        write("\\n ");
        processDimension(accelY);
        write("\\n ");
        processDimension(accelZ);
        write("\\n ");
        processDimension(gyroX);
        write("\\n ");
        processDimension(gyroY);
        write("\\n ");
        processDimension(gyroZ);
        write("\", ");
        accelX.clear();
        accelY.clear();
        accelZ.clear();
        gyroX.clear();
        gyroY.clear();
        gyroZ.clear();
    }

    /**
     * Adds a formatted dimension to the entry in a file
     * @param dimension dimension to be added
     */
    public void processDimension(ArrayList<String> dimension){
        while(!dimension.isEmpty()){
            String value = dimension.remove(0);
            write(value);
            if(!dimension.isEmpty()){
                write(", ");
            }
        }
    }

    /**
     * Closes file once entry has been written, flushes streams to conserve memory.
     */
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


    /**
     * Checks if external storage is available for read and write
     * @return true if available, otherwise false
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if external storage is available for read and write
     * @return true if available, otherwise false
     */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}
