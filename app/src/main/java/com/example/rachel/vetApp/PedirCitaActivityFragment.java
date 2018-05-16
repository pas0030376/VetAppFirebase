package com.example.rachel.vetApp;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
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

import java.util.Calendar;


public class PedirCitaActivityFragment extends Fragment {
    ListView list;
    ListView listvets;
    FirebaseListAdapter<Pets> adapter;
    DatabaseReference query;
    TextView mascota;
    TextView vet;
    TextView telefono;
    TextView choose;
        FirebaseListOptions<Pets> options;
    View view;

    FirebaseListAdapter<Veterinarias> adaptervet;
    DatabaseReference queryvet;
    FirebaseListOptions<Veterinarias> optionsvet;
    static TextInputEditText dateCita;


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
        view =  inflater.inflate(R.layout.fragment_pedir_cita, container, false);
        getActivity().setTitle("Pide una cita");
        mascota = view.findViewById(R.id.tvMascotaCita);
        vet = view.findViewById(R.id.tvCitaVet);
        telefono = view.findViewById(R.id.tvphone);
        choose = view.findViewById(R.id.tvChoose);
        dateCita = view.findViewById(R.id.etfechaCita);

        if (mascota == null){
        mascota.setVisibility(View.GONE);
        }
        if (vet == null){
            vet.setVisibility(View.GONE);
            telefono.setVisibility(View.GONE);
        }

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

                String url = model.getImageURL();
                Glide.with(getContext()).load(url).into(photo);
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
                Veterinarias vetDetails = (Veterinarias) adapterView.getItemAtPosition(i);
                AddVetToCita(vetDetails);
            }
        });


        dateCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
        return view;
    }

    private void AddVetToCita(Veterinarias vetDetails) {
        vetDetails.getPhone();
        vet = view.findViewById(R.id.tvCitaVet);
        telefono = view.findViewById(R.id.tvphone);
        vet.setText(vetDetails.getName());
        telefono.setText(vetDetails.getPhone());
        listvets.setVisibility(View.GONE);

    }

    private void AddToCita(Pets pets) {
      pets.getNameAddPet();
      mascota = view.findViewById(R.id.tvMascotaCita);
      mascota.setText(pets.getNameAddPet());

      list.setVisibility(View.GONE);
    }

    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm, dd);
        }

        public void populateSetDate(int year, int month, int day) {
            dateCita.setText(day + "/" + month + "/" + year);
        }
    }

}
