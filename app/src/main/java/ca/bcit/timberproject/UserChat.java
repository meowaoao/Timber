package ca.bcit.timberproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class UserChat extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ServiceConnection {
    private DrawerLayout drawer;
    private SharedPreferences preferences;
    private ImageButton sendBtn;
    private EditText msgField;
    private ArrayList<Message> messages;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userID;
    private String friendID;
    private ChatAdapter adapter;
    private ChatSyncService chatSyncService;
    private boolean bound = false;
    RecyclerView messageRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        preferences = getSharedPreferences("AppPref", 0);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userID = user.getUid();
        friendID = (String) getIntent().getExtras().get("userID");

        Toolbar toolbar = findViewById(R.id.chatToolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.chatDrawerLayout);
        ActionBarDrawerToggle barToggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(barToggle);
        barToggle.syncState();

        NavigationView navigator = findViewById(R.id.chatNavMenu);
        navigator.setNavigationItemSelectedListener(this);

        messageRecycler = findViewById(R.id.userChat);

        messages = new ArrayList<>();

        adapter = new ChatAdapter(messages);
        messageRecycler.setAdapter(adapter);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        messageRecycler.setLayoutManager(lm);
        loadMessages();

        sendBtn = findViewById(R.id.msgSendBtn);
        sendBtn.setEnabled(false);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
                LocalDateTime time = LocalDateTime.now();
                String currentTime = formatter.format(time);
                String content = msgField.getText().toString();
                Timestamp timestamp = Timestamp.now();

                Message msg = new Message(userID, content, currentTime, timestamp);
                msgField.setText("");
                sendMessage(msg);
                storeMessage(msg);
            }
        });

        msgField = findViewById(R.id.typeMsg);
        msgField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Not needed.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sendBtn.setEnabled(s.toString().trim().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Not needed.
            }
        });

        startSyncService();
    }

    private void sendMessage(Message userMsg) {
        db.collection("users").document(friendID).collection("messages")
                .document(userID).collection("texts").add(userMsg).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // Do nothing.
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Chat", "Error sending message", e);
            }
        });
    }

    private void storeMessage(Message userMsg) {
        db.collection("users").document(userID).collection("messages")
                .document(friendID).collection("texts").add(userMsg).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                messages.add(userMsg);
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Chat", "Error storing message", e);
            }
        });
    }

    private void loadMessages() {
        db.collection("users").document(userID).collection("messages")
                .document(friendID).collection("texts").orderBy("timestamp").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                    String textBody = doc.getString("textBody");
                    String textTime = doc.getString("textTime");
                    String userID = doc.getString("userID");
                    Timestamp timestamp = doc.getTimestamp("timestamp");
                    Message msg = new Message(userID, textBody,  textTime, timestamp);
                    messages.add(msg);
                }
                adapter.notifyDataSetChanged();
                messageRecycler.scrollToPosition(messages.size() - 1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Chat", "Error loading messages", e);
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
            onStop();
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
    public void onServiceConnected(ComponentName name, IBinder service) {
        ChatSyncService.ServiceBinder chatBinder = (ChatSyncService.ServiceBinder) service;
        chatSyncService = chatBinder.getService();
        bound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        bound = false;
        chatSyncService = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!bound) {
            Intent intent = new Intent(this, ChatSyncService.class);
            intent.putExtra("userID", userID);
            intent.putExtra("friendID", friendID);
            bindService(intent, this, Context.BIND_AUTO_CREATE);
            bound = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
            bound = false;
            unbindService(this);
        }
    }

    private void startSyncService() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (bound && chatSyncService != null) {
                    if (chatSyncService.getUser() == null || chatSyncService.getUser().equals(userID)) {
                        ArrayList<Message> newMessages = chatSyncService.getMessages();
                        System.out.println(messages.size());
                        System.out.println("----");
                        System.out.println(newMessages.size());
                        if (newMessages.size() > messages.size()) {
                            for (int i = messages.size(); i < newMessages.size(); i++) {
                                messages.add(newMessages.get(i));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                    Intent serviceIntent = new Intent(getApplicationContext(), ChatSyncService.class);
                    serviceIntent.putExtra("userID", userID);
                    serviceIntent.putExtra("friendID", friendID);
                    startService(serviceIntent);
                }
                handler.postDelayed(this, 1000 * 2);
            }
        });
    }
}