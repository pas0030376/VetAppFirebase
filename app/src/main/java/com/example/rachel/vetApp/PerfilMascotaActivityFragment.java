package com.example.rachel.vetApp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



/**
 * A placeholder fragment containing a simple view.
 */
public class PerfilMascotaActivityFragment extends Fragment {

    ListView petList;

    FirebaseListAdapter<Pets> adapter;
    DatabaseReference query;
    FirebaseListOptions<Pets> options;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://vetapp-98f0d.appspot.com/");;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String id = user.getUid().toString();
    ImageView petImg;
    TextView petName, petBreed;

    public PerfilMascotaActivityFragment() {
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_mascota, container, false);

        FloatingActionButton addPet = view.findViewById(R.id.afegirPet);
        addPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), addPetActivity.class);
                startActivity(intent);

            }
        });

        petList = view.findViewById(R.id.petList);

        query = FirebaseDatabase.getInstance()
                .getReference()
                .child(id);

        options = new FirebaseListOptions.Builder<Pets>()
                .setQuery(query, Pets.class)
                .setLayout(R.layout.petlist)
                .build();

        adapter = new FirebaseListAdapter<Pets>(options) {
            @Override
            protected void populateView(View v, Pets model, int position) {
                petName = v.findViewById(R.id.petName);
                petBreed = v.findViewById(R.id.petBreed);
                petName.setText(model.getNameAddPet());
                petBreed.setText(model.getBreed());

                petImg = v.findViewById(R.id.petImg);

<<<<<<< HEAD
                /*if (model.getPetlistImg() != null) {
=======
               /* if (model.getImageURL() != null) {
>>>>>>> 47749eb9ce64554621959c5177a9f80b495a4c8d
                    Glide.with(getContext())
                            .load(storageRef.child(id + model.getImageURL() + ".jpg"))
                            .into(petImg);
                    Log.w("URL", String.valueOf(storageRef.child(id + model.getImageURL() + ".jpg")));
                }else{
                    Glide.with(getContext())
                            .load("http://3.bp.blogspot.com/-PT0BXLSMNaU/UJA8pf0kHoI/AAAAAAAAEjY/Ko8m6RAj6Mw/s1600/20.jpg")
                            .into(petImg);
                }*/

                storageRef.child(id+"_"+model.getNameAddPet()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.w("STORAGE", uri.toString());
                        Glide.with(getContext()).load(uri).into(petImg);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
            }
        };

        petList.setAdapter(adapter);
        petList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getContext(), DetailPetActivity.class);
                Pets pets = (Pets) parent.getItemAtPosition(position);
                i.putExtra("pets", pets);
                startActivity(i);
            }
        });

        return view;
    }
}




