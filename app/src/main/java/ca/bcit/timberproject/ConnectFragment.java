package ca.bcit.timberproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class ConnectFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    ArrayList<User> usersList;
    ConnectAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_connect, container, false);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userID = user.getUid();

        RecyclerView connectRecycler = view.findViewById(R.id.connectRecycler);
        usersList = new ArrayList<>();

        adapter = new ConnectAdapter(usersList);
        connectRecycler.setAdapter(adapter);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        connectRecycler.setLayoutManager(lm);

        loadUsers(userID);

        return view;
    }

    private void loadUsers(String userID) {
        db.collection("users").orderBy("name").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                    String uID = doc.getId();
                    if (uID.equals(userID)) {
                        continue;
                    }
                    String name = doc.get("name").toString();
                    String desc = "";
                    int imgID = R.drawable.dwarf;
                    User user = new User(uID, name, desc, imgID);
                    usersList.add(user);
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Chat", "Error sending message", e);
            }
        });
    }
}