package ca.bcit.timberproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewReview extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    RecyclerView reviewRecycler;
    Review[] reviewList;
    ReviewAdapter adapter;

    DatabaseReference databaseReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_review);

        Toolbar toolbar = findViewById(R.id.reviewToolbar);
        setSupportActionBar(toolbar);

        databaseReviews = FirebaseDatabase.getInstance().getReference("Reviews");

        drawer = findViewById(R.id.reviewDrawerLayout);
        ActionBarDrawerToggle barToggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(barToggle);
        barToggle.syncState();

        NavigationView navigator = findViewById(R.id.reviewNavMenu);
        navigator.setNavigationItemSelectedListener(this);

        reviewRecycler = findViewById(R.id.reviewRecycler);
        reviewList = Review.reviews;

        adapter = new ReviewAdapter(reviewList);
        reviewRecycler.setAdapter(adapter);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        reviewRecycler.setLayoutManager(lm);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        databaseReviews.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ArrayList<Review> newReviewList = new ArrayList<>();
//                for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
//                    Review review = reviewSnapshot.getValue(Review.class);
//                    newReviewList.add(review);
//                }
//                Review[] newReviews = new Review[newReviewList.size()];
//                for (int i = 0; i < newReviewList.size(); i++) {
//                    newReviews[i] = newReviewList.get(i);
//                }
//                reviewList = newReviews;
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

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
                finishAndRemoveTask();
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
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}