package com.gestion.appgestion.Vistas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gestion.appgestion.Modelo.Tarea;
import com.gestion.appgestion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class Detalle_Tarea extends AppCompatActivity {
    private Tarea tarea;
    private boolean favorito = false;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        tarea = (Tarea) getIntent().getSerializableExtra("class_tarea");
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
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
                startActivity(new Intent(getApplicationContext(),Login_Activity.class));
                finish();
                break;
            case R.id.eliminar:
                deleteTarea();
                break;
            case R.id.favorito:
                saveFavorite(item);
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

    public void saveFavorite(MenuItem item){
        Map<String, Object> map = new HashMap<>();
        favorito=!favorito;
        if(favorito){
            map.put("id_tarea", tarea.getId());
            map.put("favorito", favorito);
            map.put("idusuario", firebaseAuth.getCurrentUser().getUid());
            map.put("titulo", tarea.getTitulo());
            map.put("descripcion", tarea.getDescripcion());
            map.put("fecha_creacion",tarea.getFecha_creacion());
            map.put("fecha_inicio", tarea.getFecha_inicio());
            map.put("fecha_finalizacion", tarea.getFecha_finalizacion());
            map.put("estado", tarea.getEstado());
            firebaseFirestore.collection("favorito").document(tarea.getId()).set(map);
            item.getIcon().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);
        }else{
            firebaseFirestore.collection("favorito").document(tarea.getId()).delete();
            item.getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        }
    }

    public void getFavorito(MenuItem item){
        firebaseFirestore.collection("favorito").document(tarea.getId()).addSnapshotListener( new EventListener<DocumentSnapshot>() {
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
            /*@Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getBoolean("favorito")!=null) {
                    favorito = documentSnapshot.getBoolean("favorito");
                    if (favorito) {
                        item.getIcon().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);
                    } else {
                        item.getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                    }
                }
            }*/
        });
    }

    public void deleteTarea(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Eliminar Tarea")
                .setMessage("¿Quieres eliminar la tarea?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseFirestore.collection("tarea").document(tarea.getId()).delete();
                        firebaseFirestore.collection("favorito").document(tarea.getId()).delete();
                        startActivity(new Intent(Detalle_Tarea.this, Menu_Activity.class).putExtra("id_usser",firebaseAuth.getCurrentUser().getUid()));
                        finish();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

}