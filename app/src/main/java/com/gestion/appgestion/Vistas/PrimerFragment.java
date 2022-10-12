package com.gestion.appgestion.Vistas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gestion.appgestion.Modelo.Tarea;
import com.gestion.appgestion.R;
import com.gestion.appgestion.Utilidades.ListAdapter;
import java.util.ArrayList;
import java.util.List;

public class PrimerFragment extends Fragment {


    List<Tarea> tareaList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_primer, container, false);
        listTask(view);
        return  view;
    }

    //https://www.youtube.com/watch?v=Mne2SrtySME
    public void listTask(View view){
        tareaList = new ArrayList<>();
        tareaList.add(new Tarea(1,"Test","Pendiente","Probando listView"));
        tareaList.add(new Tarea(2,"Test","Pendiente","Probando listView"));
        tareaList.add(new Tarea(3,"Test","Pendiente","Probando listView"));
        tareaList.add(new Tarea(4,"Test","Pendiente","Probando listView"));
        tareaList.add(new Tarea(4,"Test","Pendiente","Probando listView"));
        tareaList.add(new Tarea(4,"Test","Pendiente","Probandoasdasdasdasasdasdaasdasdasdasdasdgaysdgaysdgasydasydyasgdyasydsaygyasad listView"));
        tareaList.add(new Tarea(4,"Test","Pendiente","Probando listView"));
        tareaList.add(new Tarea(4,"Test","Pendiente","Probando listView"));
        tareaList.add(new Tarea(4,"Test","Pendiente","Probando listView"));
        tareaList.add(new Tarea(4,"Test","Pendiente","Probando listView"));
        tareaList.add(new Tarea(4,"Test","Pendiente","Probando listView"));
        tareaList.add(new Tarea(4,"Test","Pendiente","Probando listView"));
        tareaList.add(new Tarea(4,"Test","Pendiente","Probando listView"));
        tareaList.add(new Tarea(4,"Test","Pendiente","Probando listView"));
        tareaList.add(new Tarea(4,"Test","Pendiente","Probando listView"));
        ListAdapter listAdapter = new ListAdapter(tareaList,getContext());
        RecyclerView recyclerView = view.findViewById(R.id.listRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapter);
    }
}