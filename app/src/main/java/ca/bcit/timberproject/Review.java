package ca.bcit.timberproject;

public class Review {
    private String name;
    private String description;
    private float stars;

    public static final Review[] reviews = {
            new Review("Roy Mustang", "In the fictitious country of Amestris, two young brothers, " +
                    "Edward and Alphonse Elric, are bestowed with the gift of alchemy - the supernatural ability to " +
                    "transform matter. This becomes a burden after their mother dies from an illness before they figure " +
                    "out how to use their alchemic skills.", 5),
            new Review("Riza Hawkeye", "In the fictitious country of Amestris, two young brothers, " +
                    "Edward and Alphonse Elric, are bestowed with the gift of alchemy - the supernatural ability to " +
                    "transform matter. This becomes a burden after their mother dies from an illness before they figure " +
                    "out how to use their alchemic skills.", 4),
            new Review("Roy Mustang", "In the fictitious country of Amestris, two young brothers, " +
                    "Edward and Alphonse Elric, are bestowed with the gift of alchemy - the supernatural ability to " +
                    "transform matter. This becomes a burden after their mother dies from an illness before they figure " +
                    "out how to use their alchemic skills.", 5),
            new Review("Riza Hawkeye", "In the fictitious country of Amestris, two young brothers, " +
                    "Edward and Alphonse Elric, are bestowed with the gift of alchemy - the supernatural ability to " +
                    "transform matter. This becomes a burden after their mother dies from an illness before they figure " +
                    "out how to use their alchemic skills.", 3),
            new Review("Roy Mustang", "In the fictitious country of Amestris, two young brothers, " +
                    "Edward and Alphonse Elric, are bestowed with the gift of alchemy - the supernatural ability to " +
                    "transform matter. This becomes a burden after their mother dies from an illness before they figure " +
                    "out how to use their alchemic skills.", 2),
            new Review("Riza Hawkeye", "In the fictitious country of Amestris, two young brothers, " +
                    "Edward and Alphonse Elric, are bestowed with the gift of alchemy - the supernatural ability to " +
                    "transform matter. This becomes a burden after their mother dies from an illness before they figure " +
                    "out how to use their alchemic skills.", 1)
    };

    public Review() {}

    public Review(String name, String description,  float stars) {
        this.name = name;
        this.description = description;
        this.stars = stars;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getStars() {
        return stars;
    }
}
