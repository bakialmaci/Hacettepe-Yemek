package com.example.hacettepe;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.TypedArrayUtils;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class FragmentPage extends Fragment {
    public  static String photoURL = "";
    String result = "";

    TextView textView;
    TextView dateText;
    TextView dateDayText;
    TextView calText;
    TextView calText2;
    ListView listView;
    boolean yemek = true;
    boolean kahvalti = false;
    public  static boolean flag = false;
    android.widget.ImageView ImageView;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view;
        Bundle bundle = getArguments();
        assert bundle != null;
        int pageNumber = bundle.getInt("pageNumber");
        view = inflater.inflate(R.layout.page_fragment_layout,container,false);
        TextView textView = view.findViewById(R.id.day);
//        textView.setText(Integer.toString(pageNumber));


        textView = view.findViewById(R.id.text);
        dateText = view.findViewById(R.id.date);
        dateDayText = view.findViewById(R.id.day);
        calText = view.findViewById(R.id.calories_text);
        calText2 = view.findViewById(R.id.kalori);
        listView = view.findViewById(R.id.listview);
        final FrameLayout fl_saats = view.findViewById(R.id.saats);

        ImageView logo = view.findViewById(R.id.logo);
        final ImageView foodimage = view.findViewById(R.id.food_image);
        logo.setImageResource(R.drawable.logo2);

        final ImageView calImg = view.findViewById(R.id.calories);
        final ProgressBar loading = view.findViewById(R.id.progressbar);



        final Button button_yemek = view.findViewById(R.id.button1);
        final Button button_kahvalti = view.findViewById(R.id.button2);
        final Button button_saatler = view.findViewById(R.id.button3);
        final Button closebutton = view.findViewById(R.id.close_button);

        button_kahvalti.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                foodimage.setVisibility(View.INVISIBLE);
                closebutton.setVisibility(View.INVISIBLE);
                dateDayText.setVisibility(View.VISIBLE);
                dateText.setVisibility(View.VISIBLE);
                calText.setVisibility(View.INVISIBLE);
                calImg.setVisibility(View.INVISIBLE);
                calText2.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
                fl_saats.setVisibility(View.INVISIBLE);
                kahvalti = true;
                yemek    = false;
//                Log.e("KAHVALTI","KAHVALTI");
                new FragmentPage.jsonTask().execute();
            }
        });

        button_yemek.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                closebutton.setVisibility(View.INVISIBLE);

                foodimage.setVisibility(View.INVISIBLE);
//                button_yemek.setTextColor();
                dateDayText.setVisibility(View.VISIBLE);
                dateText.setVisibility(View.VISIBLE);
                calText.setVisibility(View.VISIBLE);
                calImg.setVisibility(View.VISIBLE);
                calText2.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);
                fl_saats.setVisibility(View.INVISIBLE);
                kahvalti = false;
                yemek    = true;
//                Log.e("YEMEK","YEMEK");
                new FragmentPage.jsonTask().execute();
            }
        });

        button_saatler.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                closebutton.setVisibility(View.INVISIBLE);

                foodimage.setVisibility(View.INVISIBLE);
                dateDayText.setVisibility(View.INVISIBLE);
                dateText.setVisibility(View.INVISIBLE);
                calText.setVisibility(View.INVISIBLE);
                calImg.setVisibility(View.INVISIBLE);
                calText2.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.INVISIBLE);
                fl_saats.setVisibility(View.VISIBLE);
                kahvalti = false;
                yemek    = true;
//                Log.e("YEMEK","YEMEK");
                new FragmentPage.jsonTask().execute();
            }
        });

        closebutton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ImageView img= view.findViewById(R.id.food_image);
                Button close_button = view.findViewById(R.id.close_button);
                img.setVisibility(View.INVISIBLE);
                close_button.setVisibility(View.INVISIBLE);

