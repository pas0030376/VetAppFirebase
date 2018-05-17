package com.example.rachel.vetApp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


/**
 * A placeholder fragment containing a simple view.
 */
public class addSurgeryActivityFragment extends Fragment {

    TextInputEditText veterinaria;
    TextInputEditText observaciones;
    static TextInputEditText date;
    Button btnGuardar;
    View view;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String id = user.getUid().toString();

    private DatabaseReference mRef;
    private Task<Void> mDatabase;

    Spinner cirugias;
    String petNAme;
    public addSurgeryActivityFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_add_surgery, container, false);

        getActivity().setTitle("Cirugías");

        Intent i = getActivity().getIntent();

        if(i!=null){
            petNAme = (String) i.getSerializableExtra("petName");
            if(petNAme!=null){

                Log.i("Pet nameeee", petNAme);
            }

        }

        veterinaria = view.findViewById(R.id.etSurgVeterinaria);
        observaciones = view.findViewById(R.id.observaciones);
        date = view.findViewById(R.id.btndateSurg);
        btnGuardar = view.findViewById(R.id.btnGuardar);

        cirugias = view.findViewById(R.id.surgerySpinner);
        ArrayAdapter<CharSequence> adapterVacunas = ArrayAdapter.createFromResource(this.getContext(),R.array.Tipos_cirugias, android.R.layout.simple_spinner_item);
        adapterVacunas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cirugias.setAdapter(adapterVacunas);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View v) {
                        DialogFragment newFragment = new SelectDateFragment();
                        newFragment.show(getFragmentManager(), "DatePicker");
                    }
                });


        btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveSurgery(id, petNAme);
                        Log.i("Pet nameeee", petNAme);
                    }
                });

        return view;
    }

    private void saveSurgery(String id, String petName) {
        String vet = veterinaria.getText().toString();
        String observation = observaciones.getText().toString();
        String fecha = date.getText().toString();
        String ftipo = cirugias.getSelectedItem().toString().trim();

        Cirugiass cirugias = new Cirugiass(ftipo, vet, fecha, observation, petName);
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vetapp-98f0d.firebaseio.com/");
        String val = petName+mRef.push().getKey().toString();
        mDatabase = mRef.child("Surgery"+id).child(petName).setValue(cirugias);

        Intent i = new Intent(getContext(), PerfilMascotaActivity.class);
        i.putExtra("mascota", val);
        startActivity(i);
        Toast.makeText(getContext(), "Carga exitosa!", Toast.LENGTH_SHORT).show();

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
