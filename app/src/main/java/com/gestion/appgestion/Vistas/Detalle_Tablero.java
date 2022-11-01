package com.gestion.appgestion.Vistas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gestion.appgestion.Modelo.Tablero;
import com.gestion.appgestion.Modelo.Tarea;
import com.gestion.appgestion.R;
import com.gestion.appgestion.Utilidades.ListAdapterTarea;
import com.gestion.appgestion.Vista_Usser.Login_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Detalle_Tablero extends AppCompatActivity implements View.OnClickListener {

    private Tablero tablero;
    private boolean favorito = false;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton btn_agregar_tarea;
    private TextView titulo_tablero , informacion_tarea;
    List<Tarea> tareaList;
    Tarea tarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_tablero);
        tablero = (Tablero) getIntent().getSerializableExtra("class_tablero");
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        btn_agregar_tarea = findViewById(R.id.btn_agregar_tarea);
        btn_agregar_tarea.setOnClickListener(this);
        titulo_tablero = findViewById(R.id.titulo_tablero);
        titulo_tablero.setText("Tablero: "+tablero.getTitulo());
        informacion_tarea = findViewById(R.id.informacion_tarea);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tareas");
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation_tarea);
        navigation.setOnNavigationItemSelectedListener(itemSelected);
        listar_Tarea();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener itemSelected = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.lista_tarea:
                    message("lista");
                    return true;
                case R.id.detalle_tarea:
                    message("detalle");
                    return true;
            }
            return false;
        }
    };

    public void listar_Tarea(){
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
                ListAdapterTarea listAdapter = new ListAdapterTarea(tareaList, getApplicationContext(), new ListAdapterTarea.OnItemClickListener() {
                    @Override
                    public void onItemClick(Tarea item) {
                        message("click a la tarea: "+ item.getTitulo());
                        //startActivity(new Intent(getApplicationContext(), Detalle_Tablero.class).putExtra("class_tablero",item));//enviamos los datos datos del tablero a dellate_tablero
                    }
                });
                RecyclerView recyclerView = findViewById(R.id.listRecycleView_tarea);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(listAdapter);
                if(listAdapter.getItemCount()<=0){
                    informacion_tarea.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) { //https://developer.android.com/guide/fragments/appbar?hl=es-419
        MenuItem item = menu.findItem(R.id.favorito);
        getFavorito(item);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_navigation,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_exit:
                startActivity(new Intent(getApplicationContext(), Login_Activity.class));
                finish();
                break;
            case R.id.eliminar:
                eliminar_tablero();
                break;
            case R.id.favorito:
                guardar_favorito(item);
                break;
            case android.R.id.home:
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void message(String mensaje){
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override //click event
    public void onClick(View event) {
        if(btn_agregar_tarea==event){ // https://www.youtube.com/watch?v=Kz9TkDY2sP8
            final EditText titulo= new EditText(Detalle_Tablero.this);
            final EditText descripcion= new EditText(Detalle_Tablero.this);
            titulo.setHint("Título");
            titulo.setMinEms(16);
            titulo.setInputType(InputType.TYPE_CLASS_TEXT);
            descripcion.setHint("Descripción");
            descripcion.setMinEms(16);
            descripcion.setInputType(InputType.TYPE_CLASS_TEXT);
            LinearLayout linearLayout=new LinearLayout(Detalle_Tablero.this);
            linearLayout.setOrientation(linearLayout.VERTICAL);
            linearLayout.addView(titulo);
            linearLayout.addView(descripcion);
            linearLayout.setPadding(70,50,70,10);
            AlertDialog dialog = new AlertDialog.Builder(Detalle_Tablero.this)
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
                listar_Tarea();
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



    public void guardar_favorito(MenuItem item){
        Map<String, Object> map = new HashMap<>();
        favorito=!favorito;
        if(favorito){
            map.put("id_tablero", tablero.getId_tablero());
            map.put("favorito", favorito);
            map.put("idusuario", firebaseAuth.getCurrentUser().getUid());
            firebaseFirestore.collection("favorito").document(tablero.getId_tablero()).set(map);
            item.getIcon().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);
        }else{
            firebaseFirestore.collection("favorito").document(tablero.getId_tablero()).delete();
            item.getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        }
    }

    public void getFavorito(MenuItem item){
        firebaseFirestore.collection("favorito").document(tablero.getId_tablero()).addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if(snapshot.getBoolean("favorito")!=null) {
                    favorito = snapshot.getBoolean("favorito");
                    if (favorito) {
                        item.getIcon().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);
                    } else {
                        item.getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                    }
                }
            }
        });
    }

    public void eliminar_tablero(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Eliminar tablero")
        .setMessage("¿Quieres eliminar el tablero?\nSe eliminaran todas la tareas.\nTablero: "+tablero.getTitulo())
        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseFirestore.collection("tablero").document(tablero.getId_tablero()).delete();
                firebaseFirestore.collection("favorito").document(tablero.getId_tablero()).delete();
                firebaseFirestore.collection("tarea").whereEqualTo("id_tablero", tablero.getId_tablero()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                firebaseFirestore.collection("tarea").document(document.getId()).delete();
                            }
                        }
                    }
                });
                startActivity(new Intent(Detalle_Tablero.this, Menu_Activity.class).putExtra("id_usser",firebaseAuth.getCurrentUser().getUid()));
                finish();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }


}

//db.collection("app").document("users").collection(uid).document("notifications")  colecction/colecction/document/values