//                listView.setVisibility(View.VISIBLE);
//                dateDayText.setVisibility(View.VISIBLE);
//                dateText.setVisibility(View.VISIBLE);
//                button_kahvalti.setVisibility(View.VISIBLE);
//                button_yemek.setVisibility(View.VISIBLE);
//                calText.setVisibility(View.VISIBLE);
//                calImg.setVisibility(View.VISIBLE);
//                calText2.setVisibility(View.VISIBLE);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                loading.setVisibility(View.VISIBLE);
                foodimage.setVisibility(View.INVISIBLE);
                closebutton.setVisibility(View.INVISIBLE);
//                listView.setVisibility(View.INVISIBLE);
//                dateDayText.setVisibility(View.INVISIBLE);
//                dateText.setVisibility(View.INVISIBLE);
//                button_kahvalti.setVisibility(View.INVISIBLE);
//                button_yemek.setVisibility(View.INVISIBLE);
//                calText.setVisibility(View.INVISIBLE);
//                calImg.setVisibility(View.INVISIBLE);
//                calText2.setVisibility(View.INVISIBLE);



                JSONArray jsonArray = null;
                try {

                    jsonArray = new JSONArray(MainActivity.foodStorage);
                    int get_page_date = Integer.parseInt(String.valueOf(dateText.getText()).substring(0,2));
                    Log.e("dateText", String.valueOf(get_page_date));
                    JSONObject obje = null;
                    obje = jsonArray.getJSONObject(0);
                    int intFirstDate = Integer.parseInt(obje.getString("date").substring(8,10));
                    Log.e("dateTextStorage", String.valueOf(intFirstDate));
//                    Log.e("example taken",MainActivity.example);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                photoURL = "http://104.248.251.131/"+"example.jpeg";


                    JSONObject object = null;
                    try {
                        int calculate = Integer.parseInt(String.valueOf(dateText.getText()).substring(0,2)) - Integer.parseInt(jsonArray.getJSONObject(0).getString("date").substring(8,10));
                        object = jsonArray.getJSONObject(calculate);
//                        Log.e("objects", String.valueOf(object));
                        JSONArray jsonFoods  = object.getJSONArray("foods");
                        for (int f = 0; f < jsonFoods.length(); f++){

                            try {

                                JSONObject foodObject = new JSONObject(jsonFoods.getString(f));


                                if(foodObject.getString("name").equals(String.valueOf((listView.getItemAtPosition(position)))) && yemek && !foodObject.getString("photo_url").equals("example.jpeg")){
//                                    Log.e("FOUND!!!","FOUND");
                                    if(foodObject.getString("photo_url").equals("example.jpeg")){
                                        Log.e("NOT FOund","not found!!!");
                                        photoURL = "http://104.248.251.131/"+"example.jpeg";
                                    }else{
                                        photoURL = "http://104.248.251.131/"+foodObject.getString("photo_url");
//                                    Log.e("FOUND!!!","FOUND");
                                        Log.e("FOUND!!!",foodObject.getString("photo_url"));
                                    }


                                    flag = true;
                                    break;
                                }else{

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        if(!flag){
                            loading.setVisibility(View.INVISIBLE);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                flag = false;
//                Log.e("photoURL",photoURL);
                String food_url = photoURL;
                Picasso.with(getActivity()).load(food_url).into(foodimage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        if(yemek){
                            loading.setVisibility(View.INVISIBLE);
                            foodimage.setVisibility(View.VISIBLE);
                            closebutton.setVisibility(View.VISIBLE);
                            String selectedFromList = String.valueOf((listView.getItemAtPosition(position)));
                        }

                    }

                    @Override
                    public void onError() {

                    }

                });

            }
        });

        ImageView calories = view.findViewById(R.id.calories);
        calories.setImageResource(R.drawable.cal2);


        new FragmentPage.jsonTask().execute();
        return view;
    }


    @SuppressLint("StaticFieldLeak")
    class jsonTask extends AsyncTask<Void,Void,String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            if(MainActivity.foodStorage != null){
//                Log.e("Storage","YES");
                result = MainActivity.foodStorage;
            }

            return result;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                String[] months = {
                        "",
                        "Ocak",
                        "Şubat",
                        "Mart",
                        "Nisan",
                        "Mayıs",
                        "Haziran",
                        "Temmuz",
                        "Ağustos",
                        "Eylül",
                        "Ekim",
                        "Kasım",
                        "Aralık",
                };


                Bundle bundle = getArguments();
                assert bundle != null;
                ArrayList<String> arrayList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(s);
                int pagee = Integer.parseInt(Integer.toString(bundle.getInt("pageNumber")));
