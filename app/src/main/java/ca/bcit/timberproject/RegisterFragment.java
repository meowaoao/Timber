package ca.bcit.timberproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {
    EditText emailET, nameET, passwordET, confirmPasswordET;
    Button registerBtn;
    TextView existing;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        db = FirebaseFirestore.getInstance();

        preferences = getContext().getSharedPreferences("AppPref", 0);
        String user = preferences.getString("user", null);
        if (user != null) {
            startActivity(new Intent(getContext(), MainActivity.class));
        }

        emailET = view.findViewById(R.id.register_email);
        nameET = view.findViewById(R.id.register_name);
        passwordET = view.findViewById(R.id.register_password);
        confirmPasswordET = view.findViewById(R.id.register_password_confirm);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getContext(), MainActivity.class));
        }

        existing = view.findViewById(R.id.register_existing);
        existing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.loginContainer, new LoginFragment()).commit();
            }
        });

        registerBtn = view.findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString().trim();
                String name = nameET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                String confirmPW = confirmPasswordET.getText().toString().trim();

                if (email.isEmpty()) {
                    emailET.setError("Please enter e-mail");
                    return;
                }
                if (name.isEmpty()) {
                    nameET.setError("Please enter name");
                    return;
                }
                if (password.isEmpty()) {
                    passwordET.setError("Please enter password");
                    return;
                }
                if (confirmPW.isEmpty()) {
                    confirmPasswordET.setError("Please enter password");
                    return;
                }
                if (!password.equals(confirmPW)) {
                    passwordET.setError("Passwords do not match");
                    confirmPasswordET.setError("Passwords do not match");
                    return;
                }
                emailET.setText("");
                nameET.setText("");
                passwordET.setText("");
                confirmPasswordET.setText("");

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> user = new HashMap<>();
                            user.put("email", email);
                            user.put("name", name);
                            user.put("desc", "Please update profile with a description about you.");
                            user.put("pfp", R.drawable.user_default);
                            db.collection("users").document(firebaseAuth.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Register", "DocumentSnapshot added ");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Register", "Error adding document", e);
                                }
                            });

                            preferences.edit().putString("user", email).apply();
                            startActivity(new Intent(getContext(), MainActivity.class));
                        } else {
                            Toast.makeText(getContext(), "ERROR: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        return view;
    }
}
