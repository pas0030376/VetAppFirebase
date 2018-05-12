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
    FirebaseListAdapter<Pets> adapter;
    DatabaseReference query;
    FirebaseListOptions<Pets> options;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://vetapp-98f0d.appspot.com/");;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String id = user.getUid().toString();

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
        View view =  inflater.inflate(R.layout.fragment_pedir_cita, container, false);

        list = view.findViewById(R.id.lvmypets);
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

                String url = id + "_" + model.getNameAddPet() + ".jpg";
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

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pets pets = (Pets) parent.getItemAtPosition(position);
                AddpetToCita(pets);
            }
        });

        return view;

    }

    private void AddpetToCita(Pets pets) {
        pets.getNameAddPet();
        
    }


}
