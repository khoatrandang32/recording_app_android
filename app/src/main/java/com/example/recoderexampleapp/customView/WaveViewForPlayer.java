package com.example.recoderexampleapp.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.recoderexampleapp.R;

import java.util.ArrayList;
import java.util.List;

public class WaveViewForPlayer extends View {

    public interface WaveViewListener{
        public void onSizeChange();
    }

    private static final int LINE_WIDTH = 10; // width of visualizer lines
    private static final int LINE_SCALE = 100; // scales visualizer lines
    private List<Integer> amplitudes; // amplitudes for line lengths
    private Integer defaultWidth;
    private Integer width; // width of this View
    private Integer height; // height of this View
    private Paint linePaint; // specifies line drawing characteristics
    private WaveViewForRecorder.WaveViewListener waveViewListener; // specifies line drawing characteristics

    boolean isAddable=true;

    public void setWaveViewListener(WaveViewForRecorder.WaveViewListener waveViewListener) {
        this.waveViewListener = waveViewListener;
    }

    // constructor
    public WaveViewForPlayer(Context context, AttributeSet attrs) {
        super(context, attrs); // call superclass constructor
        initView();
    }

    public void initView(){
        linePaint = new Paint(); // create Paint for lines
        linePaint.setColor(Color.GREEN); // set color to green
        linePaint.setStrokeWidth(LINE_WIDTH); // set stroke width
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        amplitudes = new ArrayList();
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
        }
        if(waveViewListener!=null){
            waveViewListener.onSizeChange();
        }
    }

    public void setNewData(List newData) {
        amplitudes= newData;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        int middle = height / 2; // get the middle of the View
        int curX = 0; // start curX at zero
        for (int power : amplitudes) {
            Log.d("KHOA", "onDraw x: "+power);
            int scaledHeight = (power*100)+10;// scale the power
            canvas.drawLine(curX, middle + scaledHeight, curX, middle
                    - scaledHeight, linePaint);
            curX += LINE_WIDTH*2; // increase X by LINE_WIDTH
        }
    }

}
