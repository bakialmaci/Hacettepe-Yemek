package com.example.hacettepe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;



public class MainActivity extends AppCompatActivity {

    String result = "";
    String url = "http://104.248.251.131/foods";
    public static String example = "";
    public static int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new jsonTask().execute();
        setContentView(R.layout.main_activity);
    }


    class jsonTask extends AsyncTask<Void,Void,String> {

        @Override
        protected   void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public String doInBackground(Void... voids) {
            try {
                Log.e("Request","Request Send!");
                URL myurl = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection)myurl.openConnection();
                InputStreamReader streamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder builder = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null){
                    builder.append(line);
                }
                result = builder.toString();

            } catch (MalformedURLException e) {
                Log.e("MalformedURLException",e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("IOException",e.toString());
                e.printStackTrace();
            }
            example = result;

            ArrayList<String> arrayList = new ArrayList<>();
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(example);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for(int k = 0; k<= jsonArray.length(); k++){
                int dayPage = 0;
                JSONObject object = null;
                try {
                    object = jsonArray.getJSONObject(k);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    dayPage = Integer.parseInt(object.getString("date").substring(8,10));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Calendar c = Calendar.getInstance();
                int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                Log.e("darOfMonth", String.valueOf(dayOfMonth));
                if (dayOfMonth == dayPage){
                    Log.e("paired","Dates Paired");
                    Log.e("counter", String.valueOf(counter));
                    break;
                }else{
                    counter++;
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
                ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
                viewPager.setOffscreenPageLimit(counter);
                SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
                viewPager.setAdapter(swipeAdapter);
                viewPager.setCurrentItem(counter-1);
        }
    }

}