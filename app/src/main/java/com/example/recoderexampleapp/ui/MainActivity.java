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
import com.example.recoderexampleapp.customView.WaveViewForPlayer;
import com.example.recoderexampleapp.customView.WaveViewForRecorderAnimation;
import com.example.recoderexampleapp.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "KHOA";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    private MediaRecorder recorder = null;
    private Timer timer;
    private long time=0;

    ImageButton btnRecord,btnStop;
    FloatingActionButton fab;
    TextView txtTimeCount;
    WaveViewForRecorderAnimation waveView;
    WaveViewForPlayer waveViewForPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timer= new Timer();

        waveViewForPlayer= findViewById(R.id.waveViewForPlayer);
        fab= findViewById(R.id.fab);
        btnStop= findViewById(R.id.btnStop);
        btnRecord= findViewById(R.id.btnRecord);
        txtTimeCount= findViewById(R.id.txtTimeCount);
        waveView= findViewById(R.id.waveView);

        waveView.setColor(getResources().getColor(R.color.purple_500));
        waveView.setSubColor(getResources().getColor(R.color.red_90));

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        btnRecord.setOnClickListener(view -> {
            startRecording();
        });
        btnStop.setOnClickListener(view -> {
            stopRecording();
        });
        fab.setOnClickListener(view -> {
            startActivity(new Intent(this, ListRecordsActivity.class));
        });
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
        btnStop.setVisibility(View.VISIBLE);
        btnRecord.setVisibility(View.GONE);
        timer= new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    time+=25;
                    txtTimeCount.setText(Utils.timeToString(time));
                    if(recorder!=null){
                        waveView.addAmplitude(recorder.getMaxAmplitude());
                    }
                });
            }
        }, 25, 25);
        //
        long time= System.currentTimeMillis();
        String fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audio_"+time+".3gp";
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
    }

    private void stopRecording() {
        btnStop.setVisibility(View.GONE);
        btnRecord.setVisibility(View.VISIBLE);
        time=0;
        txtTimeCount.setText(Utils.timeToString(time));
        recorder.stop();
        recorder.release();
        recorder = null;
        timer.cancel();
        waveView.reset();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            stopRecording();
        }
    }

}