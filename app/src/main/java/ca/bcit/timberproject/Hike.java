package ca.bcit.timberproject;

public class Hike {
    private String name;
    private String location;
    private String difficulty;
    private String timeLength;
    private int imageID;

    public static final Hike[] hikes = {
            new Hike("Pender Hill", "Sunshine Coast", "Intermediate", "2k - 1.25 hours", R.drawable.pender_hill),
            new Hike("Falls lake", "Sunshine Coast", "Advanced", "4k - 3 hours", R.drawable.falls_lake),
            new Hike("Lightning Loop Lake", "Sunshine Coast", "Beginner", "1k - 1 hour", R.drawable.lightning_loop)
    };

    public static final Hike[] recentHikes = {
            new Hike("Pender Hill", "Sunshine Coast", "Intermediate", "2k - 1.25 hours", R.drawable.pender_hill)
    };

    private Hike(String name, String location, String difficulty, String timeLength, int imgID) {
        this.name = name;
        this.location = location;
        this.difficulty = difficulty;
        this.timeLength = timeLength;
        this.imageID = imgID;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getTimeLength() {
        return timeLength;
    }

    public int getImageID() {
        return imageID;
    }
}
