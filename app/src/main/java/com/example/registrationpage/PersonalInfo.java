package com.example.registrationpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PersonalInfo extends AppCompatActivity {

    private Button btnSave;
    EditText edAddress,edName,edPhone,edEmail,edNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        btnSave = findViewById(R.id.btnSave);
        edAddress = findViewById(R.id.edAddress);
        edName = findViewById(R.id.edName);
        edPhone = findViewById(R.id.edPhone);
        edEmail = findViewById(R.id.edEmail);
        edNum = findViewById(R.id.edNum);



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalInfo.this,RoomInfoActivity.class);
                intent.putExtra("name",edName.getText().toString());
                intent.putExtra("address",edAddress.getText().toString());
                intent.putExtra("phone",edPhone.getText().toString());
                intent.putExtra("numberofperson",edNum.getText().toString());
                intent.putExtra("email",edEmail.getText().toString());
                startActivity(intent);

            }
        });
    }
}