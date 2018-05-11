package com.example.rachel.vetApp;

import android.app.Dialog;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * A placeholder fragment containing a simple view.
 */
public class addVacunasActivityFragment extends Fragment {
    TextInputEditText veterinaria;
    TextInputEditText pesoactual;
    TextInputEditText date;

    public addVacunasActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_vacunas, container, false);
        veterinaria = view.findViewById(R.id.etVeterinaria);
        pesoactual = view.findViewById(R.id.etPesoActual);
        date = view.findViewById(R.id.btndate);

        final Spinner vacunas = view.findViewById(R.id.vacunasSpinner);
        ArrayAdapter<CharSequence> adapterVacunas = ArrayAdapter.createFromResource(this.getContext(),R.array.Tipos_Vacuna, android.R.layout.simple_spinner_item);
        adapterVacunas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vacunas.setAdapter(adapterVacunas);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "DatePicker");

                Log.w("Dialog", String.valueOf(newFragment.getDialog()));
                Dialog datetext = newFragment.getDialog();
                date.setText((CharSequence) datetext);

            }
        });

        return view;
    }
}
