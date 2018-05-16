package com.example.rachel.vetApp;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class GetDetailsData  extends AsyncTask<Object, String, String> {
    String googlePlacesData;
    String url;

    @Override
    protected String doInBackground(Object... objects) {
        try {
            Log.d("GetVetsDatailData", "doInBackground entered");
            url = (String) objects[0];
            UrlConnection urlConnection = new UrlConnection();
            googlePlacesData = urlConnection.readUrl(url);
            Log.w("Do in baackground",googlePlacesData.toString());
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result){
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        DetailsPlacesParser dataParser = new DetailsPlacesParser();
        Log.w("Resulr",result);
        dataParser.parse(result);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }



}
