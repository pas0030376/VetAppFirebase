package com.example.rachel.vetApp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



public class AdopcionesListado extends AppCompatActivity {

    Button btnAñadirAdopcion;
    ListView lvAdopciones;
    ImageView imageViewAdopciones;

    FirebaseListAdapter<Adopcion> adapter;
    DatabaseReference query;
    FirebaseListOptions<Adopcion> options;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopcioneslistado);
        btnAñadirAdopcion=(Button)findViewById(R.id.btnAñadirAdopcion);

        btnAñadirAdopcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AdopcionesAnadir.class);
                startActivity(intent);
            }
        });

        AdopcionesListado.this.setTitle("Adopciones");


        lvAdopciones = findViewById(R.id.lvAdopciones);

        query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Adoption");

        options = new FirebaseListOptions.Builder<Adopcion>()
                .setQuery(query, Adopcion.class)
                .setLayout(R.layout.entrada_adopciones)
                .build();

        adapter = new FirebaseListAdapter<Adopcion>(options) {
            @Override
            protected void populateView(View v, Adopcion model, int position) {
                TextView name = v.findViewById(R.id.tvNombre);
                TextView city = v.findViewById(R.id.tvCiudad);
                TextView pais = v.findViewById(R.id.tvPais);
                name.setText(model.getNombre());
                city.setText(model.getCiudad());
                pais.setText(model.getPais());

                imageViewAdopciones = v.findViewById(R.id.imageViewAdopciones);

                storageRef = storage.getReferenceFromUrl("gs://vetapp-98f0d.appspot.com/");
                storageRef.child(model.getNombre()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.w("STORAGE", uri.toString());
                            Glide.with(getApplicationContext()).load(uri).into(imageViewAdopciones);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });


            }
        };

        lvAdopciones.setAdapter(adapter);

    }

}




