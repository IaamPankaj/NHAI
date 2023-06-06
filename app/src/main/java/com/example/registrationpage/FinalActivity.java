package com.example.registrationpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class FinalActivity extends AppCompatActivity {

    String name,email,phone,address,numberofperson;
    String roomType,CheckinDate,CheckoutDate,numberOfRooms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        TextView textView = findViewById(R.id.tv1);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        address = intent.getStringExtra("address");
        numberofperson = intent.getStringExtra("numberofperson");
        roomType = intent.getStringExtra("roomType");
        CheckinDate = intent.getStringExtra("checking");
        CheckoutDate = intent.getStringExtra("checkout");
        numberOfRooms = intent.getStringExtra("num");

        textView.setText(
                "Name: "+name +
                        "\nEmail: "+email+    "\nphone "+phone+
                        "\nAddress: "+address+
                        "\nNumberofperson: "+numberofperson+
                        "\nRoomType: "+roomType+
                        "\nCheckinDate: "+CheckinDate+
                        "\nCheckoutDate: "+CheckoutDate+
                        "\nNumberOfRooms: "+numberOfRooms
        );

    }
}