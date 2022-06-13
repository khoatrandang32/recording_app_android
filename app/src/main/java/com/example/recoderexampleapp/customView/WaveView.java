package com.example.recoderexampleapp.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class WaveView extends View {

    public interface WaveViewListener{
        public void onSizeChange();
    }

    private static final int LINE_WIDTH = 10; // width of visualizer lines
    private static final int LINE_SCALE = 100; // scales visualizer lines
    private List<Float> amplitudes; // amplitudes for line lengths
    private Integer defaultWidth;
    private Integer width; // width of this View
    private Integer height; // height of this View
    private Paint linePaint; // specifies line drawing characteristics
    private WaveViewListener waveViewListener; // specifies line drawing characteristics

    boolean isAddable=true;

    public void setWaveViewListener(WaveViewListener waveViewListener) {
        this.waveViewListener = waveViewListener;
    }

    // constructor
    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs); // call superclass constructor
        initView();
    }

    public void initView(){
        linePaint = new Paint(); // create Paint for lines
        linePaint.setColor(Color.GREEN); // set color to green
        linePaint.setStrokeWidth(LINE_WIDTH); // set stroke width
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        amplitudes = new ArrayList<Float>();
    }

    public  void  setColor(int color){
        linePaint.setColor(color); // set color to green
    }

    // called when the dimensions of the View change
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if(width==null){
            defaultWidth= w;
            width = w; // new width of this View
            height = h; // new height of this View
            for(int x=0;x<(width) / (LINE_WIDTH/2);x++){
                amplitudes.add(30f);
            }
            //fill list to show default width
        }
        if(waveViewListener!=null){
            waveViewListener.onSizeChange();
        }
    }

    public void clear() {
        amplitudes.clear();
        amplitudes= new ArrayList<>();
        for(int x=0;x<(width) / (LINE_WIDTH/2);x++){
            amplitudes.add(30f);
        }
        invalidate();
    }

    public void setNewData(ArrayList newData) {
        amplitudes= newData;
        invalidate();
    }

    public void addAmplitude(float amplitude) {
        if(isAddable){
            amplitudes.add(amplitude); // add newest to the amplitudes ArrayList
            if (amplitudes.size() * LINE_WIDTH*2 >= width) {
                width=amplitudes.size() * LINE_WIDTH*2 - LINE_WIDTH;
                setLayoutParams(new LinearLayout.LayoutParams(width, height));
            }
        }
        else {
            if (amplitudes.size() * LINE_WIDTH*2 >= width) {
                width=amplitudes.size() * LINE_WIDTH*2;
                setLayoutParams(new LinearLayout.LayoutParams(width, height));
            }
        }
        isAddable=!isAddable;

    }

    @Override
    public void onDraw(Canvas canvas) {
        int middle = height / 2; // get the middle of the View
        float curX = 0; // start curX at zero
        for (float power : amplitudes) {
            float scaledHeight = power / LINE_SCALE; // scale the power
            canvas.drawLine(curX, middle + scaledHeight / 2, curX, middle
                    - scaledHeight / 2, linePaint);
            curX += LINE_WIDTH*2; // increase X by LINE_WIDTH
        }
    }
}
