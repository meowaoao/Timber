package ca.bcit.timberproject;

import java.io.Serializable;

public class Hike implements Serializable {
    private String description;
    private String camping;
    private String season;
    private String dog;
    private int elevation;
    private double time;
    private double distance;
    private String name;
    private String region;
    private String difficulty;
    private String timeLength;
    private String imageID;
    private String docID;

    public Hike() {

    }

    public Hike(String name, String region, String difficulty, String timeLength, String imageID,
                double distance, double time, int elevation, String season, String dog,
                String camping, String description, String docID) {
        this.name = name;
        this.region = region;
        this.difficulty = difficulty;
        this.timeLength = timeLength;
        this.imageID = imageID;
        this.distance = distance;
        this.time = time;
        this.elevation = elevation;
        this.season = season;
        this.dog = dog;
        this.camping = camping;
        this.description = description;
        this.docID = docID;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getTimeLength() {
        return timeLength;
    }

    public String getSeason() {
        return season;
    }

    public String getDog() {
        return dog;
    }

    public int getElevation() {
        return elevation;
    }

    public double getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getCamping() {
        return camping;
    }

    public double getDistance() {
        return distance;
    }

    public String getImageID() {
        return imageID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getDocID() { return docID; }
}
