package ca.bcit.timberproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {
    Button editProfileBtn;

    @Override
    public void onStart() {
        super.onStart();
        View v = getView();
        if (v != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            String id = user.getUid();
            db.collection("users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String name = documentSnapshot.get("name").toString();
                    String desc = documentSnapshot.get("desc").toString();

                    TextView nameTV = v.findViewById(R.id.nameText);
                    nameTV.setText(name);

                    TextView emailTV = v.findViewById(R.id.emailText);
                    emailTV.setText(user.getEmail());

                    TextView descTV = v.findViewById(R.id.userDesc);
                    descTV.setText(desc);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("Main", "Error loading document", e);
                }
            });
        }

        editProfileBtn = (Button) v.findViewById(R.id.editProfile);
        editProfileBtn.setOnClickListener(editListener);
    }

    private View.OnClickListener editListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getContext(), EditProfile.class);
            startActivity(i);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}
