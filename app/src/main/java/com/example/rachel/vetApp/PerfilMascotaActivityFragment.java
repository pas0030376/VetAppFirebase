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
import com.gjiazhe.multichoicescirclebutton.MultiChoicesCircleButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class PerfilMascotaActivityFragment extends Fragment {

    ListView petList;

    FirebaseListAdapter<Pets> adapter;
    DatabaseReference query;
    FirebaseListOptions<Pets> options;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://vetapp-98f0d.appspot.com/");

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String id = user.getUid().toString();
    ImageView petImg;
    TextView petName, petBreed;
    View view;

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
        view = inflater.inflate(R.layout.fragment_perfil_mascota, container, false);

       /* FloatingActionButton addPet = view.findViewById(R.id.afegirPet);
        addPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), addPetActivity.class);
                startActivity(intent);

            }
        });*/

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

                String url = model.getImageURL();
                Glide.with(getContext()).load(url).into(petImg);

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

       MultiChoicesCircleButton.Item item1 = new MultiChoicesCircleButton.Item("Añadir mascota", getResources().getDrawable(R.drawable.add), 60);
        MultiChoicesCircleButton.Item item2 = new MultiChoicesCircleButton.Item("Ver vacunas/cirugías", getResources().getDrawable(R.drawable.afegirsurgery), 120);

        List<MultiChoicesCircleButton.Item> buttonItems = new ArrayList<>();
        buttonItems.add(item1);
        buttonItems.add(item2);


        MultiChoicesCircleButton multiChoicesCircleButton = view.findViewById(R.id.multiChoicesPerfil);
        multiChoicesCircleButton.setButtonItems(buttonItems);

        multiChoicesCircleButton.setOnSelectedItemListener(new MultiChoicesCircleButton.OnSelectedItemListener() {
            @Override
            public void onSelected(MultiChoicesCircleButton.Item item, int index) {
                if (item.getText().equals("Añadir mascota")){
                    Intent pedirIntent = new Intent(getContext(), addPetActivity.class);
                    startActivity(pedirIntent); }
                else if (item.getText().equals("Ver vacunas/cirugías")){
                    Intent CancelarIntent = new Intent(getContext(), ListVacunasCirugiasActivity.class);
                    startActivity(CancelarIntent); }
            }
        });


        return view;
    }


}




