package com.gestion.appgestion.Vistas;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gestion.appgestion.Modelo.Tarea;
import com.gestion.appgestion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Detalle_Tarea extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    String[] estado  = {"No iniciada","En curso","Compleada"};
    private Tarea tarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_tarea);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detalle Tarea");
        tarea = (Tarea) getIntent().getSerializableExtra("class_tarea");
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        autoCompleteTextView = findViewById(R.id.select_progreso);
        adapterItems =  new ArrayAdapter<String>(this,R.layout.list_select_progreso,estado);
        autoCompleteTextView.setAdapter(adapterItems);
        optionSelect();
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String option = adapterView.getItemAtPosition(position).toString();
                saveOptionSelect(option);
            }
        });
    }

    public void optionSelect(){
       firebaseFirestore.collection("tarea").document(tarea.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if(task.isSuccessful()){
                   DocumentSnapshot document = task.getResult();
                   autoCompleteTextView.setText(document.getString("estado"), false);
               }
           }
       });

    }

    public void saveOptionSelect(String option){
        Map<String, Object> map = new HashMap<>();
        map.put("estado", option);
        firebaseFirestore.collection("tarea").document(tarea.getId()).update(map);
    }


    public void message(String mensaje){
        Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //regresa al menu principal
                finish();
                overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() { //flecha del celular, animación
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

}