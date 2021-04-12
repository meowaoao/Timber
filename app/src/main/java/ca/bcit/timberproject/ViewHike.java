package ca.bcit.timberproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;

public class ViewHike extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    SharedPreferences preferences;
    Hike hike;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hike);
        preferences = getSharedPreferences("AppPref", 0);
        Toolbar toolbar = findViewById(R.id.hikeToolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.hikeDrawerLayout);
        ActionBarDrawerToggle barToggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(barToggle);
        barToggle.syncState();

        NavigationView navigator = findViewById(R.id.hikeNavMenu);
        navigator.setNavigationItemSelectedListener(this);

        db = FirebaseFirestore.getInstance();

        hike = (Hike) getIntent().getExtras().get("hike");
        ViewPager pager = findViewById(R.id.hikeImageSlider);
        ImageAdapter adapter = new ImageAdapter(this, hike);
        pager.setAdapter(adapter);

        TextView nameView = findViewById(R.id.detailsHikeName);
        nameView.setText(hike.getName());
        TextView descView = findViewById(R.id.detailsHikeDesc);
        descView.setText(hike.getDescription());
        TextView locView = findViewById(R.id.detailsHikeLocation);
        locView.setText(hike.getRegion());
        TextView diffView = findViewById(R.id.detailsHikeDifficulty);
        diffView.setText(hike.getDifficulty());
        TextView lengthView = findViewById(R.id.detailsHikeLength);
        String length = getResources().getString(R.string.hikeLength) + " " + hike.getDistance();
        lengthView.setText(length);
        TextView timeView = findViewById(R.id.detailsHikeTime);
        String time = getResources().getString(R.string.hikeTime) + " " + hike.getTime();
        timeView.setText(time);
        TextView elevationView = findViewById(R.id.detailsHikeElevation);
        String elevation = getResources().getString(R.string.hikeElevation) + " " + hike.getElevation();
        elevationView.setText(elevation);
        TextView seasonView = findViewById(R.id.detailsHikeSeason);
        String season = getResources().getString(R.string.hikeSeason) + " " + hike.getSeason();
        seasonView.setText(season);
        TextView dogView = findViewById(R.id.detailsHikeDog);
        String dog = getResources().getString(R.string.hikeDog) + " "  + hike.getDog();
        dogView.setText(dog);
        TextView campView = findViewById(R.id.detailsHikeCamp);
        String camp = getResources().getString(R.string.hikeCamp) + " "  + hike.getCamping();
        campView.setText(camp);

        Button markHikedBtn = findViewById(R.id.markHikedBtn);
        markHikedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                String user = firebaseAuth.getCurrentUser().getUid();

                db.collection("users").document(user).collection("recent")
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Boolean exists = false;
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            if (hike.getName().equals(doc.getString("name"))) {
                                exists = true;
                                Toast.makeText(getApplicationContext(), "This hike has already been marked.", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (!exists) {
                            db.collection("users").document(user).collection("recent").add(hike).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getApplicationContext(), "Successfully marked as hiked.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    public void mapClick(View view) {
        try {
            Intent intent = new Intent(drawer.getContext(), MapsActivity.class);
            Geocoder gc = new Geocoder(getApplicationContext());
            System.out.println(hike.getName());
            List<Address> addressList = gc.getFromLocationName(hike.getName() + " hike", 1);
            double latitude = addressList.get(0).getLatitude();
            double longitude = addressList.get(0).getLongitude();
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            intent.putExtra("hike", hike);
            startActivity(intent);
        }
        catch (IOException e) {
            System.out.println(e.getStackTrace());
        }

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
        getMenuInflater().inflate(R.menu.menu_view_reviews, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_review_button:
                Intent i = new Intent(this, ViewReview.class);
                i.putExtra("hike", hike);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}