package ca.bcit.timberproject;

public class Hike{
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

//    public static Hike[] hikes;

    public static Hike[] hikes = {
            new Hike("Pender Hill", "Sunshine Coast", "Intermediate", "2k - 1.25 hours", "https://www.vancouvertrails.com/images/photos/lighting-lakes-loop-6.jpg"
                    , 2, 1.25, 1200, "Year-Round", "Yes", "No", "description"),
            new Hike("Falls lake", "Sunshine Coast", "Advanced", "4k - 3 hours", "https://www.vancouvertrails.com/images/photos/lighting-lakes-loop-6.jpg"
                    , 2, 1.25, 1200, "Year-Round", "Yes", "No", "description"),
            new Hike("Lightning Loop Lake", "Sunshine Coast", "Beginner", "1k - 1 hour", "https://www.vancouvertrails.com/images/photos/lighting-lakes-loop-6.jpg"
                    , 2, 1.25, 1200, "Year-Round", "Yes", "No", "description")
    };

    public static final Hike[] recentHikes = {
            new Hike("Pender Hill", "Sunshine Coast", "Intermediate", "2k - 1.25 hours",
                    "https://www.vancouvertrails.com/images/photos/lighting-lakes-loop-6.jpg"
                    , 2, 1.25, 1200, "Year-Round", "Yes", "No", "description")
    };

//    public static Hike[] hikes = {
//            new Hike("Pender Hill", "Sunshine Coast", "Intermediate", "2k - 1.25 hours", R.drawable.pender_hill
//                    , 2, 1.25, 1200, "Year-Round", "Yes", "No"),
//            new Hike("Falls lake", "Sunshine Coast", "Advanced", "4k - 3 hours", R.drawable.falls_lake
//                    , 2, 1.25, 1200, "Year-Round", "Yes", "No"),
//            new Hike("Lightning Loop Lake", "Sunshine Coast", "Beginner", "1k - 1 hour", R.drawable.lightning_loop
//                    , 2, 1.25, 1200, "Year-Round", "Yes", "No")
//    };
//
//    public static final Hike[] recentHikes = {
//            new Hike("Pender Hill", "Sunshine Coast", "Intermediate", "2k - 1.25 hours",
//                    R.drawable.pender_hill, 2, 1.25, 1200, "Year-Round", "Yes", "No")
//    };

    public Hike() {

    }

    private Hike(String name, String region, String difficulty, String timeLength, String imageID,
                 double distance, double time, int elevation, String season, String dog,
                 String camping, String description) {
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
}
