package com.example.rachel.vetApp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rachel.vetapplove.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CitasActivityFragment extends Fragment {

    public CitasActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_citas, container, false);
    }
}
