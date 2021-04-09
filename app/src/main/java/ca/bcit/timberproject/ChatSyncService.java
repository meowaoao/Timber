package ca.bcit.timberproject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ChatSyncService extends Service {
    private ArrayList<Message> messages = new ArrayList<>();
    private String user;
    private ChatSyncService chatSyncService;
    private IBinder binder = new ServiceBinder();
    private Handler handler;

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public String getUser() { return user; }

    public ChatSyncService() {
        // Do nothing.
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        user = intent.getStringExtra("userID");
        String friendID = intent.getStringExtra("friendID");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        messages.clear();

        db.collection("users").document(user).collection("messages")
                .document(friendID).collection("texts").orderBy("timestamp")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) { ;
                    String textBody = doc.getString("textBody");
                    String textTime = doc.getString("textTime");
                    String userID = doc.getString("userID");
                    Timestamp timestamp = doc.getTimestamp("timestamp");
                    Message msg = new Message(userID, textBody,  textTime, timestamp);
                    messages.add(msg);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Chat", "Error loading messages", e);
            }
        });
        return Service.START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        messages.clear();
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class ServiceBinder extends Binder {
        ChatSyncService getService() {
            return ChatSyncService.this;
        }
    }

    /**
     * Stops service when app is closed fully.
     * @param rootIntent Intent.
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }
}
