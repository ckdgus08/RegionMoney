package com.tistory.ckdgus.regionmoney;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsReceiver";
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    jspDown down;
    jspConnect connect;

    GpsTracker gpsTracker = null;
    @Override
    public void onReceive(Context context, Intent intent) { // SMS_RECEIVED에 대한 액션일때 실행
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Log.d(TAG, "onReceiver() 호출"); // Bundle을 이용해서 메세지 내용을 가져옴
            Bundle bundle = intent.getExtras();
            SmsMessage[] messages = parseSmsMessage(bundle); // 메세지가 있을 경우 내용을 로그로 출력해 봄
            if (messages.length > 0) { // 메세지의 내용을 가져옴
                String sender = messages[0].getOriginatingAddress();
                String contents = messages[0].getMessageBody();
                Date receivedDate = new Date(messages[0].getTimestampMillis());
                // 로그를 찍어보는 과정이므로 생략해도 됨

                if(sender.equals("15447200")) {
                    gpsTracker = new GpsTracker(context);
                    double latitude = gpsTracker.getLatitude(); // 위도
                    double longitude = gpsTracker.getLongitude(); //경도

                    String str1 = String.valueOf(latitude);
                    String str2 = String.valueOf(longitude);

                    connect = new jspConnect();
                    down = new jspDown();

                    new Thread() {
                        @Override
                        public void run() {
                            connect.doInBackground("https://xn--bh3b03t.com/locationData.jsp?latitude=" + latitude + "&longitude=" + longitude + "&name=" + contents);
                        }
                    }.start();

                    Log.d(TAG, "latitude :" + latitude);
                    Log.d(TAG, "longitude : " + longitude);
                    Log.d(TAG, "Sender :" + sender);
                    Log.d(TAG, "contents : " + contents);
                    Log.d(TAG, "receivedDate : " + receivedDate);
                }
            }
        }
    }

    private SmsMessage[] parseSmsMessage(Bundle bundle) {
        Object[] objs = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[objs.length];
        for (int i = 0; i < objs.length; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String format = bundle.getString("format");
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i], format);
            } else {
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i]);
            }
        }
        return messages;
    }

}