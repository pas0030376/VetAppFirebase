package com.example.rachel.vetApp;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class DetailsPlacesParser {
    private DatabaseReference mRef;
    private Task<Void> mDatabase;

    public void parse(String jsonData) {
        JSONObject jsonObject = null;
        try {
            Log.d("Places", "parse");
            jsonObject = new JSONObject((String) jsonData);
        } catch (JSONException e) {
            Log.d("Places", "parse error");
            e.printStackTrace();
        }
        getPlace(jsonObject);
    }

    private void getPlace(JSONObject googlePlaceJson) {
        String placeName = "";
        String vicinity = "-";
        String telefono = "";
        String id = "";
        String latitude = "";
        String longitude = "";
        Log.d("getPlace", "Entered");
        Log.w("Place", googlePlaceJson.toString());

        try {
            placeName = googlePlaceJson.getJSONObject("result").getString("name");
            Log.w("Place name",placeName);

            vicinity =  googlePlaceJson.getJSONObject("result").getString("vicinity");
            Log.w("Vicinity",vicinity);

            telefono = googlePlaceJson.getJSONObject("result").getString("formatted_phone_number");
            Log.w("telefono", telefono);

            id =  googlePlaceJson.getJSONObject("result").getString("place_id");
            Log.w("id ",id);

            latitude =
                    googlePlaceJson.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude =
                    googlePlaceJson.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getString("lng");

            Veterinarias vet = new Veterinarias(latitude,longitude,placeName,vicinity,telefono,id);

            mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vetapp-98f0d.firebaseio.com/");
            mDatabase = mRef.child("Veterinarias").child(id).setValue(vet);

        } catch (JSONException e) {
            Log.d("getPlaceDetails", "Error");
            e.printStackTrace();
        }
    }
}
