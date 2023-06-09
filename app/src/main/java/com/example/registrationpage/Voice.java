package com.example.registrationpage;

import static android.content.Intent.ACTION_DIAL;
import static android.content.Intent.ACTION_MAIN;
import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.CATEGORY_APP_GALLERY;
import static android.content.Intent.CATEGORY_APP_MUSIC;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.annotations.NotNull;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import okio.BufferedSink;

public class Voice extends AppCompatActivity {

    private Button speakButton;
    private TextView speechTranscription;
    private TextToSpeech textToSpeech;

    private OkHttpClient httpClient;
    private HttpUrl.Builder httpBuilder;
    private Request.Builder httpRequestBuilder;

    private AudioRecord recorder;
    private static final int SAMPLE_RATE = 8000;
    private static final int CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL, AUDIO_FORMAT) * 10;
    private static final AtomicBoolean recordingInProgress = new AtomicBoolean(false);
    private Thread recordingThread;
    private static final String TAG = "Voice";
    /* Go to your Wit.ai app Management > Settings and obtain the Client Access Token */
    private static final String CLIENT_ACCESS_TOKEN = "KRKWDBKVMZIQZ5WB275MFDENPQAJQAKP";
    ImageView micImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);

        if (!checkPermissionsFromDevice()) requestPermissions();

        // Get a reference to the TextView and Button from the UI
        speechTranscription = findViewById(R.id.speechTranscription);
        speakButton = findViewById(R.id.speakButton);
        micImage = findViewById(R.id.img_mic);

        // Initialize TextToSpeech
        initializeTextToSpeech(this.getApplicationContext());

        // Initialize HTTP Client
        initializeHttpClient();

        // Wire up speakButton to an onClickListener
        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!recordingInProgress.get()) {
                    startRecording();
                    speakButton.setText("Listening ...");
                    micImage.setImageResource(R.drawable.ic_mic_none);
                    Log.d("speakButton", "Start listening ...");
                } else {
                    stopRecording();
                    speakButton.setText("Speak");
                    micImage.setImageResource(R.drawable.ic_mic_none);
                    Log.d("speakButton", "Stop listening ...");
                }
            }
        });

        micImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recordingInProgress.get()) {
                    startRecording();
                    speakButton.setText("Listening ...");
                    micImage.setImageResource(R.drawable.ic_mic_none);
                    Log.d("speakButton", "Start listening ...");
                } else {
                    stopRecording();
                    speakButton.setText("Speak");
                    micImage.setImageResource(R.drawable.ic_mic_none);
                    Log.d("speakButton", "Stop listening ...");
                }
            }
        });
    }

    // Processes the response from Speech API and responds to the user appropriately
    // See here for shape of the response: https://wit.ai/docs/http#get__message_link
    private void respondToUser(String response) {
        Log.v("respondToUser", response);
        String intentName = "";
        String speakerName = null;
        String responseText = "";

        try {
            // Parse the intent name from the Wit.ai response
            JSONObject data = new JSONObject(response);

            // Update the TextView with the voice transcription
            // Run it on the MainActivity's UI thread since it's the owner
            final String utterance = data.getString("text");
            Voice.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    speechTranscription.setText(utterance);
                }
            });
            if(utterance.toLowerCase().trim().contains("whatsapp") && utterance.toLowerCase().trim().contains("open")){
                intentName = "open_whatsapp";
            }else if(utterance.toLowerCase().trim().contains("map") && utterance.toLowerCase().trim().contains("open")){
                intentName = "open_map";
            }else if(utterance.toLowerCase().trim().contains("Play_a_song"))  {
                intentName = "Play_a_song";
            }else if(utterance.toLowerCase().trim().contains("watch?v=e_YLWSNMfZg")){
                intentName = "open_youtube";
            }else if(utterance.toLowerCase().trim().contains("com/i/flow/login")) {
                intentName = "open_twitter";
            }
            //Temp
            switch (intentName) {
                case "Open_camera_app":
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String cameraPackage = intent.resolveActivity(getPackageManager()).getPackageName();
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage(cameraPackage);
                    startActivity(launchIntent);
                    break;
                case "Play_a_song":
                    Intent intent1 = Intent.makeMainSelectorActivity(ACTION_MAIN, CATEGORY_APP_MUSIC);
//                    Intent intent1 = Intent.makeMainSelectorActivity("com.song" );
                    startActivity(intent1);
                    break;
                case "make_a_call":
                case "open_phone_app":
                    Intent intent2 = new Intent(ACTION_DIAL);
                    startActivity(intent2);
                    break;
                case "open_messaging_app":
                    Intent intent3 = new Intent(ACTION_VIEW);
                    intent3.setData(Uri.parse("sms:"));
                    startActivity(intent3);
                    break;
                case "open_photos_app":
                    Intent intent4 = Intent.makeMainSelectorActivity(ACTION_MAIN, CATEGORY_APP_GALLERY);
                    startActivity(intent4);
                    break;
                case "open_whatsapp":
                    Intent launchIntent1 = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                    startActivity(launchIntent1);
                    break;
                case "open_Twitter":
                    Intent launchIntent9 = getPackageManager().getLaunchIntentForPackage("https://twitter.com/i/flow/login");
                    startActivity(launchIntent9);
                    break;
                case "open_map":
                    Intent launchIntent7 = getPackageManager().getLaunchIntentForPackage("com.google.android.apps.maps");
                    startActivity(launchIntent7);
                    break;
                case "open_youtube":
                    Intent launchIntent2 = getPackageManager().getLaunchIntentForPackage("com.youtube.watch?v=e_YLWSNMfZg");
                    startActivity(launchIntent2);
                    break;
                case "open_facebook":
                    Intent launchIntent3 = getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
                    try {
                        startActivity(launchIntent3);
                    } catch (Exception e) {
                        Log.e(TAG, "respondToUser: Error opening facebook: ", e);
                    }
                    break;
                case "open_instagram":
                    Intent launchIntent4 = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
                    try {
                        startActivity(launchIntent4);
                    } catch (Exception e) {
                        Log.e(TAG, "respondToUser: Error opening Instagram: ", e);
                    }
                    break;
                case "open_notes_app":
                    Intent launchIntent5 = getPackageManager().getLaunchIntentForPackage("com.google.android.keep");
                    try {
                        startActivity(launchIntent5);
                    } catch (Exception e) {
                        Log.e(TAG, "respondToUser: Error opening Notes: ", e);
                    }
                    break;
                case "tell_app_name":
                    textToSpeech.speak("मी तुमची सहायक आहे", TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString());
                    break;
                default:
                    textToSpeech.speak("माफ करा, मला कळले नाही! मी तुमच्या साठी काय करू शकते?", TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString());
                break;
            }
            //Temp close
            // Get most confident intent
            JSONObject intent = getMostConfident(data.getJSONArray("intents"));
            if (intent == null) {
                //textToSpeech.speak("माफ करा, मला कळले नाही! मी तुमच्या साठी काय करू शकते?", TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString());
                return;
            }
            intentName = intent.getString("name");
            Log.v("respondToUser", intentName);

            // Parse and get the most confident entity value for the name
