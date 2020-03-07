package com.example.hacettepe;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SwipeAdapter extends FragmentStatePagerAdapter {


    public SwipeAdapter(FragmentManager fm){
        super(fm);

    }


    @Override
    public Fragment getItem(int position) {
        Fragment pageFragment = new FragmentPage();
        Bundle bundle = new Bundle();
        bundle.putInt("pageNumber",position+1);
        pageFragment.setArguments(bundle);
        return pageFragment;
    }


    @Override
    public int getCount() {
        return 15;
    }
}
