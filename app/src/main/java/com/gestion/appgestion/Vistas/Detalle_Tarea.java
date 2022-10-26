package com.gestion.appgestion.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.gestion.appgestion.Modelo.Tarea;
import com.gestion.appgestion.R;

public class Detalle_Tarea extends AppCompatActivity {
    private Tarea tarea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        tarea = (Tarea) getIntent().getSerializableExtra("class_tarea");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        mostrar_mensaje();
    }

    public void mostrar_mensaje(){
        Toast.makeText(getApplicationContext(),tarea.getId()+" : "+tarea.getFechainicio(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }
}