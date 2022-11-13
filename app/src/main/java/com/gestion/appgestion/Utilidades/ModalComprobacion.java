package com.gestion.appgestion.Utilidades;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gestion.appgestion.Modelo.Comprobacion;
import com.gestion.appgestion.R;
import java.util.ArrayList;
import java.util.List;

public class ModalComprobacion extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_comprobacion,container,false);
        listar_Comprobacion(view);
        return view;
    }

    public void listar_Comprobacion(View view){
        List<Comprobacion> comprobacionList = new ArrayList<>();
        comprobacionList.add(new Comprobacion("d13"));
        comprobacionList.add(new Comprobacion("123d13"));
        comprobacionList.add(new Comprobacion("d112"));
        comprobacionList.add(new Comprobacion("3d32"));
        ListAdapterComprobacion listAdapter = new ListAdapterComprobacion(comprobacionList,getContext());
        RecyclerView recyclerView = view.findViewById(R.id.lista_recicleview_jhon);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapter);
    }

}