//            JSONObject nameEntity = getMostConfident((data.getJSONObject("entities")).getJSONArray("wit$contact:contact"));

//            speakerName = (String) nameEntity.get("value");
//            Log.v("respondToUser", speakerName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Handle intents
        switch (intentName) {
            case "Open_camera_app":
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String cameraPackage = intent.resolveActivity(getPackageManager()).getPackageName();
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(cameraPackage);
                startActivity(launchIntent);
                break;
            case "Play_a_song":
                Intent intent1 = Intent.makeMainSelectorActivity(ACTION_MAIN, CATEGORY_APP_MUSIC);
                startActivity(intent1);
                break;
            case "make_a_call":
            case "open_phone_app":
                Intent intent2 = new Intent(ACTION_DIAL);
                startActivity(intent2);
                break;
            case "open_messaging_app":
                Intent intent3 = new Intent(ACTION_VIEW);
                intent3.setData(Uri.parse("sms:"));
                startActivity(intent3);
                break;
            case "open_photos_app":
                Intent intent4 = Intent.makeMainSelectorActivity(ACTION_MAIN, CATEGORY_APP_GALLERY);
                startActivity(intent4);
                break;
            case "open_whatsapp":
                Intent launchIntent1 = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                startActivity(launchIntent1);
                break;
            case "open_youtube":
                Intent launchIntent2 = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
                startActivity(launchIntent2);
                break;
            case "open_facebook":
                Intent launchIntent3 = getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
                try {
                    startActivity(launchIntent3);
                } catch (Exception e) {
                    Log.e(TAG, "respondToUser: Error opening facebook: ", e);
                }
                break;
            case "open_instagram":
                Intent launchIntent4 = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
                try {
                    startActivity(launchIntent4);
                } catch (Exception e) {
                    Log.e(TAG, "respondToUser: Error opening Instagram: ", e);
                }
                break;
            case "open_notes_app":
                Intent launchIntent5 = getPackageManager().getLaunchIntentForPackage("com.google.android.keep");
                try {
                    startActivity(launchIntent5);
                } catch (Exception e) {
                    Log.e(TAG, "respondToUser: Error opening Notes: ", e);
                }
                break;
            case "tell_app_name":
                textToSpeech.speak("मी तुमची सहायक आहे", TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString());
                break;
        }
    }

    // Get the resolved intent or entity with the highest confidence from Wit Speech API
    // https://wit.ai/docs/recipes#which-confidence-threshold-should-you-use
    private JSONObject getMostConfident(JSONArray list) {
        JSONObject confidentObject = null;
        double maxConfidence = 0.0;
        for (int i = 0; i < list.length(); i++) {
            try {
                JSONObject object = list.getJSONObject(i);
                double currConfidence = object.getDouble("confidence");
                if (currConfidence > maxConfidence) {
                    maxConfidence = currConfidence;
                    confidentObject = object;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return confidentObject;
    }

    // Instantiate a new AudioRecord and start streaming the recording to the Wit Speech API
    private void startRecording() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL, AUDIO_FORMAT, BUFFER_SIZE);
        recorder.startRecording();
        recordingInProgress.set(true);
        recordingThread = new Thread(new StreamRecordingRunnable(), "Stream Recording Thread");
        recordingThread.start();
    }

    // Release resources for the AudioRecord and Runnable when recording is stopped
    private void stopRecording() {
        if (recorder == null) return;
        recordingInProgress.set(false);
        recorder.stop();
        recorder.release();
        recorder = null;
        recordingThread = null;
    }

    // Define a Runnable to stream the recording data to the Speech API
    // https://wit.ai/docs/http#post__speech_link
    private class StreamRecordingRunnable implements Runnable {
        @Override
        public void run() {
            final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
            RequestBody requestBody = new RequestBody() {
                @Override
                public MediaType contentType() {
                    return MediaType.parse("audio/raw;encoding=signed-integer;bits=16;rate=8000;endian=little");
                }

                @Override
                public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
                    while (recordingInProgress.get()) {
                        int result = recorder.read(buffer, BUFFER_SIZE);
                        if (result < 0) {
                            throw new RuntimeException("Reading of audio buffer failed: " +
                                    getBufferReadFailureReason(result));
                        }
                        bufferedSink.write(buffer);
                        buffer.clear();
                    }
                }
            };

            // Start streaming audio to Wit.ai Speech API
            Request request = httpRequestBuilder.post(requestBody).build();
            try{
                Response response = httpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    respondToUser(responseData);
                    Log.v("Streaming Response", responseData);
                }
            } catch (IOException e) {
                Log.e("Streaming Response", e.getMessage());
            }
        }

        private String getBufferReadFailureReason(int errorCode) {
            switch (errorCode) {
                case AudioRecord.ERROR_INVALID_OPERATION:
                    return "ERROR_INVALID_OPERATION";
                case AudioRecord.ERROR_BAD_VALUE:
                    return "ERROR_BAD_VALUE";
                case AudioRecord.ERROR_DEAD_OBJECT:
                    return "ERROR_DEAD_OBJECT";
                case AudioRecord.ERROR:
                    return "ERROR";
                default:
                    return "Unknown (" + errorCode + ")";
            }
        }
    }

    // Instantiate a Request.Builder that can be used for all the streaming requests
    // https://square.github.io/okhttp/recipes/#post-streaming-kt-java
    // https://wit.ai/docs/http#post__speech_link
    private void initializeHttpClient() {
        httpClient = new OkHttpClient();
        httpBuilder = HttpUrl.parse("https://api.wit.ai/speech").newBuilder();
        httpBuilder.addQueryParameter("v", "20200805");
        httpRequestBuilder = new Request.Builder()
                .url(httpBuilder.build())
                .header("Authorization", "Bearer "+ CLIENT_ACCESS_TOKEN )
                .header("Content-Type", "audio/raw")
                .header("Transfer-Encoding", "chunked");
    }

    // Initialize the Android TextToSpeech
    // https://developer.android.com/reference/android/speech/tts/TextToSpeech
    private void initializeTextToSpeech(Context applicationContext) {
        textToSpeech = new TextToSpeech(applicationContext,new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int ttsStatus) {
                // Disable the speakButton and provide the status of app while waiting for TextToSpeech to initialize
                speechTranscription.setHint("Loading app ...");
                speakButton.setEnabled(false);

                // Check the status of the initialization
                if (ttsStatus == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(new Locale("mar","IN","mr"));
//                    textToSpeech.speak("Hi! Welcome to the Wit a.i. voice demo. My name is Wit. What is your name?", TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString());
                    textToSpeech.speak("नमस्कार मी आपली सहायक आहे. मी तुमच्या साठी काय करू शकते?",TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString());
                    speechTranscription.setHint("Speak बटण दाबा आणि बोला!");
                    speakButton.setEnabled(true);
                } else {
                    String message = "TextToSpeech initialization failed";
                    speechTranscription.setTextColor(Color.RED);
                    speechTranscription.setText(message);
                    Log.e("TextToSpeech", message);
                }
            }
        });
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.RECORD_AUDIO
        }, 1000);
    }

    private boolean checkPermissionsFromDevice() {
        int recordAudioResult = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int internetResult = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);

        return recordAudioResult == PackageManager.PERMISSION_GRANTED
                && internetResult == PackageManager.PERMISSION_GRANTED;
    }
}

