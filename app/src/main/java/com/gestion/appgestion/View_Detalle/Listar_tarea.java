package com.gestion.appgestion.View_Detalle;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.gestion.appgestion.Modelo.Tablero;
import com.gestion.appgestion.Modelo.Tarea;
import com.gestion.appgestion.R;
import com.gestion.appgestion.Utilidades.ListAdapterTarea;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Listar_tarea extends Fragment implements View.OnClickListener {

    private Tablero tablero;
    private Tarea tarea;
    private List<Tarea> tareaList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton btn_agregar_tarea;
    private TextView titulo_tablero , count_tarea;

    public static Listar_tarea newInstance(Tablero tablero) {
        Listar_tarea fragment = new Listar_tarea();
        Bundle args = new Bundle();
        args.putSerializable("class_tablero", tablero);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tablero = (Tablero) getArguments().getSerializable("class_tablero");
        }
    }

    public void message(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listar_tarea, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        btn_agregar_tarea = view.findViewById(R.id.btn_agregar_tarea);
        btn_agregar_tarea.setOnClickListener(this);
        titulo_tablero = view.findViewById(R.id.titulo_tablero);
        titulo_tablero.setText("Tablero: "+tablero.getTitulo());
        count_tarea = view.findViewById(R.id.count_tarea);
        listar_Tarea(view);
        return  view;
    }


    public void listar_Tarea(View view){
        tareaList = new ArrayList<>();
        firebaseFirestore.collection("tarea").whereEqualTo("id_tablero",tablero.getId_tablero()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    try {
                        tarea = new Tarea();
                        tarea.setId(document.getId());
                        tarea.setId_tablero(document.getString("id_tablero"));
                        tarea.setId_usuario(document.getString("id_usuario"));
                        tarea.setTitulo(document.getString("titulo"));
                        tarea.setDescripcion(document.getString("descripcion"));
                        tarea.setFecha_creacion(document.getString("fecha_creacion"));
                        tarea.setFecha_inicio(document.getString("fecha_inicio"));
                        tarea.setFecha_finalizacion(document.getString("fecha_finalizacion"));
                        tarea.setEstado(document.getString("estado"));
                        tareaList.add(tarea);
                    }catch (Exception exception){
                        message("error:`"+exception);
                    }
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ListAdapterTarea listAdapter = new ListAdapterTarea(tareaList,getContext(), new ListAdapterTarea.OnItemClickListener() {
                    @Override
                    public void onItemClick(Tarea item) {
                        message("click a la tarea: "+ item.getTitulo());
                        //startActivity(new Intent(getApplicationContext(), Detalle_Tablero.class).putExtra("class_tablero",item));//enviamos los datos datos del tablero a dellate_tablero
                    }
                });
                RecyclerView recyclerView = view.findViewById(R.id.listRecycleView_tarea);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(listAdapter);
                if(listAdapter.getItemCount()<=0){
                    count_tarea.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        if(btn_agregar_tarea==view){ // https://www.youtube.com/watch?v=Kz9TkDY2sP8
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
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {
                    String titulo_tarea      = titulo.getText().toString().trim();
                    String descripcion_tarea = descripcion.getText().toString().trim();
                    if(titulo_tarea.isEmpty() || descripcion_tarea.isEmpty()){
                        message("Complete todos los campos");
                    }else{
                        registrar_Tarea(titulo_tarea, descripcion_tarea);
                        dialog.dismiss();
                    }
                }
            });
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void registrar_Tarea(String titulo, String descripcion){
        Map<String, Object> map = new HashMap<>();
        map.put("id_tablero", tablero.getId_tablero());
        map.put("id_usuario", firebaseAuth.getCurrentUser().getUid());
        map.put("titulo", titulo);
        map.put("descripcion", descripcion);
        map.put("fecha_creacion",fecha());
        map.put("fecha_inicio", "");
        map.put("fecha_finalizacion", "");
        map.put("estado", "Pendiente");
        firebaseFirestore.collection("tarea").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                listar_Tarea(getView());
                message("Se ha creado una tarea.");
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
    }}

}