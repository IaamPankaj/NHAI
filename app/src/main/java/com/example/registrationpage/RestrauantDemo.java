package com.example.registrationpage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

public class RestrauantDemo extends AppCompatActivity {
    TextView tvFeedback;
    RatingBar tbStars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restrauant_demo);

        tvFeedback = findViewById(R.id.tvFeedback);
        tbStars = findViewById(R.id.tbStars);

        tbStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating == 0){
                    tvFeedback.setText("Very Unsatisfied");
                }
                else if(rating == 1){
                    tvFeedback.setText("Unsatisfied");
                }
                else if(rating == 1.5){
                    tvFeedback.setText("Unsatisfied");
                }

                else if(rating == 2 || rating ==3){
                    tvFeedback.setText("Good");
                }
                else if(rating == 3.5 ){
                    tvFeedback.setText("Satisfied");
                }

                else if(rating == 4 ){
                    tvFeedback.setText("Satisfied");
                }
                else if(rating == 4.5 ){
                    tvFeedback.setText("Very Satisfied");
                }

                else if(rating == 5 ){
                    tvFeedback.setText("Very Satisfied");
                }
                else {

                }
            }
        });
    }
}