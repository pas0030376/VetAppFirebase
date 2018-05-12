package com.example.rachel.vetApp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.gjiazhe.multichoicescirclebutton.MultiChoicesCircleButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailPetActivityFragment extends Fragment {

    FirebaseListAdapter<Pets> adapter;
    DatabaseReference query;
    FirebaseListOptions<Pets> options;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://vetapp-98f0d.appspot.com/");;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String id = user.getUid().toString();

    TextView name, especie, edad, raza, peso, enfermedades, esterilizado, bdate, sexo, alergias;
    ImageView detailImg;

    public DetailPetActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_pet, container, false);

        getActivity().setTitle("Perfil de Mascota");

        MultiChoicesCircleButton.Item item1 = new MultiChoicesCircleButton.Item("Añadir vacunas", getResources().getDrawable(R.drawable.afegirvacuna), 60);
        MultiChoicesCircleButton.Item item2 = new MultiChoicesCircleButton.Item("Añadir cirugías", getResources().getDrawable(R.drawable.afegirsurgery), 120);

        List<MultiChoicesCircleButton.Item> buttonItems = new ArrayList<>();
        buttonItems.add(item1);
        buttonItems.add(item2);


        MultiChoicesCircleButton multiChoicesCircleButton = view.findViewById(R.id.multiChoicesPetDetails);
        multiChoicesCircleButton.setButtonItems(buttonItems);

        multiChoicesCircleButton.setOnSelectedItemListener(new MultiChoicesCircleButton.OnSelectedItemListener() {
            @Override
            public void onSelected(MultiChoicesCircleButton.Item item, int index) {
                switch (item.getText()) {
                    case "Añadir vacunas":
                        Intent i = new Intent(getContext(), addVacunasActivity.class);
                        startActivity(i);
                        break;
                    case "Añadir cirugías":
                        startActivity( new Intent(getContext(), addSurgeryActivity.class));
                        break;

                    default:
                        break;
                }

            }
        });

        name = view.findViewById(R.id.detailNamePet);
        especie = view.findViewById(R.id.detailEspecie);
        edad = view.findViewById(R.id.detailEdad);
        raza = view.findViewById(R.id.detailRaza);
        peso = view.findViewById(R.id.detailPeso);
        enfermedades = view.findViewById(R.id.detailEnfermedades);
        esterilizado = view.findViewById(R.id.detailEsterilizado);
        bdate = view.findViewById(R.id.detailBdate);
        sexo = view.findViewById(R.id.detailSexo);
        alergias = view.findViewById(R.id.detailAlergias);

        detailImg = view.findViewById(R.id.detailImg);

        Intent i = getActivity().getIntent();
        if (i != null) {
            Pets pets = (Pets) i.getSerializableExtra("pets");
            if (pets != null) {
                mostrarMascotas(pets);
            }
        }


        return view;
    }

    private void mostrarMascotas(Pets pets) {
        String nom = pets.getNameAddPet().toString();
        String age = pets.getEdad().toString();
        String breed = pets.getBreed().toString();
        String gender = pets.getGenderAddPet().toString();
        String specie = pets.getSpecies().toString();
        String weight = pets.getPeso().toString();
        String enfer = pets.getEnfermedades().toString();
        String esteril = pets.getEsterilizado().toString();
        String allergy = pets.getAlergias().toString();
        String fechNac = pets.getBdateAddPet().toString();


        name.setText(nom);
        edad.setText(age);
        raza.setText(breed);
        sexo.setText(gender);
        especie.setText(specie);
        peso.setText(weight);
        enfermedades.setText(enfer);
        esterilizado.setText(esteril);
        alergias.setText(allergy);
        bdate.setText(fechNac);

        storageRef.child(id+"_"+pets.getNameAddPet()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.w("STORAGE", uri.toString());
                Glide.with(getContext()).load(uri).into(detailImg);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }
}
