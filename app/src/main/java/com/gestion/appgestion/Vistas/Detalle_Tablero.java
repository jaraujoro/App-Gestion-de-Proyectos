package com.gestion.appgestion.Vistas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.gestion.appgestion.View_Detalle.Estado_tarea;
import com.gestion.appgestion.Modelo.Tablero;
import com.gestion.appgestion.R;
import com.gestion.appgestion.View_Detalle.Listar_tarea;
import com.gestion.appgestion.Vista_Usser.Login_Activity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;

public class Detalle_Tablero extends AppCompatActivity{

    private Tablero tablero;
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

    @Override
    public void onBackPressed() { //flecha del celular, animación
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
        finish();
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


    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) { //https://developer.android.com/guide/fragments/appbar?hl=es-419
        MenuItem item = menu.findItem(R.id.favorito);
        //getFavorito(item);  //prepara el favorito
        return super.onPrepareOptionsMenu(menu);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //crea la barra de menu superior
        getMenuInflater().inflate(R.menu.top_navigation,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_exit: //cerrar sesion
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login_Activity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
                break;
            case R.id.eliminar: //eliminar tablero
                eliminar_tablero();
                break;
            case R.id.editar_tablero:
                showModal();
                break;
            case R.id.agregar_persona: //favorito tablero
                message("agregar");
                break;
            case android.R.id.home: //regresa al menu principal
                finish();
                overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void message(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    public void showModal(){
        final EditText titulo= new EditText(Detalle_Tablero.this);
        titulo.setHint("Título");
        titulo.setText(tablero.getTitulo());
        titulo.setMinEms(16);
        titulo.setInputType(InputType.TYPE_CLASS_TEXT);
        titulo.setFilters( new InputFilter[]{new InputFilter.LengthFilter(50)});
        LinearLayout linearLayout=new LinearLayout(Detalle_Tablero.this);
        linearLayout.setOrientation(linearLayout.VERTICAL);
        linearLayout.addView(titulo);
        linearLayout.setPadding(70,50,70,10);
        AlertDialog dialog = new AlertDialog.Builder(Detalle_Tablero.this)
                .setTitle("Editar Nombre Tablero")
                .setPositiveButton("Editar",null)
                .setNegativeButton("Cancelar",null)
                .setView(linearLayout)
                .show();
        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positive.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String titulo_tablero = titulo.getText().toString().trim();
                if(titulo_tablero.isEmpty()){
                    message("Complete todos los campos");
                }else{
                    actualizar_Tablero(titulo_tablero);
                    dialog.dismiss();
                }
            }
        });
    }

    public void actualizar_Tablero(String titulo_tablero){
        HashMap<String, Object> map = new HashMap<>();
        map.put("titulo",titulo_tablero);
        firebaseFirestore.collection("tablero").document(tablero.getId_tablero()).update(map);
        listar_tarea.setTitle("Tablero: "+titulo_tablero);
    }
    /*public void guardar_favorito(MenuItem item){
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
    }*/

    /*public void getFavorito(MenuItem item){
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
    }*/

    public void eliminar_tablero(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
        .setIcon(R.drawable.ic_baseline_delete_24_alert)
        .setTitle("Eliminar tablero")
        .setMessage("¿Quieres eliminar el tablero?\nSe eliminaran todas la tareas.\nTablero: "+tablero.getTitulo())
        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseFirestore.collection("tablero").document(tablero.getId_tablero()).delete();
                eliminar_tareas();
                startActivity(new Intent(Detalle_Tablero.this, Menu_Activity.class).putExtra("id_usser",firebaseAuth.getCurrentUser().getUid()));
                finish();
                overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
            }}).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }}).show();
    }

    public void eliminar_tareas(){
        Query query = firebaseFirestore.collection("tarea").whereEqualTo("id_tablero", tablero.getId_tablero());
        ListenerRegistration registration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot document : value) {
                    firebaseFirestore.collection("tarea").document(document.getId()).delete();
                }
            }

        });
        registration.remove();
    }

}

//db.collection("app").document("users").collection(uid).document("notifications")  colecction/colecction/document/values