package com.example.recoderexampleapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.recoderexampleapp.R;
import com.example.recoderexampleapp.adapter.AudioAdapter;
import com.example.recoderexampleapp.models.MyAudio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ListRecordsActivity extends AppCompatActivity {

    RecyclerView rvListAudio;
    private ArrayList<MyAudio>  listAudio;
    AudioAdapter audioAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio);
        rvListAudio= findViewById(R.id.rvListAudio);

        rvListAudio.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        listAudio = new ArrayList<>();
        getListAudio();
    }

    private  void  getListAudio(){
        listAudio= new ArrayList<>();
        String path = getExternalCacheDir().getAbsolutePath();
        File directory = new File(path);
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            if(files[i].getName().contains(".3gp")){
                String pathStr= path+"/"+files[i].getName();
                Uri uri = Uri.parse(pathStr);
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(getApplicationContext(),uri);
                String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long millSecond = Long.parseLong(durationStr);
                listAudio.add(new MyAudio(
                        files[i].getName(),
                        pathStr,
                        files[i].lastModified(),
                        millSecond));
            }
        }

        audioAdapter= new AudioAdapter(listAudio, item -> {
//            startPlaying(pathName);
            Intent intent= new Intent(this,PlayRecordActivity.class);
            intent.putExtra("DATA",item);
            startActivity(intent);
        });
        rvListAudio.setAdapter(audioAdapter);
    }


}