//                Log.e("onPostExecute", String.valueOf(pagee));
                JSONObject object = jsonArray.getJSONObject(pagee);
//                dateText.setText(object.getString("date").substring(0,10));

                String dayPage = object.getString("date").substring(8,10);
                int get_month_name = Integer.parseInt(object.getString("date").substring(5,7));

                dateText.setText(dayPage+" "+months[get_month_name]);


                calText.setText(object.getString("cal"));
                String[][] menu = {{
                        "Çay, Soslu Patates Kızartması",
                        "Haşlanmış Yumurta Beyaz Peynir",
                        "Siyah Zeytin, Tahin Pekmez",
                        "Domates-Biber, Ekmek(50 gr)"
                },{
                        "Çay, Peynirli Rulo Börek",
                        "Haşlanmış Yumurta, Tereyağ-Bal",
                        "Poşet Beyaz Peynir, Yeşil Zeytin",
                        "Domates-Salatalık, Ekmek (50 gr)"
                },{
                        "Çay ,Kek, Haşlanmış Yumurta",
                        "Üçgen Peynir, Siyah Zeytin",
                        "Tereyağ-Reçel, Domates-Biber",
                        "Ekmek (50 gr)"
                },{
                        "Çay, Simit, Haşlanmış Yumurta",
                        "Kaşar Peynir, Yeşil Zeytin",
                        "Fındık Ezmesi, Domates-Salatalık",
                        "Ekmek (50 gr)"
                },{
                        "Çay, Patatesli Rulo Börek",
                        "Peynirli Yumurta,Beyaz Peynir",
                        "Tereyağ-Bal, Domates-Biber",
                        "Ekmek (50 gr)"

                },{
                        "Çay, Sade Poğaça, Haşlanmış Yumurta",
                        "Üçgen Peynir, Siyah Zeytin",
                        "Tereyağ-Reçel, Domates-Salatalık",
                        "Ekmek ( 50 gr)"
                },{
                        "Çay, Simit, Haşlanmış Yumurta",
                        "Kaşar Peynir, Yeşil Zeytin",
                        "Tahin Pekmez, Domates-Biber",
                        "Ekmek (50 gr)"
                }};

                String[] daysOfWeek = {
                        "Pazartesi",
                        "Salı",
                        "Çarşamba",
                        "Perşembe",
                        "Cuma",
                        "Cumartesi",
                        "Pazar",
                };

                int kahvaltiIndex = 0;

                Calendar c = Calendar.getInstance();
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                int dayPageInt = Integer.parseInt(dayPage);
                int whichDay = (dayPageInt+5)%7;
                dateDayText.setText(daysOfWeek[whichDay]);

                if (yemek){
                    JSONArray jsonFoods  = object.getJSONArray("foods");
                    for(int food = 0; food < jsonFoods.length(); food++){
                        JSONObject foodObject = new JSONObject(jsonFoods.getString(food));
                        String foodName = (foodObject.getString("name"));
                        arrayList.add(foodName);
                    }
                    arrayList.add("*"+object.getString("veg"));
                }else{
                    for(int index = 0; index < 7; index++){
                        if(daysOfWeek[index] == daysOfWeek[whichDay]){
                            kahvaltiIndex = index;
//                                Log.e("takeIndexOfMenu", String.valueOf(kahvaltiIndex));
                        }
                    }
                    for(int food = 0; food < menu[kahvaltiIndex].length; food++) {
                        arrayList.add(menu[kahvaltiIndex][food]);
                    }

                }


                ArrayAdapter arrayAdapter  = new ArrayAdapter(getActivity(),R.layout.textcenter,R.id.food_name,arrayList);
                listView.setAdapter(arrayAdapter);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }


}
