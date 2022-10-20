package com.gestion.appgestion.Vistas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gestion.appgestion.Modelo.Tarea;
import com.gestion.appgestion.R;
import com.gestion.appgestion.Utilidades.ListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrimerFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton button_float;
    List<Tarea> tareaList;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private ProgressDialog loadingBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init();
    }

    //https://www.youtube.com/watch?v=sYHKhwoVU4Q barra de navegación arriba
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_primer, container, false);
        button_float = view.findViewById(R.id.button_float);
        button_float.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        listTask(view);
        return  view;
    }

    public void progress(String mensaje){
        loadingBar=new ProgressDialog(getContext());
        loadingBar.setMessage(mensaje);
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    public void registerTask(String task, String description){
        progress("Guardando datos...");
        String id = firebaseAuth.getCurrentUser().getUid();
        Map<String, Object> map = new HashMap<>();
        map.put("id_usuario", id);
        map.put("tarea", task);
        map.put("descripcion", description);
        map.put("fecha_inicio","");
        map.put("fecha_fin", "");
        map.put("estado", "");
        firestore.collection("tarea").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                message("Los datos se almacenaron correctamente.");
                loadingBar.dismiss();
            }}).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loadingBar.dismiss();
                    message("Error al registrar.");
                }
            });
    }
    //https://www.youtube.com/watch?v=Mne2SrtySME
    public void listTask(View view){
        tareaList = new ArrayList<>();
        tareaList.add(new Tarea(1,"Test para todos aqueelos quewefjnwefwefjnwenfjewjnwefewwefw ","Pendiente"));
        tareaList.add(new Tarea(2,"Test","Pendiente"));
        tareaList.add(new Tarea(3,"Test","Pendiente"));
        tareaList.add(new Tarea(4,"Test","Pendiente"));
        tareaList.add(new Tarea(5,"Test","Pendiente"));
        tareaList.add(new Tarea(6,"Test","Pendiente"));
        tareaList.add(new Tarea(7,"Test","Pendiente"));
        tareaList.add(new Tarea(8,"Test","Pendiente"));
        tareaList.add(new Tarea(9,"Test","Pendiente"));
        tareaList.add(new Tarea(10,"Test","Pendiente"));
        tareaList.add(new Tarea(11,"Test","Pendiente"));
        tareaList.add(new Tarea(12,"Test","Pendiente"));
        tareaList.add(new Tarea(13,"Test","Pendiente"));
        tareaList.add(new Tarea(14,"Test","Pendiente"));
        tareaList.add(new Tarea(15,"Test","Pendiente"));
        ListAdapter listAdapter = new ListAdapter(tareaList, getContext(), new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Tarea item) {
                message("click al item" + item.getId());
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.listRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapter);
    }

    public void message(String mensaje){
        Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View event) { // https://stackoverflow.com/questions/3426917/how-to-add-two-edit-text-fields-in-an-alert-dialog
        if(button_float==event){
            AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
            builder.setTitle("Agregar Tarea");
            final EditText titulo= new EditText(getContext());
            final EditText descripcion= new EditText(getContext());
            titulo.setHint("Título");
            titulo.setMinEms(16);
            titulo.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            descripcion.setHint("Descripción");
            descripcion.setMinEms(16);
            descripcion.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            LinearLayout linearLayout=new LinearLayout(getContext());
            linearLayout.setOrientation(linearLayout.VERTICAL);
            linearLayout.addView(titulo);
            linearLayout.addView(descripcion);
            linearLayout.setPadding(70,50,70,10);
            builder.setView(linearLayout);
            builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String titulo_tarea      = titulo.getText().toString().trim();
                    String descripcion_tarea = descripcion.getText().toString().trim();
                    if(titulo_tarea.isEmpty() || descripcion_tarea.isEmpty()){
                        message("Complete todos los campos");
                        //builder.
                    }else{
                        registerTask(titulo_tarea, descripcion_tarea);
                    }
                }
            });//https://www.youtube.com/watch?v=Kz9TkDY2sP8 VALID AUTO-CLOSE
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            builder.create().show();//https://www.geeksforgeeks.org/how-to-change-password-of-user-in-android-using-firebase/
        }
    }
}