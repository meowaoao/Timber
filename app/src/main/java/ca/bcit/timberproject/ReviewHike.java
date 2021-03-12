package ca.bcit.timberproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class ReviewHike extends AppCompatActivity {

    EditText reviewText;
    RatingBar ratingBar;
    Button uploadPhotoButton;
    Button cancelButton;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_hike);

        Toolbar toolbar = findViewById(R.id.reviewToolbar);
        setSupportActionBar(toolbar);

        reviewText = (EditText) findViewById(R.id.reviewText);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        uploadPhotoButton = (Button) findViewById(R.id.upload_photo_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        submitButton = (Button) findViewById(R.id.submit_button);
    }
}