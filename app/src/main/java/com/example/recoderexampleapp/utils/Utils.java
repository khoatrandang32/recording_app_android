package com.example.recoderexampleapp.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
    public static final String timeToString(long milliseconds){
        long minutes = milliseconds / 1000 / 60;
        long seconds = milliseconds / 1000 % 60;

        String secondsStr = seconds+"";
        String secs = secondsStr.length() >= 2?secondsStr.substring(0, 2):"0"+secondsStr;
        String minStr = minutes < 10? "0"+minutes : minutes+"";
        String secsStr = secs.length() < 2?"0"+secs : secs+"";
        return minStr+":"+secsStr;
    }

    public static final String getDate(long milliSeconds, String dateFormat)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static byte[] fileToBytes(String filepath) {
        File file = new File(filepath);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
