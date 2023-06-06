package com.example.registrationpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
        "(?=.*[0-9])" +
        "(?=.*[a-z])" +
        "(?=.*[A-Z])" +
        "(?=.*[@#$%^&+=])" +
        "(?=\\S+$)" +
        ".{6,}" +
        "$");

    private EditText Username, emails, Phone, Password, Password2;
    private ImageView imageView;
    private Button button, signIn;
    private TextView textView, txtSignIn,compile;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        Username = findViewById(R.id.Username);
        emails = findViewById(R.id.emails);
        Phone = findViewById(R.id.Phone);
        Password = findViewById(R.id.Password);
        Password2 = findViewById(R.id.Password2);
        button = findViewById(R.id.button);
        txtSignIn = findViewById(R.id.txtSignIn);
        textView = findViewById(R.id.textview);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Username.getText().toString().length() >= 3) {
                    if (emails.getText().toString().length() > 4) {
                        if (Phone.getText().toString().length() == 10) {
                            if (Password.getText().toString().length() > 0) {
                                if (Password2.getText().toString().equals(Password.getText().toString())) {

                                    if(!validateEmail()){
                                        Toast.makeText(MainActivity.this, "Email format incorrect", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    if(!validatePassword()){
                                        Toast.makeText(MainActivity.this, "Password too weak", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    HashMap<String, Object> log = new HashMap<>();
                                    log.put("email", emails.getText().toString());
                                    log.put("mobile", Phone.getText().toString());
                                    log.put("username", Username.getText().toString());
                                    log.put("password", Password.getText().toString());

                                    db.collection("Login").document(Phone.getText().toString())
                                            .set(log)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(MainActivity.this, "Registration Successful! Login Now.", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(MainActivity.this, log1.class);
                                                    startActivity(intent);

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
//                                                Log.w(TAG, "Error adding document", e);
                                                    Log.i("EROROROROR:", e.toString() + " / " + e.getMessage());
                                                    Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                } else {
                                    Toast.makeText(MainActivity.this, " Both password is not matching please check ", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, " Please enter the password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Please Enter valid phone no", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Please Enter your valid emails id", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please Enter Valid username", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtSignIn.getVisibility()== View.VISIBLE){
                    startActivity(new Intent(MainActivity.this,log1.class));
                }else{
                    txtSignIn.setVisibility(View.VISIBLE);

                }
//
            }
        });

    }

    private Boolean validateEmail() {
        String emailInput = emails.getEditableText().toString().trim();


        if (emailInput.isEmpty() ) {
            emails.setError("Field cannot be empty");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            emails.setError("please enter a valid email address");
            return false;

        } else {
            //emails.setError("null");
            return true;
        }
    }
    private Boolean validatePassword() {
        String passwordInput = Password.getText().toString().trim();


        if (passwordInput.isEmpty() ) {
            Password.setError("Field cannot be empty");
            Log.i("Passwooorord:","here1");
            return false;
        } else if(!PASSWORD_PATTERN.matcher(passwordInput).matches()){
            Password.setError("Password too weak");
            Log.i("Passwooorord:","here2");
            return false;

        } else {
            //Password.setError("null");
            Log.i("Passwooorord:","here3");
            return true;
        }
    }
    public boolean confirmInput(View v) {
        /*
        if (!validateEmail() | !validatePassword()) {
            return;
        }

        // if the email and password matches, a toast message
        // with email and password is displayed
        String input = "Email: " + emails.getText().toString();
        input += "\n";
        input += "Password: " + Password.getText().toString();
        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();

         */

        if (validateEmail() && validatePassword()) {
            return true;
        }else { return false;}
    }
}







