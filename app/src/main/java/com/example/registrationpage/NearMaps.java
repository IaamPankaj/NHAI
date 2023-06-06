package com.example.registrationpage;

import static android.content.ContentValues.TAG;

import static com.google.api.ResourceProto.resource;
import static com.google.firebase.firestore.remote.Stream.State.Error;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.example.registrationpage.databinding.ActivityNearMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;

public class NearMaps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
//    private ActivityNearMapsBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private  static  final int Request_code = 101;
    private double lat,lng;
    ImageButton hospital,atm,restaurant,hotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_maps);

//        binding = ActivityNearMapsBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        hospital = findViewById(R.id.hospital);
        atm = findViewById(R.id.atm);
        restaurant = findViewById(R.id.restaurant);
        hotel = findViewById(R.id.hotel);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getApplicationContext());

         
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        atm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    StringBuilder stringBuilder = new StringBuilder
                            (" https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                    stringBuilder.append("location=" + lat + "," + lng);
                    stringBuilder.append("&radius=1000");
                    stringBuilder.append("&type=atm");
                    stringBuilder.append("&sensor=true");
                    stringBuilder.append("&key=AIzaSyDRxg2JNiAQaORroerqF9tFSNB1VnNn5AM");
                   // Log.i("ERROR123:",stringBuilder);

                    String url = stringBuilder.toString();
//                    Log.i("ERROR123:",url);
                    Log.i("ERROR123:",url);

                    Object dataFetch[] = new Object[2];
                    dataFetch[0] = mMap;
                    dataFetch[1] = url;

                    FetchData fetchData = new FetchData();
                    fetchData.execute(dataFetch);
                } catch (Exception e) {
                    Log.i("ERROR:",e.getMessage()+" / " + e.toString());
//
                }
            }
        });

        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    StringBuilder stringBuilder = new StringBuilder
                            ("https://maps.googleapis.com/maps/api/geocode/json?");
                    stringBuilder.append("location=" + lat + "," + lng);
                    stringBuilder.append("&radius=1000");
                    stringBuilder.append("&type=hospital");
                    stringBuilder.append("&sensor=true");
                    stringBuilder.append("&key=AIzaSyDRxg2JNiAQaORroerqF9tFSNB1VnNn5AM" );

//                    AIzaSyAZRZoUM-S9fJGw9IRWDfhxSiK5sr7jj4c
//                    AIzaSyC5vAUmnLKPzN43h4tbl5U0a_xRDkOmvWw

                    String url = stringBuilder.toString();
                    Object dataFetch[] = new Object[2];
                    dataFetch[0] = mMap;
                    dataFetch[1] = url;

                    FetchData fetchData = new FetchData();
                    fetchData.execute(dataFetch);
                }catch (Exception e){
                        Log.i("EORROR:",e.getMessage()+" / "+e.toString());
                }
            }
        });


        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder stringBuilder = new StringBuilder
                        (" https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                stringBuilder.append("location="+ lat + "," +lng);
                stringBuilder.append("&radius=1000");
                stringBuilder.append("&type=restaurant");
                stringBuilder.append("&sensor=true");
                stringBuilder.append("&key=" +getResources().getString(R.string.goole_map_key));

                String url = stringBuilder.toString();
                Object dataFetch[]=new Object[2];
                dataFetch[0]=mMap;
                dataFetch[1]=url;

                FetchData fetchData = new FetchData();
                fetchData.execute(dataFetch);
            }
        });

        hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder stringBuilder = new StringBuilder
                        (" https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                stringBuilder.append("location"+ lat + "," +lng);
                stringBuilder.append("&radius=1000");
                stringBuilder.append("&type=hotel");
                stringBuilder.append("&sensor=true");
                stringBuilder.append("&key=" +getResources().getString(R.string.goole_map_key));

                String url = stringBuilder.toString();
                Object dataFetch[]=new Object[2];
                dataFetch[0]=mMap;
                dataFetch[1]=url;

                FetchData fetchData = new FetchData();
                fetchData.execute(dataFetch);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getCurrentLocation();
    }

    private void getCurrentLocation(){

        if(ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                 && ActivityCompat.checkSelfPermission
                (this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions
                    (this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_code);
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(6000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(5000);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult( LocationResult locationResult) {
//                Toast.makeText(getApplicationContext(), "location result is="+ locationResult, Toast.LENGTH_SHORT).show();

                if(locationResult==null){
//                    Toast.makeText(getApplicationContext(), "current location is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                for(Location location:locationResult.getLocations()){

                    if(location!= null){
//                        Toast.makeText(getApplicationContext(), "Current location is="+ location.getLongitude(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location!= null){
                    lat = location.getLatitude();
                    lng = location.getLongitude();

                    LatLng latLng = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions().position(latLng).title("current location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (Request_code){

            case Request_code:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    getCurrentLocation();
                }
        }
    }
}