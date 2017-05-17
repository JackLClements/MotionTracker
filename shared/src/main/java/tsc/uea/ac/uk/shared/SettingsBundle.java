package tsc.uea.ac.uk.shared;

/**
 * Bundle class for settings. Yet to be implemented.
 * Created by Jack L. Clements on 17/04/2017.
 */
public class SettingsBundle {
    private boolean accelGrav;
    private boolean gyroRotate;
    private String lang;

    /**
     * Default constructor
     */
    public SettingsBundle(){
        lang = "";
    }

    /**
     * Value-based constructor, langauge set to default
     * @param accelGrav choice between acceleration and acceleration with gravity
     * @param gyroRotate choice between gyroscopic acceleration and angle of rotation
     */
    public SettingsBundle(boolean accelGrav, boolean gyroRotate){
        this.accelGrav = accelGrav;
        this.gyroRotate = gyroRotate;
        this.lang = "eng";
    }

    /**
     * Value-based constructor, language can be chosene
     * @param accelGrav choice between acceleration and acceleration with gravity
     * @param gyroRotate choice between gyroscopic acceleration and angle of rotation
     * @param lang chosen language
     */
    public SettingsBundle(boolean accelGrav, boolean gyroRotate, String lang){
        this.accelGrav = accelGrav;
        this.gyroRotate = gyroRotate;
        this.lang = lang;
    }

    //Accessor methods

    /**
     * Setter for accelGrav
     * @param accelGrav choice between acceleration and acceleration with gravity
     */
    public void setAccelGrav(boolean accelGrav){
        this.accelGrav = accelGrav;
    }

    /**
     * Setter for gyroRotate
     * @param gyroRotate choice between gyroscopic acceleration and angle of rotation
     */
    public void setGyroRotate(boolean gyroRotate){
        this.gyroRotate = gyroRotate;
    }

    /**
     * Setter for language
     * @param lang language chosen
     */
    public void setLang(String lang){
        this.lang = lang;
    }

    /**
     * Getter for accelgrav
     * @return choice between acceleration and acceleration with gravity
     */
    public boolean getAccelGrav(){
        return this.accelGrav;
    }

    /**
     * Getter for gyrorotate
     * @return choice between gyroscopic acceleration and angle of rotation
     */
    public boolean getGyroRotate(){
        return this.gyroRotate;
    }

    /**
     * Getter for language
     * @return language chosen
     */
    public String getLang(){
        return this.lang;
    }


}
