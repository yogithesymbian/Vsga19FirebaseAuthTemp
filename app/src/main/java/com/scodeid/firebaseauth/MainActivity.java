package com.scodeid.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    Button btnLogin, btnSet;
    EditText edtEmail, edtPass;
    TextView txtDataPersistance;

    private String TAG = MainActivity.class.getSimpleName();

    // data persistence on instanceState
    private static final String STATE_HASILNYA = "state_hasilnya";

    // code ini akan dijalankan ketika adanya rotasi layar
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        // save data into statement STATE_HASILNYA
        outState.putString(
                STATE_HASILNYA,
                "testing data persistence : " + edtEmail.getText().toString()
        );
        Log.d(TAG,"onSaveInstanceState : some data string from emailEditText ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtDataPersistance = findViewById(R.id.txt_set);
        btnSet = findViewById(R.id.btn_set_text);

        // check saveinstance
        if (savedInstanceState != null){
            // get data from saveInstanceState
            String resultInstance = savedInstanceState.getString(STATE_HASILNYA);
            // set the data
            txtDataPersistance.setText(resultInstance);
        }

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        edtEmail = findViewById(R.id.edt_email);
        edtPass = findViewById(R.id.edt_pass);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edtEmail.getText().toString();
                String password = edtPass.getText().toString();

                Log.d(TAG, "email : " + email + " \n password : " + password);

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if (user != null) {
                                        updateUI(user);
                                    } else {
                                        Log.d("Main", "user is null");
                                    }
                                }

                            }
                        });
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                txtDataPersistance.setText(
                        "testing data persistence : " + edtEmail.getText().toString()
                );

            }
        });
    }

    public void updateUI(FirebaseUser firebaseUser) {
        Toast.makeText(this, firebaseUser.getEmail(), Toast.LENGTH_LONG).show();
        Log.d(TAG, "hello " + firebaseUser.getEmail());

//        DatabaseReference ref = firebaseDatabase.getReference("message");
//        ref.setValue("Hello World " + user.getEmail());

        // initialize UUID from user already log in or registered
        String uid = firebaseAuth.getCurrentUser().getUid();
        // initialize database path with unique UUID()
        DatabaseReference databaseReference = firebaseDatabase.getReference("/vsgaUser/"+uid);
        // declare Pojo class
        User user;
        // assign value to
        user = new User(
                "username",
                ""+edtEmail.getText().toString(),
                ""+edtPass.getText().toString()
        );


        databaseReference.setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG,"Register Successfully " +aVoid);
                }
            });



    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
////        updateUI(currentUser);
//    }
}
