package com.example.rachel.vetApp;

import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {
    String googlePlacesData;
    GoogleMap mMap;
    String url;
    private DatabaseReference mRef;
    private Task<Void> mDatabase;


    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyVetsData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            UrlConnection urlConnection = new UrlConnection();
            googlePlacesData = urlConnection.readUrl(url);
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
            Log.w("Id of place",id);

            Veterinarias vet = new Veterinarias(String.valueOf(lat),String.valueOf(lng),placeName,vicinity,telefono,id);
            //getUrl(vet,id);

            Log.w("Veterinaria blablabla", vet.toString());
            UpdateveterinariasList(vet,id);

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMap.addMarker(markerOptions);

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
    }

    private void UpdateveterinariasList(Veterinarias vet, String id) {
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vetapp-98f0d.firebaseio.com/");
        mDatabase = mRef.child("Veterinarias").child(id).setValue(vet);
    }

}