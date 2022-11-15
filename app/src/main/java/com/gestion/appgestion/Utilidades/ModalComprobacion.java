package com.gestion.appgestion.Utilidades;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gestion.appgestion.Modelo.Comprobacion;
import com.gestion.appgestion.Modelo.Tarea;
import com.gestion.appgestion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class ModalComprobacion extends DialogFragment {

    private FirebaseFirestore firebaseFirestore;
    private Comprobacion comprobacion;
    private List<Comprobacion> comprobacionList;
    private Tarea tarea;
    private LinearLayout txt_informacion;
    private TextView comprobaciones_realizadas, total_comprobaciones;

    public ModalComprobacion(Tarea tarea) {
        this.tarea = tarea;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_comprobacion,container,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        txt_informacion = view.findViewById(R.id.txt_informacion);
        total_comprobaciones = view.findViewById(R.id.total_comprobaciones);
        comprobaciones_realizadas = view.findViewById(R.id.comprobaciones_realizadas);
        listar_Comprobacion(view);
        total_comprobaciones();
        comprobantes_realizados();
        sin_informacion();
        return view;
    }

    public void listar_Comprobacion(View view){
        firebaseFirestore.collection("comprobacion").whereEqualTo("id_tarea", tarea.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            comprobacionList = new ArrayList<>();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                comprobacion = new Comprobacion();
                comprobacion.setId(document.getId());
                comprobacion.setTitulo(document.getString("titulo"));
                comprobacion.setRealizado(document.getBoolean("realizado"));
                comprobacionList.add(comprobacion);
            }
        }}).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            ListAdapterComprobacion listAdapter = new ListAdapterComprobacion(comprobacionList,getContext());
            RecyclerView recyclerView = view.findViewById(R.id.lista_recicleview_jhon);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(listAdapter);
        }});
    }

    public void sin_informacion(){
        Query docRef = firebaseFirestore.collection("comprobacion").whereEqualTo("id_tarea", tarea.getId());
        docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.size()<=0){
                    txt_informacion.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void total_comprobaciones(){
        Query docRef = firebaseFirestore.collection("comprobacion").whereEqualTo("id_tarea", tarea.getId());
        docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                total_comprobaciones.setText(value.size()+"");
            }
        });
    }

    public void comprobantes_realizados(){
        Query docRef = firebaseFirestore.collection("comprobacion").whereEqualTo("id_tarea", tarea.getId()).whereEqualTo("realizado",true);
        docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                comprobaciones_realizadas.setText(value.size()+"");
            }
        });
    }

}
