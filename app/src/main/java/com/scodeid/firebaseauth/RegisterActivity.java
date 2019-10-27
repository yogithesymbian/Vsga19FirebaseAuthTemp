package com.scodeid.firebaseauth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    Button btnLogin, btnSet, btnSelectFoto;
    EditText edtEmail, edtPass, edtUsername;
    ImageView selectImageCircle;

//    TextView txtDataPersistance;
    Uri uriImage;
    Bitmap bitmapImage;

    // declare Pojo class
    User user;

    private String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        edtEmail = findViewById(R.id.edt_email);
        edtPass = findViewById(R.id.edt_pass);
        btnLogin = findViewById(R.id.btn_login);
        edtUsername = findViewById(R.id.edt_username);
        btnSelectFoto = findViewById(R.id.btn_select_photo);
        selectImageCircle = findViewById(R.id.select_circle_image);

        /**
         * Listener on click a component
         */
        btnSelectFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*"); //uri image
                startActivityForResult(intent, 0);

            }
        });
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

    }

    public void updateUI(FirebaseUser firebaseUser) {
        Toast.makeText(this, firebaseUser.getEmail(), Toast.LENGTH_LONG).show();
        Log.d(TAG, "hello " + firebaseUser.getEmail());

        // simpan database ke firebase


//        DatabaseReference ref = firebaseDatabase.getReference("message");
//        ref.setValue("Hello World " + user.getEmail());


        // initialize UUID from user already log in or registered
        String uid = firebaseAuth.getCurrentUser().getUid();
        // initialize database path with unique UUID()
        DatabaseReference databaseReference = firebaseDatabase.getReference("/vsgaUser/" + uid);

        // assign value to
        user = new User(
                "" + edtUsername.getText().toString(),
                "" + edtEmail.getText().toString(),
                "" + edtPass.getText().toString()
        );


        databaseReference.setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Register Successfully " + aVoid);
                    }
                });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (
                requestCode == 0 &&
                resultCode == Activity.RESULT_OK &&
                data != null) {
            Log.d(TAG,"Photo was selected");

            uriImage = data.getData();
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(
                        getContentResolver(),
                        uriImage
                        );
                selectImageCircle.setImageBitmap(bitmapImage);
                btnSelectFoto.setAlpha(0f);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
