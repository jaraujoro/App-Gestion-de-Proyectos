package com.gestion.appgestion.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.gestion.appgestion.Modelo.Tarea;
import com.gestion.appgestion.R;

public class Detalle_Tarea extends AppCompatActivity {
    private Tarea tarea;
    private boolean favorito = false;
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
        Toast.makeText(getApplicationContext(),tarea.getId(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(),"CLICK ELIMINAR", Toast.LENGTH_SHORT).show();
                break;
            case R.id.favorito:
                favorito=!favorito;
                if(favorito){
                    item.getIcon().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);
                }else{
                    favorito=false;
                    item.getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                }
                break;
            case android.R.id.home:
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}