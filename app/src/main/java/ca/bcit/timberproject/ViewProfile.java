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
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private SharedPreferences preferences;
    private User user;

    private ImageView profileImg;
    private TextView userName;
    private TextView userDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        preferences = getSharedPreferences("AppPref", 0);
        user = (User) getIntent().getExtras().get("user");

        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.profileDrawerLayout);
        ActionBarDrawerToggle barToggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(barToggle);
        barToggle.syncState();

        NavigationView navigator = findViewById(R.id.profileNavMenu);
        navigator.setNavigationItemSelectedListener(this);

        loadUserProfile();
    }

    /**
     * Updates all fields to display user info.
     */
    public void loadUserProfile() {
        profileImg = findViewById(R.id.viewUserImage);
        userName = findViewById(R.id.viewUserName);
        userDesc = findViewById(R.id.viewUserDesc);

        profileImg.setImageResource(user.getProfileImage());
        userName.setText(user.getName());
        userDesc.setText(user.getProfileDesc());
    }

    /**
     * On start chat button click event handler.
     * @param view view.
     */
    public void startChat(View view) {
        Intent intent = new Intent(view.getContext(), UserChat.class);
        intent.putExtra("userID", user.getUserID());
        startActivity(intent);
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
}