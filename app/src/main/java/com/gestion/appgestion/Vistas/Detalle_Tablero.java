package com.gestion.appgestion.Vistas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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

import com.gestion.appgestion.View_Detalle.Estado_tarea;
import com.gestion.appgestion.Modelo.Tablero;
import com.gestion.appgestion.Modelo.Tarea;
import com.gestion.appgestion.R;
import com.gestion.appgestion.Utilidades.ListAdapterTarea;
import com.gestion.appgestion.View_Detalle.Listar_tarea;
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

public class Detalle_Tablero extends AppCompatActivity{

    private Tablero tablero;
    private boolean favorito = false;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private Estado_tarea estado_tarea = new Estado_tarea();
    private Listar_tarea listar_tarea = new Listar_tarea();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_tablero);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tareas");
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation_tarea);
        navigation.setOnNavigationItemSelectedListener(itemSelected);
        tablero = (Tablero) getIntent().getSerializableExtra("class_tablero");
        listar_tarea = Listar_tarea.newInstance(tablero);
        loadFragment(listar_tarea);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener itemSelected = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.lista_tarea:
                    loadFragment(listar_tarea);
                    return true;
                case R.id.detalle_tarea:
                    loadFragment(estado_tarea);
                    return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container_tarea,fragment);
        transaction.commit();
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
            }}).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }}).show();
    }

}

//db.collection("app").document("users").collection(uid).document("notifications")  colecction/colecction/document/values