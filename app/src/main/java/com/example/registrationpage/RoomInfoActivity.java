package com.example.registrationpage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RoomInfoActivity extends AppCompatActivity {

    Calendar myCalendar;
    EditText edCheckin,edCheckout,edNum;
    String name,email,phone,address,numberofperson;
    Spinner spinnerType;
    Button btnPeview;
    String roomType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_info);

        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        address = intent.getStringExtra("address");
        numberofperson = intent.getStringExtra("numberofperson");


        myCalendar= Calendar.getInstance();

        edCheckin= (EditText) findViewById(R.id.edCheckin);
        edCheckout= (EditText) findViewById(R.id.edCheckout);
        spinnerType = findViewById(R.id.spinnerType);
        btnPeview = findViewById(R.id.btnPreview);
        edNum = findViewById(R.id.edNum);

        btnPeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomType=spinnerType.getSelectedItem().toString();
                Intent intent = new Intent(RoomInfoActivity.this,FinalActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("address",address);
                intent.putExtra("phone",phone);
                intent.putExtra("numberofperson",numberofperson);
                intent.putExtra("email",email);
                intent.putExtra("roomType",roomType);
                intent.putExtra("checking",edCheckin.getText().toString());
                intent.putExtra("checkout",edCheckout.getText().toString());
                intent.putExtra("num",edNum.getText().toString());
                startActivity(intent);
            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel(myCalendar,edCheckin);
            }
        };
        edCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(RoomInfoActivity.this,date,
                        myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        DatePickerDialog.OnDateSetListener date2 =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel(myCalendar,edCheckout);
            }
        };
        edCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(RoomInfoActivity.this,date2,myCalendar
                        .get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel(Calendar myCalendar,EditText editText){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(dateFormat.format(myCalendar.getTime()));
    }
}



