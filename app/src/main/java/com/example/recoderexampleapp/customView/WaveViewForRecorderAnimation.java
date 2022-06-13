package com.example.recoderexampleapp.customView;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.recoderexampleapp.R;

public class WaveViewForRecorderAnimation extends LinearLayout {

    LayoutParams layoutParams;
    WaveViewForRecorder waveView,waveBgView;
    HorizontalScrollView recordingScrollView,recordingBgScrollView;
    View lineView;

    public WaveViewForRecorderAnimation(Context context) {
        super(context);
        initView();
    }

    public WaveViewForRecorderAnimation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WaveViewForRecorderAnimation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public WaveViewForRecorderAnimation(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.wave_view_anim, null);
        view.setLayoutParams(layoutParams);
        waveView = view.findViewById(R.id.waveView);
        recordingScrollView = view.findViewById(R.id.recordingScrollView);
        waveBgView = view.findViewById(R.id.waveBgView);
        lineView = view.findViewById(R.id.lineView);

        recordingBgScrollView = view.findViewById(R.id.recordingBgScrollView);

        recordingScrollView.setOnTouchListener((view1, motionEvent) -> true);
        recordingBgScrollView.setOnTouchListener((view1, motionEvent) -> true);

        recordingScrollView.addOnLayoutChangeListener((view1, i, i1, i2, i3, i4, i5, i6, i7) -> {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            recordingScrollView.setLayoutParams(
                    new LayoutParams(width / 2, recordingScrollView.getHeight()));
        });
        waveView.setWaveViewListener(() -> recordingScrollView.fullScroll(View.FOCUS_RIGHT));
        waveBgView.setWaveViewListener(() -> recordingBgScrollView.fullScroll(View.FOCUS_RIGHT));
        this.addView(view);
    }

    public void addAmplitude(float amplitude) {
        waveView.addAmplitude(amplitude);
        waveBgView.addAmplitude(30);
    }

    public void reset(){
        waveView.clear();
        waveBgView.clear();
    }


    public void setColor(int color) {
        waveView.setColor(color);
    }

    public void setSubColor(int color) {
        waveBgView.setColor(color);
        lineView.setBackgroundColor(color);
    }

}
