package com.gestion.appgestion.Vistas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gestion.appgestion.Modelo.Tarea;
import com.gestion.appgestion.R;
import com.gestion.appgestion.Utilidades.ListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PrimerFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton button_float;
    List<Tarea> tareaList;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private ProgressDialog loadingBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //https://www.youtube.com/watch?v=sYHKhwoVU4Q barra de navegación arriba
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_primer, container, false);
        button_float = view.findViewById(R.id.button_float);
        button_float.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        listTask(view);
        return  view;
    }

    public void progress(String mensaje){
        loadingBar=new ProgressDialog(getContext());
        loadingBar.setMessage(mensaje);
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }


    public void listTask(View view){//https://www.youtube.com/watch?v=Mne2SrtySME
        progress("Cargando Datos...");
        tareaList = new ArrayList<>();
        firestore.collection("tarea").orderBy("titulo").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String id = document.getId();
                    Tarea tarea = document.toObject(Tarea.class);
                    tarea.setId(id);
                    tareaList.add(tarea);
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                loadingBar.dismiss();
                ListAdapter listAdapter = new ListAdapter(tareaList, getContext(), new ListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Tarea item) {
                        message("click al item: " + item.getId());
                    }
                });
                RecyclerView recyclerView = view.findViewById(R.id.listRecycleView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(listAdapter);
            }
        });
    }

    public void message(String mensaje){
        Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View event) { // https://stackoverflow.com/questions/3426917/how-to-add-two-edit-text-fields-in-an-alert-dialog
        if(button_float==event){ // https://www.youtube.com/watch?v=Kz9TkDY2sP8
            final EditText titulo= new EditText(getContext());
            final EditText descripcion= new EditText(getContext());
            titulo.setHint("Título");
            titulo.setMinEms(16);
            titulo.setInputType(InputType.TYPE_CLASS_TEXT);
            descripcion.setHint("Descripción");
            descripcion.setMinEms(16);
            descripcion.setInputType(InputType.TYPE_CLASS_TEXT);
            LinearLayout linearLayout=new LinearLayout(getContext());
            linearLayout.setOrientation(linearLayout.VERTICAL);
            linearLayout.addView(titulo);
            linearLayout.addView(descripcion);
            linearLayout.setPadding(70,50,70,10);
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle("Crear Nueva Tarea")
                    .setPositiveButton("Aceptar",null)
                    .setNegativeButton("Cancelar",null)
                    .setView(linearLayout)
                    .show();
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String titulo_tarea      = titulo.getText().toString().trim();
                    String descripcion_tarea = descripcion.getText().toString().trim();
                    if(titulo_tarea.isEmpty() || descripcion_tarea.isEmpty()){
                        message("Complete todos los campos");
                    }else{
                        registerTask(titulo_tarea, descripcion_tarea);
                        dialog.dismiss();
                    }
                }
            });
        }
    }


    public void registerTask(String task, String description){
        String id = firebaseAuth.getCurrentUser().getUid();
        Map<String, Object> map = new HashMap<>();
        map.put("id_usuario", id);
        map.put("titulo", task);
        map.put("descripcion", description);
        map.put("fecha_inicio", fecha());
        map.put("fecha_fin", "");
        map.put("asignado", "");
        map.put("estado", "Pendiente");
        map.put("activo", "1");
        firestore.collection("tarea").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                listTask(getView());
                message("Los datos se almacenaron correctamente.");
            }}).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                message("Error al registrar.");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String fecha(){{
        ZoneId zona = ZoneId.of("America/Lima");
        LocalDate localDate = LocalDate.now(zona);
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String currentDate = localDate.format(f);
        return currentDate;
        //Calendar calendar = Calendar.getInstance();
        //SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        //String currentDate = localDate.formato.format(calendar.getTime());
        //String date = DateFormat.getDateInstance().format(calendar.getTime());
        //return date;
    }}
}