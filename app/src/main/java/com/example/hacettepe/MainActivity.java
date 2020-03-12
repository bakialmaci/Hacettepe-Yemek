package com.example.hacettepe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;



public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "foods.txt";
    String result = "";
    String url = "http://104.248.251.131/foods";
    public static String example = "";
    public static int counter = 0;
    public static String foodStorage = "no";
    public  static boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new jsonTask().execute();
        setContentView(R.layout.main_activity);
    }

    public void save(String data) {

        FileOutputStream fos = null;

        try {
            fos = openFileOutput(FILE_NAME,MODE_PRIVATE);
            fos.write(data.getBytes());
//            Log.e("xd","xd");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public String load() {
        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
//                    Log.e("append",text);
                sb.append(text).append("\n");
            }
            foodStorage = sb.toString();
//                ja = sb.
//            Log.e("sb.toString22", sb.toString());
            return sb.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "no";
    }




    class jsonTask extends AsyncTask<Void,Void,String> {

        @Override
        protected   void onPreExecute() {
            super.onPreExecute();

            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                //we are connected to a network
//                Log.e("conn","conn");
                connected = true;
            }
            else
                connected = false;

            if(!connected && !foodStorage.equals("no")){
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("İnternet Bağlantın Bulunamadı")
                        .setMessage("Fakat yine de yemek listesini sorunsuz bir şekilde görebilirsin.")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }

//            if(!connected && foodStorage.equals("no")){
//                new AlertDialog.Builder(MainActivity.this)
//                        .setTitle("Yemek Verisi Çekilemedi")
//                        .setMessage("Uygulamanın yemek verisini yüklemesi ya da güncellemesi gerek. Bunu için internete bağlantınız olması gerek.")
//                        .setPositiveButton(android.R.string.ok, null)
//                        .show();
//            }



        }

        @SuppressLint("WrongThread")
        @Override
        public String doInBackground(Void... voids) {


            Calendar c = Calendar.getInstance();
            int dayOfMonth2 = c.get(Calendar.DAY_OF_MONTH);

            File file = new File(String.valueOf(getFileStreamPath(FILE_NAME)));
            Date lastModDate = new Date(file.lastModified());
            int lastModDateInt = Integer.parseInt(lastModDate.toString().substring(8,10));


            load();
//            Log.e("loadeq",foodStorage);
            if ((foodStorage == "no" || connected) && (dayOfMonth2 - lastModDateInt) >= 1){
                try {
//                    Log.e("Request12","Request Send!");
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
//                example = result;
                save(result);
                foodStorage = result;
//                Log.e("loadeq",foodStorage);
            }else{
//                Log.e("foodStorage",foodStorage);
                Log.e("HTTP","http passed");
            }

            load();


            ArrayList<String> arrayList = new ArrayList<>();
            JSONArray jsonArray = null;
//            Log.e("WTF",load());
            try {
                jsonArray = new JSONArray(load());
//                Log.e("sasa", String.valueOf(jsonArray));
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
//                Calendar c = Calendar.getInstance();
                int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
//                Log.e("darOfMonth", String.valueOf(dayOfMonth));
                if (dayOfMonth == dayPage){
//                    Log.e("paired","Dates Paired");
//                    Log.e("counter", String.valueOf(counter));
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