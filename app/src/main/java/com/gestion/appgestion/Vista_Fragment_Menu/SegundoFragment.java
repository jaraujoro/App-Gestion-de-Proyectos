package com.gestion.appgestion.Vista_Fragment_Menu;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gestion.appgestion.Modelo.Usuario;
import com.gestion.appgestion.R;


public class SegundoFragment extends Fragment {

    private Usuario usuario;

    public SegundoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            usuario = (Usuario) getArguments().getSerializable("usser_class");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_segundo, container, false);
        return  view;
    }
}