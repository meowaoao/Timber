package ca.bcit.timberproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    SharedPreferences preferences;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;

    TextView userName;
    Hike[] hikeList = new Hike[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("AppPref", 0);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        loadUserData(user);

        System.out.println("<-------------- start Toolbar ---------------->");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle barToggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(barToggle);
        barToggle.syncState();

        System.out.println("<-------------- start NavigationView ---------------->");
        NavigationView navigator = findViewById(R.id.navMenu);
        navigator.setNavigationItemSelectedListener(this);
        userName = navigator.getHeaderView(0).findViewById(R.id.nav_userName);

        loadHikeData();

        /*
         * Checks for intents on the main activity. On opening the app this will be null however if the menu is used from
         * another activity, this will carry a string flag to open a specific fragment.
         */
        Bundle intentItems = getIntent().getExtras();
        if (intentItems != null) {
            int result = intentItems.getInt("Frag");
            onNavigationItemSelected(result);
        }

        //Opens the application on the home fragment if the saved state is null, prevents rotation issues.
        else if (savedInstanceState == null) {
            navigator.setCheckedItem(R.id.nav_home);
            System.out.println("<-------------- start HomeFragment ---------------->");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragHolder, new HomeFragment()).commit();
        }
    }

    public void loadUserData(FirebaseUser user) {
        System.out.println("<-------------- start loadUserData ---------------->");
        String id = user.getUid();
        db.collection("users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userName.setText(documentSnapshot.get("name").toString());
                System.out.println("<-------------- finish loadUserData ---------------->");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Main", "Error loading document", e);
            }
        });
    }

    public void loadHikeData() {
        db.collection("hikes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        System.out.println("<-------------- start toObject inner ---------------->");
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Hike hike = document.toObject(Hike.class);
                                hikeList[i] = hike;
                                i++;
                                System.out.println("<-------------- to Object ---------------->");
                                System.out.println(hike);
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                            Hike.hikes = hikeList;
                            System.out.println(Hike.hikes);
                            System.out.println("<-------------- finish toObject inner ---------------->");
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
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
        switch (item.getItemId()) {
            case R.id.nav_home:
                HomeFragment home = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragHolder, home).commit();
                break;
            case R.id.nav_recent:
                RecentFragment recent = new RecentFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragHolder, recent).commit();
                break;
            case R.id.nav_connect:
                ConnectFragment connector = new ConnectFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragHolder, connector).commit();
                break;
            case R.id.nav_profile:
                ProfileFragment profile = new ProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragHolder, profile).commit();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                preferences.edit().remove("user").apply();
                finishAndRemoveTask();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Overloaded onNavigationItemSelected for use when the navigation menu is used in another activity and
     * an ID is passed to update the frame layout of the main activity.
     * @param item int
     * @return boolean.
     */
    public boolean onNavigationItemSelected(int item) {
        switch (item) {
            case R.id.nav_home:
                HomeFragment home = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragHolder, home).commit();
                break;
            case R.id.nav_recent:
                RecentFragment recent = new RecentFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragHolder, recent).commit();
                break;
            case R.id.nav_connect:
                ConnectFragment connector = new ConnectFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragHolder, connector).commit();
                break;
            case R.id.nav_profile:
                ProfileFragment profile = new ProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragHolder, profile).commit();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                preferences.edit().remove("user").apply();
                finishAndRemoveTask();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}