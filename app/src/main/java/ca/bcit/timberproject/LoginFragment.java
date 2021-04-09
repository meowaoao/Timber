package ca.bcit.timberproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {
    TextView forgot_pass, register;
    Button loginBtn;
    EditText emailET, passwordET;
    FirebaseAuth firebaseAuth;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        preferences = getContext().getSharedPreferences("AppPref", 0);
        String user = preferences.getString("user", null);
        if (user != null) {
            startActivity(new Intent(getContext(), MainActivity.class));
        }

        firebaseAuth = FirebaseAuth.getInstance();

        forgot_pass = view.findViewById(R.id.login_forgot);
        register = view.findViewById(R.id.login_register);
        loginBtn = view.findViewById(R.id.login_btn);
        emailET = view.findViewById(R.id.login_email);
        passwordET = view.findViewById(R.id.login_password);

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.loginContainer, new RegisterFragment()).commit();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();

                if (email.isEmpty()) {
                    emailET.setError("Please enter e-mail");
                    return;
                }
                if (password.isEmpty()) {
                    passwordET.setError("Please enter password");
                    return;
                }
                emailET.setText("");
                passwordET.setText("");

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
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
