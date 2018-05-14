package com.example.rachel.vetApp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class AdopcionesListado extends AppCompatActivity {

    Button btnAñadirAdopcion;
    ListView lvAdopciones;
    ImageView imageViewAdopciones;

    FirebaseListAdapter<Adopcion> adapter;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReferenceFromUrl("gs://vetapp-98f0d.appspot.com/");


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
        lvAdopciones = findViewById(R.id.lvAdopciones);

        DatabaseReference query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Adopcion");
        FirebaseListOptions<Adopcion> options = new FirebaseListOptions.Builder<Adopcion>()
                .setQuery(query,Adopcion.class)
                .setLayout(R.layout.entrada_adopciones)
                .build();

        btnAñadirAdopcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AdopcionesAnadir.class);
                startActivity(intent);
            }
        });

        AdopcionesListado.this.setTitle("Adopciones");


        adapter = new FirebaseListAdapter<Adopcion>(options) {
            @Override
            protected void populateView(View v, Adopcion model, int position) {
                TextView name = v.findViewById(R.id.tvNombre);
                TextView city = v.findViewById(R.id.tvCiudad);
                TextView pais = v.findViewById(R.id.tvPais);
                name.setText(model.getNombre());
                city.setText(model.getCiudad());
                pais.setText(model.getPais());
                ImageView imageViewAdopciones = v.findViewById(R.id.fotito);

                Glide.with(getApplicationContext())
                        .load(storageRef.child(model.getNombre()))
                        .into(imageViewAdopciones);
                /*String pictureString =model.getNombre();
                byte[] picture = Base64.decode(pictureString.getBytes(), Base64.DEFAULT);
                Glide.with(getApplicationContext()).load(picture).into(imageViewAdopciones);*/
                /*storageRef=storageRef.child(model.getNombre());
                Glide.with(getApplicationContext())
                        .using(new FirebaseImageLoader())
                        .load(storageRef)
                        .asBitmap()
                        .fitCenter()
                        .into(imageViewAdopciones);*/

                //String imagenString=model.getUrl();
                //Glide.with(getApplication()).load(imagenString).into(imageViewAdopciones);


                //imageViewAdopciones.setImageURI(Uri.parse(imagenString));
                //Glide.with(getApplicationContext()).load(storageRef.child("https://karemsarai.deviantart.com/art/Recursos-Para-El-pajarito-Png-318367481")).into(imageViewAdopciones);
                //Log.w("Foto url", String.valueOf(storageRef.child("fotos134412.jpg")));
                /*Glide.with(getContext())
                        .load(storageRef.child(model.getNIE()+".jpg"))
                        .into(photo);*/




            }
        };

        lvAdopciones.setAdapter(adapter);

    }

}




