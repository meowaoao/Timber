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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    FirebaseFirestore db;
    ArrayList<Hike> hikeList;
    HikeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();
        hikeList = new ArrayList<>();

//        ArrayList<Hike> hikes = new ArrayList<>();
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("hikes")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                String hikeName = document.getString("name");
//                                String location = document.getString("region");
//                                String difficulty = document.getString("difficulty");
//                                String timeLength = document.getString("time");
//                                String imageID = document.getString("imageID");
//                                String docID = document.getId();
//                                hikes.add(new Hike(hikeName, location, difficulty, timeLength, imageID, docID));
//                            }
//                        }
//                    }
//                })

        RecyclerView hikeRecycler = view.findViewById(R.id.hikeRecycler);
        loadHikeData();

        adapter = new HikeAdapter(hikeList);
        hikeRecycler.setAdapter(adapter);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        hikeRecycler.setLayoutManager(lm);

        return view;
    }

    public void loadHikeData() {
        db.collection("hikes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() == hikeList.size()) {
                    return;
                }
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    String docID = doc.getId();
                    String name = doc.getString("name");
                    String region = doc.getString("region");
                    String difficulty = doc.getString("difficulty");
                    String timeLength = doc.getString("timeLength");
                    String imageID = doc.getString("imageID");
                    double distance = doc.getDouble("distance");
                    double time = doc.getDouble("time");
                    int elevation =  Math.toIntExact((Long) doc.get("elevation"));
                    String season = doc.getString("season");
                    String dog = doc.getString("dog");
                    String camping = doc.getString("camping");
                    String description = doc.getString("description");
                    Hike hike = new Hike(name, region, difficulty, timeLength, imageID, distance,
                        time, elevation, season, dog, camping, description, docID);
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
