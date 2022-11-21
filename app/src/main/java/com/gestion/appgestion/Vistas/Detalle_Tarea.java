package com.gestion.appgestion.Vistas;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.gestion.appgestion.Modelo.Tarea;
import com.gestion.appgestion.R;
import com.gestion.appgestion.Utilidades.ModalComprobacion;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Detalle_Tarea extends AppCompatActivity implements View.OnClickListener{

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItems;
    private FirebaseFirestore firebaseFirestore;
    private String[] estado  = {"No iniciada","En curso","Completada"};
    private Tarea tarea;
    private EditText txt_detalle_tarea_fecha_inicio,txt_detalle_tarea_fecha_vencimiento,descripcion_detalle_tarea,titulo_detalle_tarea,txt_agregar_comprobacion;
    private Map<String, Object> map = new HashMap<>();
    private Button btn_modal_comprobacion,btn_guardar_comprobacion;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_tarea);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detalle Tarea");
        tarea                     = (Tarea) getIntent().getSerializableExtra("class_tarea");
        firebaseFirestore         = FirebaseFirestore.getInstance();
        autoCompleteTextView      = findViewById(R.id.select_progreso);
        adapterItems              =  new ArrayAdapter<String>(this,R.layout.list_select_progreso,estado);
        descripcion_detalle_tarea = findViewById(R.id.descripcion_detalle_tarea);
        titulo_detalle_tarea      = findViewById(R.id.titulo_detalle_tarea);
        txt_detalle_tarea_fecha_inicio      =  findViewById(R.id.txt_detalle_tarea_fecha_inicio);
        txt_detalle_tarea_fecha_vencimiento =  findViewById(R.id.txt_detalle_tarea_fecha_vencimiento);
        txt_agregar_comprobacion = findViewById(R.id.txt_agregar_comprobacion);
        txt_detalle_tarea_fecha_inicio.setOnClickListener(this);
        txt_detalle_tarea_fecha_vencimiento.setOnClickListener(this);
        btn_modal_comprobacion   = findViewById(R.id.btn_modal_comprobacion);
        btn_modal_comprobacion.setOnClickListener(this);
        btn_guardar_comprobacion = findViewById(R.id.btn_guardar_comprobacion);
        btn_guardar_comprobacion.setOnClickListener(this);
        autoCompleteTextView.setAdapter(adapterItems);
        Cargar_datos();
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String option = adapterView.getItemAtPosition(position).toString();
                saveOptionSelect(option);
            }
        });
        titulo_detalle_tarea.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) { //https://www.youtube.com/watch?v=F26kLIugutA
                if(event.getAction() == KeyEvent.ACTION_UP){
                    map.put("titulo", titulo_detalle_tarea.getText().toString());
                    firebaseFirestore.collection("tarea").document(tarea.getId()).update(map);
                }
                return false;
            }
        });
        descripcion_detalle_tarea.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {  //https://www.youtube.com/watch?v=F26kLIugutA
                if(event.getAction() == KeyEvent.ACTION_UP){
                    map.put("descripcion", descripcion_detalle_tarea.getText().toString());
                    firebaseFirestore.collection("tarea").document(tarea.getId()).update(map);
                }
                return false;
            }
        });
        txt_agregar_comprobacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String comprobacion = txt_agregar_comprobacion.getText().toString().trim();
                btn_guardar_comprobacion.setEnabled(!comprobacion.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void progress(String mensaje){
        loadingBar=new ProgressDialog(this);
        loadingBar.setMessage(mensaje);
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    public void guardar_comprobante(){
        progress("Guardando.....");
        if(!txt_agregar_comprobacion.getText().toString().trim().equals("")){
            map.put("id_tarea", tarea.getId());
            map.put("titulo", txt_agregar_comprobacion.getText().toString());
            map.put("realizado",false);
            map.put("fecha_creacion",new Timestamp(new Date()));
            firebaseFirestore.collection("comprobacion").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    txt_agregar_comprobacion.setText("");
                    message("Se agregó una comprobación.");
                    loadingBar.dismiss();
                }}).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    message("Error al registrar.");
                }
            });
        }
    }

    public void longitud_texto(){
        String length_titulo = titulo_detalle_tarea.getText().toString();
        String length_descripcion = descripcion_detalle_tarea.getText().toString();
        if(length_titulo.length()<=0){
            titulo_detalle_tarea.setText("Sin título");
            map.put("titulo", titulo_detalle_tarea.getText().toString());
            firebaseFirestore.collection("tarea").document(tarea.getId()).update(map);
        }
        if(length_descripcion.length()<=0){
            descripcion_detalle_tarea.setText("Sin Descripción");
            map.put("descripcion", descripcion_detalle_tarea.getText().toString());
            firebaseFirestore.collection("tarea").document(tarea.getId()).update(map);
        }
    }

    public void Cargar_datos(){
       descripcion_detalle_tarea.setText(tarea.getDescripcion());
       titulo_detalle_tarea.setText(tarea.getTitulo());
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
                longitud_texto();
                overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() { //flecha del celular, animación
        super.onBackPressed();
        finish();
        longitud_texto();
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

        if(btn_modal_comprobacion==view){
            ModalComprobacion modalComprobacion = new ModalComprobacion(tarea);
            modalComprobacion.show(getSupportFragmentManager(),"MyFragment");
        }

        if(btn_guardar_comprobacion==view){
            guardar_comprobante();
        }

    }
}