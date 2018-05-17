package com.example.rachel.vetApp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListVacunasCirugiasActivityFragment extends Fragment {
    FirebaseListAdapter<Cirugiass> adapter;
    DatabaseReference query;
    FirebaseListOptions<Cirugiass> options;

    ListView surgeryList;
    View view;
    public ListVacunasCirugiasActivityFragment() {
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
        view = inflater.inflate(R.layout.fragment_list_vacunas_cirugias, container, false);
        getActivity().setTitle("Listado");

        surgeryList = view.findViewById(R.id.lvSurgVaccine);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String id = user.getUid().toString();


        query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Surgery"+id);

        options = new FirebaseListOptions.Builder<Cirugiass>()
                .setQuery(query, Cirugiass.class)
                .setLayout(R.layout.lv_cirugias)
                .build();

        adapter = new FirebaseListAdapter<Cirugiass>(options) {
            @Override
            protected void populateView(View v, Cirugiass model, int position) {
                TextView tipo = v.findViewById(R.id.tipoSurgery);
                TextView vet = v.findViewById(R.id.vetSurgery);
                TextView fecha = v.findViewById(R.id.fechaSurgery);
                TextView observaciones = v.findViewById(R.id.observacionesSurgery);

                tipo.setText(model.getTipo());
                vet.setText(model.getVet());
                fecha.setText(model.getFecha());
                observaciones.setText(model.getObservaciones());
            }
        };

        surgeryList.setAdapter(adapter);

        return view;
    }
}
