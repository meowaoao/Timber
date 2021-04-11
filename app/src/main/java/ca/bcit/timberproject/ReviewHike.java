package ca.bcit.timberproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ReviewHike extends AppCompatActivity {

    EditText reviewText;
    RatingBar ratingBar;
    Button cancelButton;
    Button submitButton;
    Hike hike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_hike);

        Toolbar toolbar = findViewById(R.id.reviewToolbar);
        setSupportActionBar(toolbar);

        reviewText = (EditText) findViewById(R.id.reviewText);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(submitListener);

        hike = (Hike) getIntent().getExtras().get("hike");

        TextView hikeNameTV = findViewById(R.id.hikeNameTV);
        hikeNameTV.setText(hike.getName());
    }

    private View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            String review_text = reviewText.getText().toString();
            float rating = ratingBar.getRating();

            if (TextUtils.isEmpty(review_text)) {
                Toast.makeText(getApplicationContext(), "Your review cannot be empty.", Toast.LENGTH_LONG).show();
                return;
            }

            String id = user.getUid();
            db.collection("users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String name = documentSnapshot.get("name").toString();

                    Review review = new Review(name, review_text, rating);

                    db.collection("hikes").document(hike.getDocID())
                            .update("reviews", FieldValue.arrayUnion(review))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Review Submitted.", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Something went wrong, review could not be submitted.", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("Main", "Error loading document", e);
                }
            });
        }
    };
}