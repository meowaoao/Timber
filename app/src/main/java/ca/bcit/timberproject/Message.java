package ca.bcit.timberproject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class Message implements Parcelable {
    private String userID;
    private String textBody;
    private String textTime;
    private Timestamp timestamp;

    public Message(String userID, String textBody, String textTime, Timestamp timestamp) {
        this.userID = userID;
        this.textBody = textBody;
        this.textTime = textTime;
        this.timestamp = timestamp;
    }

    protected Message(Parcel in) {
        userID = in.readString();
        textBody = in.readString();
        textTime = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getUserID() {
        return userID;
    }

    public String getTextBody() {
        return textBody;
    }

    public String getTextTime() {
        return textTime;
    }

    public Timestamp getTimestamp() { return timestamp; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeString(textBody);
        dest.writeString(textTime);
        dest.writeParcelable(timestamp, flags);
    }
}
