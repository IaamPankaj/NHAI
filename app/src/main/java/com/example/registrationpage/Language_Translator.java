package com.example.registrationpage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.ArrayList;
import java.util.Locale;

public class Language_Translator extends AppCompatActivity {

    private Spinner fromSpinner,toSpinner;
    private TextInputEditText sourceEdt;
    private ImageView micIV;
    private MaterialButton idBtnTranslate;
    private TextView idTVTranslateTV;
    String[] fromLanguages = {"From","English","Hindi","Arabic","Marathi","Gujarati","Afrikaans","Belarusian","Bulgarian","Bengali","Catalan",
                                "Czech","Welsh","Urdu"};
    String[] toLanguages = {"To","English","Hindi","Marathi","Gujarati","Afrikaans","Arabic","Belarusian","Bulgarian","Bengali","Catalan",
            "Czech","Welsh","Urdu"};

    private static final int REQUEST_PERMISSION_CODE = 1 ;

    int languageCode,fromLanguageCode,toLanguageCode = 0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_translator);

        fromSpinner = findViewById(R.id.idFromSpinner);
        toSpinner = findViewById(R.id.idToSpinner);
        sourceEdt = findViewById(R.id.idEdtSource);
        micIV = findViewById(R.id.idIVMic);
        idBtnTranslate = findViewById(R.id.idBtnTranslate);
        fromSpinner = findViewById(R.id.idFromSpinner);
        idTVTranslateTV = findViewById(R.id.idTVTranslateTV);

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                fromLanguageCode = getLanguageCode(fromLanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter fromAdapter = new ArrayAdapter(this,R.layout.spinner_item,fromLanguages);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                toLanguageCode = getLanguageCode(toLanguages[position]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter toAdapter = new ArrayAdapter(this,R.layout.spinner_item,toLanguages);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);

        idBtnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    idTVTranslateTV.setText("");
                    if (sourceEdt.getText().toString().isEmpty()) {
                        Toast.makeText(Language_Translator.this, "Please Enter Your Text To Translate", Toast.LENGTH_SHORT).show();
                    } else if (fromLanguageCode == 0) {
                        Toast.makeText(Language_Translator.this, "Please Select Your Source Code", Toast.LENGTH_SHORT).show();
                    } else if (toLanguageCode == 0) {
                        Toast.makeText(Language_Translator.this, "Please Select The Languages To Make Translate", Toast.LENGTH_SHORT).show();
                    } else {
                        translateText(fromLanguageCode, toLanguageCode, sourceEdt.getText().toString());
                    }
            }
        });

        micIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak to convert into text");
                try{
                    startActivityForResult(intent,REQUEST_PERMISSION_CODE);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(Language_Translator.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_PERMISSION_CODE){
            if(resultCode == RESULT_OK && data!=null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                sourceEdt.setText(result.get(0));

            }
        }

    }

    private void translateText(int fromLanguageCode, int toLanguageCode, String source){
        idTVTranslateTV.setText("Downloading Modal..");
        FirebaseTranslatorOptions options =new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromLanguageCode)
                .setTargetLanguage(toLanguageCode)
                .build();

        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();

        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                idTVTranslateTV.setText("Translating..");
                translator.translate(source).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        idTVTranslateTV.setText(s);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Language_Translator.this, "Fail To Translate :"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Language_Translator.this, "Fail To Download language Model :"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getLanguageCode(String language){
        int languageCode = 0;
        switch (language){
            case "English":
                languageCode = FirebaseTranslateLanguage.EN;
                break;
            case "Hindi":
                languageCode = FirebaseTranslateLanguage.HI;
                break;
            case "Marathi":
                languageCode = FirebaseTranslateLanguage.MR;
                break;
            case "Gujarati":
                languageCode = FirebaseTranslateLanguage.GU;
                break;
            case "Tamil":
                languageCode = FirebaseTranslateLanguage.TA;
                break;
            case "Telugu":
                languageCode = FirebaseTranslateLanguage.TE;
                break;
            case "Kannada":
                languageCode = FirebaseTranslateLanguage.KN;
                break;
            case "Urdu":
                languageCode = FirebaseTranslateLanguage.UR;
                break;
            case "French":
                languageCode = FirebaseTranslateLanguage.FR;
                break;
            case "Afrikaans":
                languageCode = FirebaseTranslateLanguage.AF;
                break;
            case "Belarusian":
                languageCode = FirebaseTranslateLanguage.BE;
                break;
            case "Arabic":
                languageCode = FirebaseTranslateLanguage.AR;
                break;
            case "Welsh":
                languageCode = FirebaseTranslateLanguage.CY;
                break;
            case "Czech":
                languageCode = FirebaseTranslateLanguage.CS;
                break;
            case "Catalan":
                languageCode = FirebaseTranslateLanguage.CA;
                break;
            case "Bengali":
                languageCode = FirebaseTranslateLanguage.BN;
                break;
            case "Bulgarian":
                languageCode = FirebaseTranslateLanguage.BN;
                break;

            default:
                languageCode = 0;
        }
        return languageCode;
    }
}
