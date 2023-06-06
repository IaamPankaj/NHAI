package com.example.registrationpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.common.collect.BiMap;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//
import javax.xml.transform.Source;

import io.grpc.internal.JsonParser;

public class homePage extends AppCompatActivity  {

    private ImageView room1, room2, room3, room4, nhai_logo, logout,chatbot,img1,idVoice,img2,img3,img4,img5,img6,img7,
            idBtnTranslate;
    Spinner spType;
    private Button button,btnBookNow,read,btnSend;
    private TextView BHotel3,About1,Login1,Register1,Contact1;
    NavigationView navigationView;
    Toolbar toolbar;
    private double getLatitude,getLongitude;
    Location location;

//    private GoogleMap mMap;
//    private FusedLocationProviderClient fusedLocationProviderClient;
//    private  static  final int Request_code = 101;
//    private double lat,lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        spType = findViewById(R.id.sp_type);
        nhai_logo = findViewById(R.id.nhai_logo);
        logout = findViewById(R.id.logout);
        btnSend = findViewById(R.id.btnSend);
//        chatbot = findViewById(R.id.chatbot);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);
        img6 = findViewById(R.id.img6);
        img7 = findViewById(R.id.img7);


        button = findViewById(R.id.button);
        read = findViewById(R.id.read);
        btnBookNow = findViewById(R.id.btnBookNow);
        BHotel3 = findViewById(R.id.BHotel3);
        idVoice = findViewById(R.id.idVoice);
        About1 = findViewById(R.id.About1);
        Login1 = findViewById(R.id.Login1);
        Register1 = findViewById(R.id.Register1);
//        navigationView = findViewById(R.id.navigationview);
//        toolbar = findViewById(R.id.toolbar);

        idBtnTranslate = findViewById(R.id.idBtnTranslate);

//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,toolbar,R.string.navigration_open,R.string.navigration_closed);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this,homepage2.class);
                startActivity(intent);
            }
        });

//        btnSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(homePage.this,PersonalInfo.class);
//                startActivity(intent);
//            }
//        });
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this,AboutPage.class);
                startActivity(intent);

            }
        });

        btnBookNow = findViewById(R.id.btnBookNow);
        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this,PersonalInfo.class);
                intent.putExtra("info","Single Bedroom");
                startActivity(intent);
            }
        });
        BHotel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager smsManager = SmsManager.getDefault();
                //DbHelper db = new DbHelper(SensorService.this);
                String message
                        = "Hey, "
                        + " I am in DANGER, i need help. Please urgently reach me out. Here are my coordinates.\n "
                        + "http://maps.google.com/?q=";
//                        + location.getLatitude() + ","
//                        + location.getLongitude();
//                smsManager.sendTextMessage("+917357027318", null,
//                        message, null, null);
                smsManager.sendTextMessage("+919653281009", null,
                        message, null, null);
//                smsManager.sendTextMessage("+918691854416", null,
//                        message, null, null);
//                smsManager.sendTextMessage("+919372900345", null,
//                        message, null, null);


            }

        });

        idBtnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this,Language_Translator.class);
                startActivity(intent);
            }
        });

        ArrayAdapter<CharSequence> aa = ArrayAdapter.createFromResource
                (this, R.array.menu, android.R.layout.simple_spinner_item);
        aa.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spType.setAdapter(aa);



        nhai_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this,homePage.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this,log1.class);
                startActivity(intent);
            }
        });
//        chatbot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(homePage.this,ChatBot.class);
//                startActivity(intent);
//            }
//        });
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this,NearMaps.class);
                startActivity(intent);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this,NearMaps.class);
                startActivity(intent);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this,NearMaps.class);
                startActivity(intent);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this,NearMaps.class);
                startActivity(intent);
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this,NearMaps.class);
                startActivity(intent);
            }
        });
        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this,NearMaps.class);
                startActivity(intent);
            }
        });
        img7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this,NearMaps.class);
                startActivity(intent);
            }
        });

        idVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this,Voice.class);
                startActivity(intent);

            }
        });

        About1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this,AboutPage.class);
                startActivity(intent);

            }
        });
        Login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this,log1.class);
                startActivity(intent);
            }
        });

        Register1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
