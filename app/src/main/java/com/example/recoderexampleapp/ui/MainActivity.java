package com.example.recoderexampleapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.recoderexampleapp.R;
import com.example.recoderexampleapp.customView.WaveViewWrapper;
import com.example.recoderexampleapp.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    private MediaRecorder recorder = null;
    private Timer timer;
    private long time=0; // time of recording

    ImageButton btnRecord,btnStop;
    FloatingActionButton fab;
    TextView txtTimeCount;
    WaveViewWrapper waveView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initEvent() {
        //EVENT BINDING
        btnRecord.setOnClickListener(view -> {
            startRecording();
        });
        btnStop.setOnClickListener(view -> {
            stopRecording();
        });
        fab.setOnClickListener(view -> {
            startActivity(new Intent(this, ListRecordsActivity.class));
        });

        /// REQUEST  RECORD AUDIO PERMISSION
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
    }

    private void initView() {
        timer= new Timer();
        fab= findViewById(R.id.fab);
        btnStop= findViewById(R.id.btnStop);
        btnRecord= findViewById(R.id.btnRecord);
        txtTimeCount= findViewById(R.id.txtTimeCount);
        waveView= findViewById(R.id.waveView);
        waveView.setColor(getResources().getColor(R.color.purple_500));
        waveView.setSubColor(getResources().getColor(R.color.red_90));

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();
    }


    private void startRecording() {
        btnStop.setVisibility(View.VISIBLE);         //Show stop record button
        btnRecord.setVisibility(View.GONE);           // Hide start record button


        timer= new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    time+=25;
                    txtTimeCount.setText(Utils.timeToString(time));  // Show recording time

                    if(recorder!=null){
                        waveView.addAmplitude(recorder.getMaxAmplitude());
                        // add amplitude  to show wave of sound
                    }
                });
            }
        }, 25, 25); // run timer every 25ms

        long time= System.currentTimeMillis(); // use time as record Id

        String fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audio_"+time+".3gp"; // set up file name

        // SET UP RECORDER
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
        //start record
    }

    private void stopRecording() {
        btnStop.setVisibility(View.GONE);   //Hide stop record button
        btnRecord.setVisibility(View.VISIBLE); //Show stop record button


        time=0; // reset recording time
        txtTimeCount.setText(Utils.timeToString(time)); // reset recording time of textview
        recorder.stop();
        recorder.release();
        recorder = null; // stop and release recorder

        timer.cancel(); // cancel timer
        waveView.reset(); // reset wave view

    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            stopRecording();
        }
    }

}