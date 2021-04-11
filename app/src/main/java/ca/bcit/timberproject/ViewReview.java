package ca.bcit.timberproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewReview extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    SharedPreferences preferences;
    RecyclerView reviewRecycler;
    ArrayList<Review> reviewList;
    int position;
    Hike hike;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_review);
        preferences = getSharedPreferences("AppPref", 0);
        Toolbar toolbar = findViewById(R.id.reviewToolbar);
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();

        drawer = findViewById(R.id.reviewDrawerLayout);
        ActionBarDrawerToggle barToggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(barToggle);
        barToggle.syncState();

        NavigationView navigator = findViewById(R.id.reviewNavMenu);
        navigator.setNavigationItemSelectedListener(this);

        reviewRecycler = findViewById(R.id.reviewRecycler);
        reviewList = new ArrayList<>();

        position = (int) getIntent().getExtras().get("position");
        hike = Hike.hikes[position];
        loadReviews();
    }

    public void loadReviews() {
        db.collection("hikes")
                .document(hike.getDocID()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<Map<String, Object>> reviews = (List<Map<String, Object>>) document.get("reviews");
                                List<Review> rList = new ArrayList<Review>();

                                for (Map<String, Object> dict: reviews) {
                                    Object starsObject = dict.get("stars");

                                    if (starsObject instanceof Long) {
                                        Review review = new Review(dict.get("name").toString(), dict.get("description").toString(), (Long) starsObject);
                                        if (!reviewList.contains(review)) {
                                            reviewList.add(review);
                                        }
                                    } else {
                                        Double starsDouble = (Double) starsObject;
                                        float stars = starsDouble.floatValue();
                                        Review review = new Review(dict.get("name").toString(), dict.get("description").toString(), stars);
                                        if (!reviewList.contains(review)) {
                                            reviewList.add(review);
                                        }
                                    }

                                }

                                ReviewAdapter adapter = new ReviewAdapter(reviewList);
                                reviewRecycler.setAdapter(adapter);
                                LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
                                reviewRecycler.setLayoutManager(lm);
                            }
                        }
                    }
                });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reviewList.clear();
        loadReviews();
    }

    /**
     * If the back button is pressed with the navigation menu open, it will close the menu
     * instead of exiting the application or going back a page.
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Handles button clicks in the navigation menu and changes the fragment being displayed based
     * on the item that is clicked.
     * @param item menu item.
     * @return boolean.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(drawer.getContext(), MainActivity.class);
        switch (item.getItemId()) {
            case R.id.nav_home:
                intent.putExtra("Frag", R.id.nav_home);
                startActivity(intent);
                break;
            case R.id.nav_recent:
                intent.putExtra("Frag", R.id.nav_recent);
                startActivity(intent);
                break;
            case R.id.nav_connect:
                intent.putExtra("Frag", R.id.nav_connect);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                intent.putExtra("Frag", R.id.nav_profile);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                preferences.edit().remove("user").apply();
                Intent logging = new Intent(this, Login.class);
                startActivity(logging);
                finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write_review, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.review_button:
                Intent i = new Intent(this, ReviewHike.class);
                i.putExtra("position", position);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}