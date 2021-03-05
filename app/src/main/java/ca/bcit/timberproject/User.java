package ca.bcit.timberproject;

public class User {
    private String name;
    private String profileDesc;
    private int profileImage;

    private static String temp = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean suscipit dui ante, accumsan dictum nulla consequat et. " +
            "Praesent nisl diam, condimentum cursus dolor eget, maximus blandit mi. Pellentesque commodo quam nec molestie egestas. " +
            "Quisque blandit turpis pretium, porttitor purus eu, feugiat nunc. Cras aliquam molestie imperdiet. Pellentesque tincidunt at " +
            "purus a vulputate. Donec sed ante ac tellus pharetra pellentesque. Ut ut molestie erat. Suspendisse massa erat, egestas id " +
            "libero sit amet, lobortis viverra neque. Nullam in odio maximus, sollicitudin nisl vitae, facilisis sem. Maecenas congue, velit " +
            "ac eleifend dignissim, nibh elit tristique quam, ac tristique nisi nisl nec tellus. Nulla facilisi. ";

    public static User[] members = {
            new User("Edward Elric", temp, R.drawable.edward),
            new User("Winry Rockbell", temp, R.drawable.winry),
            new User("Dwarf in the Flask", temp, R.drawable.dwarf),
    };

    private User(String name, String desc, int imgID) {
        this.name = name;
        this.profileDesc = desc;
        this.profileImage = imgID;
    }

    public String getName() {
        return name;
    }

    public String getProfileDesc() {
        return profileDesc;
    }

    public int getProfileImage() {
        return profileImage;
    }
}
