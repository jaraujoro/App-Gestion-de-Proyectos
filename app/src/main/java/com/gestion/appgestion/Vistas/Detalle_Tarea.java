package com.gestion.appgestion.Vistas;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.gestion.appgestion.Modelo.Tarea;
import com.gestion.appgestion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Detalle_Tarea extends AppCompatActivity implements View.OnClickListener{

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItems;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String[] estado  = {"No iniciada","En curso","Completada"};
    private Tarea tarea;
    private TextInputLayout descripcion_detalle_tarea,titulo_detalle_tarea;
    private EditText txt_detalle_tarea_fecha_inicio,txt_detalle_tarea_fecha_vencimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_tarea);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detalle Tarea");
        tarea                     = (Tarea) getIntent().getSerializableExtra("class_tarea");
        firebaseFirestore         = FirebaseFirestore.getInstance();
        firebaseAuth              = FirebaseAuth.getInstance();
        autoCompleteTextView      = findViewById(R.id.select_progreso);
        adapterItems              =  new ArrayAdapter<String>(this,R.layout.list_select_progreso,estado);
        descripcion_detalle_tarea = findViewById(R.id.descripcion_detalle_tarea);
        titulo_detalle_tarea      = findViewById(R.id.titulo_detalle_tarea);
        txt_detalle_tarea_fecha_inicio      =  findViewById(R.id.txt_detalle_tarea_fecha_inicio);
        txt_detalle_tarea_fecha_vencimiento =  findViewById(R.id.txt_detalle_tarea_fecha_vencimiento);
        txt_detalle_tarea_fecha_inicio.setOnClickListener(this);
        txt_detalle_tarea_fecha_vencimiento.setOnClickListener(this);
        autoCompleteTextView.setAdapter(adapterItems);
        Cargar_datos();
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String option = adapterView.getItemAtPosition(position).toString();
                saveOptionSelect(option);
            }
        });
    }

    public void Cargar_datos(){
       descripcion_detalle_tarea.getEditText().setText(tarea.getDescripcion());
       titulo_detalle_tarea.getEditText().setText(tarea.getTitulo());
       txt_detalle_tarea_fecha_vencimiento.setText(tarea.getFecha_finalizacion());
       txt_detalle_tarea_fecha_inicio.setText(tarea.getFecha_inicio());
       autoCompleteTextView.setText(tarea.getEstado(),false);
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

    @Override
    public void onClick(View view) {
        if(txt_detalle_tarea_fecha_inicio==view){
            final Calendar c = Calendar.getInstance();
            int anio         = c.get(Calendar.YEAR);
            int mes          = c.get(Calendar.MONTH);
            int dia          = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(Detalle_Tarea.this,new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int anio, int mes, int dia) {
                    txt_detalle_tarea_fecha_inicio.setText(dia + "-" + (mes + 1) + "-" + anio);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("fecha_inicio",txt_detalle_tarea_fecha_inicio.getText().toString());
                    firebaseFirestore.collection("tarea").document(tarea.getId()).update(map);
                }},anio, mes, dia);
            datePickerDialog.show();
        }
        if(txt_detalle_tarea_fecha_vencimiento==view){
            final Calendar c = Calendar.getInstance();
            int anio         = c.get(Calendar.YEAR);
            int mes          = c.get(Calendar.MONTH);
            int dia          = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(Detalle_Tarea.this,new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int anio,int mes, int dia) {
                    txt_detalle_tarea_fecha_vencimiento.setText(dia + "-" + (mes + 1) + "-" + anio);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("fecha_finalizacion",txt_detalle_tarea_fecha_vencimiento.getText().toString());
                    firebaseFirestore.collection("tarea").document(tarea.getId()).update(map);
                }},anio, mes, dia);
            datePickerDialog.show();
        }
    }
}