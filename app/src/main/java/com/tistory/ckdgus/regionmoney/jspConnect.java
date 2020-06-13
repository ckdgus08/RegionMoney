package com.tistory.ckdgus.regionmoney;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import noman.googleplaces.PlaceType;

public class jspConnect extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... urls) {
        StringBuilder jsonHtml = new StringBuilder();
        try {
            // 연결 url 설정
            URL url = new URL(urls[0]);
            // 커넥션 객체 생성
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 연결되었으면.
            if (conn != null) {

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept-Charset", "UTF-8");
                conn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

                conn.setConnectTimeout(10000);
                conn.setUseCaches(false);

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    System.out.println("@@@@@@@@@@@@@@@@@@@@접속 성공");
                } else {
                    System.out.println("@@@@@@@@@@@@@@@@@@@@접속 실패");
                }
                // 연결되었음 코드가 리턴되면.
            conn.disconnect();
        }
    } catch(
    Exception ex)

    {
        ex.printStackTrace();
    }
        return jsonHtml.toString();

}

    protected void onPostExecute(String str) {
    }

}
