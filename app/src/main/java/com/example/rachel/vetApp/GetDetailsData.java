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
    private DatabaseReference mRef;
    private Task<Void> mDatabase;


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
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        Log.w("Resulr",result);
        nearbyPlacesList = dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList){
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute","Veterinarias");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");


            String telefono = googlePlace.get("formatted_phone_number");
            String id = googlePlace.get("place_id");

            Veterinarias vet = new Veterinarias(String.valueOf(lat),String.valueOf(lng),placeName,vicinity,telefono,id);
            //getUrl(vet,id);

            Log.w("Veterinaria blablabla", vet.toString());

            mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vetapp-98f0d.firebaseio.com/");
            mDatabase = mRef.child("Veterinarias").child(id).setValue(vet);
        }
    }


}
