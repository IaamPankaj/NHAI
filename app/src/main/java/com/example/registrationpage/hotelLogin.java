package com.example.registrationpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class hotelLogin extends AppCompatActivity {

private TextView mTextView;
private  Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_login);

        String inforamation = getIntent().getStringExtra("info");

        mTextView = findViewById(R.id.textView);

        mTextView.setText("Room Type: "+ inforamation);

        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(hotelLogin.this,HomeActivity.class);
                startActivity(intent);
            }
        });

    }
}