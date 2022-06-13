package com.example.recoderexampleapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.recoderexampleapp.R;
import com.example.recoderexampleapp.models.MyAudio;
import com.example.recoderexampleapp.utils.Utils;
import com.masoudss.lib.SeekBarOnProgressChanged;
import com.masoudss.lib.WaveformSeekBar;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PlayRecordActivity extends AppCompatActivity {
    private static final String LOG_TAG = "PlayRecordActivity";

    MyAudio myAudio;
    TextView txtAudioName, txtPos, txtDuration;
    private MediaPlayer player = null;
    WaveformSeekBar waveView;
    ImageButton btnPlay, btnStop;

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_record);

        getPreData();
        initView();
        initWaveBar();

        player = new MediaPlayer();

        try {
            player.setDataSource(myAudio.getPath());
//            waveView.updateVisualizer(Utils.fileToBytes(fileName));
            player.prepare();

        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed " + e.getMessage());
        }

        //
        txtAudioName.setText(myAudio.getName());

        btnPlay.setOnClickListener(view -> {
            startPlaying();
        });
        btnStop.setOnClickListener(view -> {
            stopPlaying();
        });
        txtDuration.setText(Utils.timeToString(player.getDuration()));
    }

    private void initView() {
        txtPos = findViewById(R.id.txtPos);
        txtDuration = findViewById(R.id.txtDuration);
        txtAudioName = findViewById(R.id.txtAudioName);
        btnPlay = findViewById(R.id.btnPlay);
        btnStop = findViewById(R.id.btnStop);
        waveView = findViewById(R.id.waveView);
    }

    private void getPreData() {
        Intent intent = getIntent();
        myAudio = (MyAudio) intent.getSerializableExtra("DATA");
    }

    private void initWaveBar() {
        waveView.setSampleFrom(new File(myAudio.getPath()));
        waveView.setOnProgressChanged(new SeekBarOnProgressChanged() {
            @Override
            public void onProgressChanged(WaveformSeekBar waveformSeekBar, float v, boolean b) {
                Log.d(LOG_TAG, "onProgressChanged: " + v);
                runOnUiThread(() -> txtPos.setText(Utils.timeToString(player.getCurrentPosition())));
                if (b) {
                    float per = (v / 100) * player.getDuration();
                    player.seekTo(Math.round(per));
                }

            }
        });
    }

    private void startPlaying() {
        timer = new Timer();
        btnPlay.setVisibility(View.GONE);
        btnStop.setVisibility(View.VISIBLE);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                waveView.setProgress(0);
                btnPlay.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.GONE);
            }
        });

        player.start();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (player != null && player.isPlaying()) {
                    float per = (((float) player.getCurrentPosition()) / ((float) player.getDuration())) * 100f;
                    waveView.setProgress(per);

                }
            }

        }, 0, 50);
    }

    private void stopPlaying() {
        btnPlay.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.GONE);
        player.pause();
        timer.cancel();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }

}