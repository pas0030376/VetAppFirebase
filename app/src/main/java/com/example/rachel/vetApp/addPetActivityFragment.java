package com.example.rachel.vetApp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class addPetActivityFragment extends Fragment {

    EditText etName, etSpecies, etBreed, etGender, etBirthdate;
    Button btnSavePet;
    ImageView imgImageAddPet;

    private DatabaseReference mRef;
    private Task<Void> mDatabase;

    private static final int ACTIVITAT_SELECCIONAR_IMATGE = 1;
    Bitmap bitmap=null;

    public addPetActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_add_pet, container, false);

        etName = view.findViewById(R.id.nameAddPet);
        etSpecies = view.findViewById(R.id.species);
        etBreed = view.findViewById(R.id.breed);
        etGender = view.findViewById(R.id.genderAddPet);
        etBirthdate = view.findViewById(R.id.bdateAddPet);
        btnSavePet = view.findViewById(R.id.savePet);
        imgImageAddPet = view.findViewById(R.id.imageAddPet);


        imgImageAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveImageOfPet();
            }
        });

        btnSavePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               writeNewPet();

            }
        });

        return view;
    }

    private void saveImageOfPet() {
        
    }

    private void writeNewPet() {

        String nameAddPet =etName.getText().toString();
        String species = etSpecies.getText().toString();
        String breed = etBreed.getText().toString();
        String bdateAddPet = etBirthdate.getText().toString();
        String genderAddPet = etGender.getText().toString();
        String imageAddPet="";
        //String username = getActivity().getIntent().getExtras().getString("usuario");

        Pets pets = new Pets(nameAddPet, species, breed, bdateAddPet, genderAddPet);
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vetapp-98f0d.firebaseio.com/");
        mDatabase = mRef.child("Pets").child(nameAddPet).setValue(pets);

        CharSequence text = "Location added.";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getContext(), text, duration);
        toast.show();


    }


}
