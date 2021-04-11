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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class RecentFragment extends Fragment {
    FirebaseFirestore db;
    ArrayList<Hike> hikeList;
    HikeAdapter adapter;
    FirebaseAuth firebaseAuth;
    String user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_recent, container, false);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser().getUid();
        hikeList = new ArrayList<>();

        RecyclerView recentRecycler = view.findViewById(R.id.recentRecycler);
        loadRecentHikes();

        adapter = new HikeAdapter(hikeList);
        recentRecycler.setAdapter(adapter);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        recentRecycler.setLayoutManager(lm);

        return view;
    }

    public void loadRecentHikes() {
        db.collection("users").document(user).collection("recent")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() == hikeList.size()) {
                    return;
                }
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    String name = doc.getString("name");
                    String region = doc.getString("region");
                    String difficulty = doc.getString("difficulty");
                    String timeLength = doc.get("time").toString();
                    String imageID = doc.getString("imageID");
                    double distance = doc.getDouble("distance");
                    double time = doc.getDouble("time");
                    int elevation =  Math.toIntExact((Long) doc.get("elevation"));
                    String season = doc.getString("season");
                    String dog = doc.getString("dog");
                    String camping = doc.getString("camping");
                    String description = doc.getString("description");
                    Hike hike = new Hike(name, region, difficulty, timeLength, imageID, distance,
                            time, elevation, season, dog, camping, description);
                    hikeList.add(hike);
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Chat", "Error loading hikes", e);
            }
        });
    }
}
