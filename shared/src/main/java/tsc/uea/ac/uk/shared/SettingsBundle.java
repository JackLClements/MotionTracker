package tsc.uea.ac.uk.shared;

/**
 * Bundle class for settings.
 * Created by Jack L. Clements on 17/04/2017.
 */

public class SettingsBundle {
    private boolean accelGrav;
    private boolean gyroRotate;
    private String lang;

    public SettingsBundle(){
        lang = "";
    }

    public SettingsBundle(boolean accelGrav, boolean gyroRotate){
        this.accelGrav = accelGrav;
        this.gyroRotate = gyroRotate;
        this.lang = "eng";
    }

    public SettingsBundle(boolean accelGrav, boolean gyroRotate, String lang){
        this.accelGrav = accelGrav;
        this.gyroRotate = gyroRotate;
        this.lang = lang;
    }

    public void setAccelGrav(boolean accelGrav){
        this.accelGrav = accelGrav;
    }

    public void setGyroRotate(boolean gyroRotate){
        this.gyroRotate = gyroRotate;
    }

    public void setLang(String lang){
        this.lang = lang;
    }

    public boolean getAccelGrav(){
        return this.accelGrav;
    }

    public boolean getGyroRotate(){
        return this.gyroRotate;
    }

    public String getLang(){
        return this.lang;
    }


}
