package com.example.rachel.vetApp;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jackandphantom.circularimageview.CircleImage;



public class PedirCitaActivityFragment extends Fragment {

    ListView list;
    ListView listvets;
    FirebaseListAdapter<Pets> adapter;
    DatabaseReference query;
    FirebaseListOptions<Pets> options;

    FirebaseListAdapter<Veterinarias> adaptervet;
    DatabaseReference queryvet;
    FirebaseListOptions<Veterinarias> optionsvet;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://vetapp-98f0d.appspot.com/");;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String id = user.getUid().toString();

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        adaptervet.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        adaptervet.stopListening();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_pedir_cita, container, false);

        list = view.findViewById(R.id.lvmypets);
        listvets = view.findViewById(R.id.lvVeterinarias);

        query = FirebaseDatabase.getInstance()
                .getReference()
                .child(id);

        options = new FirebaseListOptions.Builder<Pets>()
                .setQuery(query, Pets.class)
                .setLayout(R.layout.listcitaspets)
                .build();

        adapter = new FirebaseListAdapter<Pets>(options) {
            @Override
            protected void populateView(View v, Pets model, int position) {
                CircleImage photo = v.findViewById(R.id.cipet);
                TextView petName = v.findViewById(R.id.tvname);

                petName.setText(model.getNameAddPet());

                String url = id + model.getNameAddPet() + ".jpg";
                if (model.getPetlistImg() != null) {
                    Glide.with(getContext())
                            .load(storageRef.child(url))
                            .into(photo);
                }else{
                    Glide.with(getContext())
                            .load("http://3.bp.blogspot.com/-PT0BXLSMNaU/UJA8pf0kHoI/AAAAAAAAEjY/Ko8m6RAj6Mw/s1600/20.jpg")
                            .into(photo);
                }
            }
        };

        queryvet = FirebaseDatabase.getInstance()
                .getReference()
                .child("Veterinarias");

        optionsvet = new FirebaseListOptions.Builder<Veterinarias>()
                .setQuery(queryvet, Veterinarias.class)
                .setLayout(R.layout.vetslits)
                .build();

        adaptervet = new FirebaseListAdapter<Veterinarias>(optionsvet) {
            @Override
            protected void populateView(View v, Veterinarias model, int position) {
                TextView vetName = v.findViewById(R.id.tvVetNom);
                vetName.setText(model.getName());
            }
        };

        list.setAdapter(adapter);
        listvets.setAdapter(adaptervet);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pets pets = (Pets) parent.getItemAtPosition(position);
                AddToCita(pets);
            }
        });
        listvets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Veterinarias vet = (Veterinarias) adapterView.getItemAtPosition(i);
                String id = vet.getId();
                getUrlDetails(id);
            }
        });
        return view;
    }

    private void AddToCita(Pets pets) {
        pets.getNameAddPet();
    }

    private void getUrlDetails(String id) {
        Object[] DataTransfer = new Object[1];
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        googlePlacesUrl.append("placeid="+id+"&key=AIzaSyDIHLr2xsjP_3cOLCz1UU0Hir45B9KDykg");
        String url = googlePlacesUrl.toString();
        DataTransfer[0] = url;
        GetDetailsData getDetailsData = new GetDetailsData();
        getDetailsData.execute(DataTransfer);
    }
}
