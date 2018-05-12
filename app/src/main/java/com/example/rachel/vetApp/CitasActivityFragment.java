package com.example.rachel.vetApp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gjiazhe.multichoicescirclebutton.MultiChoicesCircleButton;

import java.util.ArrayList;
import java.util.List;

public class CitasActivityFragment extends Fragment {


    public CitasActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_citas, container, false);

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
                  Intent pedirIntent = new Intent(getContext(), addVacunasActivity.class);
                  startActivity(pedirIntent); }
               else if (item.getText().equals("Cancelar Cita")){
                  Intent CancelarIntent = new Intent(getContext(), addVacunasActivity.class);
                  startActivity(CancelarIntent); }
            }
        });

        return view;
    }
}
