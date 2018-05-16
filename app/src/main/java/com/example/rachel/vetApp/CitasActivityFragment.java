package com.example.rachel.vetApp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.gjiazhe.multichoicescirclebutton.MultiChoicesCircleButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jackandphantom.circularimageview.CircleImage;

import java.util.ArrayList;
import java.util.List;

public class CitasActivityFragment extends Fragment {
    FirebaseListAdapter<Cita> adapter;
    DatabaseReference query;
    FirebaseListOptions<Cita> options;
    ListView dates;
    View view;
    TextView vetname;
    TextView dateDate;
    CircleImage petpic;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://vetapp-98f0d.appspot.com/");

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String id = user.getUid().toString();
    public CitasActivityFragment() {
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
       view = inflater.inflate(R.layout.fragment_citas, container, false);
       getActivity().setTitle("Citas");

       dates = view.findViewById(R.id.lvcitas);

       String citalink = "Citas"+id;
        query = FirebaseDatabase.getInstance()
                .getReference()
                .child(citalink);
        Log.i("CITA LINK ", citalink);

        options = new FirebaseListOptions.Builder<Cita>()
                .setQuery(query, Cita.class)
                .setLayout(R.layout.citalist)
                .build();

        adapter = new FirebaseListAdapter<Cita>(options) {
            @Override
            protected void populateView(View v, Cita model, int position) {
                vetname = v.findViewById(R.id.tvvetfordate);
                dateDate = v.findViewById(R.id.tvdateofdate);
                petpic = v.findViewById(R.id.petpic);

                vetname.setText(model.getVeterinaria());
                dateDate.setText(model.getFecha());

                String url = id+model.getMascota();
                Log.w("URL OF IMAGE", String.valueOf(storageRef.child(url)));
               // Glide.with(getContext()).load(url).into(petpic);
                Glide.with(getContext())
                        .load(storageRef.child(url))
                        .into(petpic);

            }
        };

        dates.setAdapter(adapter);





        MultiChoicesCircleButton.Item item1 = new MultiChoicesCircleButton.Item("Pedir Cita", getResources().getDrawable(R.drawable.icondate), 60);
        MultiChoicesCircleButton.Item item2 = new MultiChoicesCircleButton.Item("Cancelar Cita", getResources().getDrawable(R.drawable.iconcancel), 120);

        List<MultiChoicesCircleButton.Item> buttonItems = new ArrayList<>();
        buttonItems.add(item1);
        buttonItems.add(item2);

        MultiChoicesCircleButton multiChoicesCircleButton = view.findViewById(R.id.multiChoicesCircleButton);
        multiChoicesCircleButton.setButtonItems(buttonItems);

        multiChoicesCircleButton.setOnSelectedItemListener(new MultiChoicesCircleButton.OnSelectedItemListener() {
            @Override
            public void onSelected(MultiChoicesCircleButton.Item item, int index) {
              if (item.getText().equals("Pedir Cita")){
                  Intent pedirIntent = new Intent(getContext(), PedirCitaActivity.class);
                  startActivity(pedirIntent); }
               else if (item.getText().equals("Cancelar Cita")){
                  Intent CancelarIntent = new Intent(getContext(), CitasDetailActivity.class);
                  startActivity(CancelarIntent); }
            }
        });
        return view;
    }
}
