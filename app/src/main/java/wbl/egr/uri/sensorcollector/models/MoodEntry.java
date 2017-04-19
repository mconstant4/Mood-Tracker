package wbl.egr.uri.sensorcollector.models;

/**
 * Created by root on 4/9/17.
 */

public class MoodEntry {
    private String mood;

    public MoodEntry(String mood) {
        this.mood = mood;
    }

    public String getMood(){return mood;}

    public void setMood(String mood){this.mood = mood;}
}
