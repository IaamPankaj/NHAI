package com.example.registrationpage;

import static android.content.ContentValues.TAG;
import static com.google.api.AnnotationsProto.http;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;

public class log1 extends AppCompatActivity {

    GoogleSignInClient gsc;
    GoogleSignInOptions gso;
    CallbackManager callbackManager;


    private ImageView imageView,googleBtn,fb_btn,linkedIn;
    private EditText emails,Password;
    private TextView textView,textView2,txtsign_up;
    private Button login;


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log1);

        callbackManager = CallbackManager.Factory.create();


        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        startActivity(new Intent(log1.this,ThirdActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);



        imageView = findViewById(R.id.imageView);
        emails = findViewById(R.id.emails);
        Password = findViewById(R.id.Password);
        textView = findViewById(R.id.textview);
        textView2 = findViewById(R.id.textView2);
        txtsign_up = findViewById(R.id.txtSignup);
        login = findViewById(R.id.login);
        googleBtn = findViewById(R.id.googleBtn);
        fb_btn = findViewById(R.id.fb_btn);
        linkedIn = findViewById(R.id.linkedIn);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emails.getText().toString().trim().length() > 4){
                    if(Password.getText().toString().trim().length() > 3) {

                        db.collection("Login").whereEqualTo("email",emails.getText().toString().trim())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.getResult().size() > 0) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                    if (document.getData().get("password").toString().equals(Password.getText().toString().trim())) {

                                                        startActivity(new Intent(log1.this,Voice.class));

                                                    } else {
                                                        Toast.makeText(log1.this, "Please Enter proper credentials", Toast.LENGTH_SHORT).show();

//                                                        Toast.makeText(log1.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
                                                        Log.d("TAG", document.getData().get("password").toString() + " / " + Password.getText().toString().trim());
                                                    }
//                                                Log.d("TAG12345", document.getId() + " => " + document.getData());
                                                }
                                            } else {
                                                Log.w("TAG", "Error getting documents.", task.getException());
                                            }
                                        }else {
                                            Toast.makeText(log1.this, "No User found!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }else{
                        Toast.makeText(log1.this, "Please Enter proper password credentials", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(log1.this, "Please enter proper email credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });

        txtsign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(log1.this,MainActivity.class);
                startActivity(intent);

            }
        });

//        googleBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
//                startActivity(intent);
//            }
//        });

        fb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginManager.getInstance().logInWithReadPermissions(log1.this, Arrays.asList("public_profile"));
            }
        });

        linkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.LinkedIn.com"));
                startActivity(intent);
            }
        });
    }


    void signIn(){

        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
    }
    void navigateToSecondActivity(){
        finish();
        Intent intent = new Intent(log1.this,SecondActivity.class);

        startActivity(intent);
    }

}