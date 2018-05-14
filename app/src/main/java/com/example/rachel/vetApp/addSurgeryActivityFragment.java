package com.example.rachel.vetApp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.util.Calendar;


/**
 * A placeholder fragment containing a simple view.
 */
public class addSurgeryActivityFragment extends Fragment {

    TextInputEditText veterinaria;
    static TextInputEditText date;
    View view;

    public addSurgeryActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_add_surgery, container, false);

        getActivity().setTitle("Cirugías");

        veterinaria = view.findViewById(R.id.etSurgVeterinaria);
        date = view.findViewById(R.id.btndateSurg);

        final Spinner vacunas = view.findViewById(R.id.surgerySpinner);
        ArrayAdapter<CharSequence> adapterVacunas = ArrayAdapter.createFromResource(this.getContext(),R.array.Tipos_cirugias, android.R.layout.simple_spinner_item);
        adapterVacunas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vacunas.setAdapter(adapterVacunas);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        return view;
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
            date.setText(day+"/"+month+"/"+year);
        }


    }

}
