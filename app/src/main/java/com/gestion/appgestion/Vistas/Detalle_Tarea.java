package com.gestion.appgestion.Vistas;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gestion.appgestion.Modelo.Comprobacion;
import com.gestion.appgestion.Modelo.Tablero;
import com.gestion.appgestion.Modelo.Tarea;
import com.gestion.appgestion.R;
import com.gestion.appgestion.Utilidades.ListAdapterComprobacion;
import com.gestion.appgestion.Utilidades.ListAdapterTablero;
import com.gestion.appgestion.Utilidades.ModalComprobacion;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Detalle_Tarea extends AppCompatActivity implements View.OnClickListener{

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItems;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String[] estado  = {"No iniciada","En curso","Completada"};
    private Tarea tarea;
    private EditText txt_detalle_tarea_fecha_inicio,txt_detalle_tarea_fecha_vencimiento,descripcion_detalle_tarea,titulo_detalle_tarea;
    private Map<String, Object> map = new HashMap<>();
    private List<Comprobacion> comprobacionList;
    private Button btn_modal_comprobacion;

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
        btn_modal_comprobacion = findViewById(R.id.btn_modal_comprobacion);
        btn_modal_comprobacion.setOnClickListener(this);
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
    }

    /*public void list_comprobacion(){
        comprobacionList = new ArrayList<>();
        comprobacionList.add(new Comprobacion("d13"));
        comprobacionList.add(new Comprobacion("123d13"));
        comprobacionList.add(new Comprobacion("d112"));
        comprobacionList.add(new Comprobacion("3d32"));
        ListAdapterComprobacion listAdapter = new ListAdapterComprobacion(comprobacionList,this);
        RecyclerView recyclerView = findViewById(R.id.lista_recicleview_comprobacion);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }*/

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
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
        longitud_texto();
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
            ModalComprobacion modalComprobacion = new ModalComprobacion();
            modalComprobacion.show(getSupportFragmentManager(),"MyFragment");
            //list_comprobacion();
        }
    }
}