package com.gestion.appgestion.Vista_Fragment_Menu;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.gestion.appgestion.Modelo.Tablero;
import com.gestion.appgestion.R;
import com.gestion.appgestion.Utilidades.ListAdapterTablero;
import com.gestion.appgestion.Vistas.Detalle_Tablero;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrimerFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton btn_agregar_tablero;
    List<Tablero> tableroList;
    TextView resultado;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog loadingBar;
    private String id;

    public PrimerFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override //https://www.youtube.com/watch?v=sYHKhwoVU4Q barra de navegación arriba
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_primer, container, false);
        btn_agregar_tablero = view.findViewById(R.id.btn_agregar_tablero);
        btn_agregar_tablero.setOnClickListener(this);
        resultado = view.findViewById(R.id.resultado);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        getActivity().setTitle("Tableros");
        id = firebaseAuth.getCurrentUser().getUid();
        //listar_Tablero(view);
        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        listar_Tablero(getView());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        listar_Tablero(getView());
    }

    public void progress(String mensaje){
        loadingBar=new ProgressDialog(getContext());
        loadingBar.setMessage(mensaje);
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    public void listar_Tablero(View view){//https://www.youtube.com/watch?v=Mne2SrtySME
        tableroList = new ArrayList<>();
        firebaseFirestore.collection("tablero").whereEqualTo("id_usuario", id ).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Tablero tablero = new Tablero();
                        tablero.setId_tablero(document.getId());
                        tablero.setTitulo(document.getString("titulo"));
                        tablero.setFecha_creación("fecha_creacion");
                        tablero.setFavorito(document.getBoolean("favorito"));
                        tablero.setId_usuario(String.valueOf(document.get("id_usuario")));
                        tableroList.add(tablero);
                }
            }}).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    ListAdapterTablero listAdapter = new ListAdapterTablero(tableroList, getContext(), new ListAdapterTablero.OnItemClickListener() {
                        @Override
                        public void onItemClick(Tablero item) {
                            startActivity(new Intent(getContext(), Detalle_Tablero.class).putExtra("class_tablero",item));//enviamos los datos datos del tablero a dellate_tablero
                            getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
                        }
                    });
                    RecyclerView recyclerView = view.findViewById(R.id.listRecycleView_tablero);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(listAdapter);
                    if(listAdapter.getItemCount()<=0){
                        resultado.setVisibility(View.VISIBLE);
                    }
            }});
    }

    public void message(String mensaje){
        Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View event) { // https://stackoverflow.com/questions/3426917/how-to-add-two-edit-text-fields-in-an-alert-dialog
        if(btn_agregar_tablero==event){ // https://www.youtube.com/watch?v=Kz9TkDY2sP8
            final EditText titulo= new EditText(getContext());
            titulo.setHint("Título");
            titulo.setMinEms(16);
            titulo.setInputType(InputType.TYPE_CLASS_TEXT);
            titulo.setFilters( new InputFilter[]{new InputFilter.LengthFilter(50)});
            LinearLayout linearLayout=new LinearLayout(getContext());
            linearLayout.setOrientation(linearLayout.VERTICAL);
            linearLayout.addView(titulo);
            linearLayout.setPadding(70,50,70,10);
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle("Crear Nuevo Tablero")
                    .setPositiveButton("Aceptar",null)
                    .setNegativeButton("Cancelar",null)
                    .setView(linearLayout)
                    .show();
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {
                    String titulo_tablero      = titulo.getText().toString().trim();
                    if(titulo_tablero.isEmpty()){
                        message("Complete todos los campos");
                    }else{
                        registrar_Tablero(titulo_tablero);
                        dialog.dismiss();
                    }
                }
            });
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void registrar_Tablero(String titulo){ //cada tablero pertenece a un usuario
        Map<String, Object> map = new HashMap<>();
        map.put("id_usuario", id);
        map.put("titulo",titulo);
        map.put("fecha_creacion",fecha());
        map.put("favorito",false);
        firebaseFirestore.collection("tablero").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                listar_Tablero(getView());
                message("Se ha creado un tablero.");
            }}).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                message("Error al registrar.");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String fecha(){{
        ZoneId zona = ZoneId.of("America/Lima");
        LocalDateTime localDate = LocalDateTime.now(zona);
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String currentDate = localDate.format(f);
        return currentDate;
    }}
}
/*
Calendar calendar = Calendar.getInstance();
SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
String currentDate = localDate.formato.format(calendar.getTime());
String date = DateFormat.getDateInstance().format(calendar.getTime());
return date;
/*
ZoneId zona = ZoneId.of("America/Lima");
LocalDate localDate = LocalDate.now(zona);
DateTimeFormatter f = DateTimeFormatter.ofPattern("dd-MM-yyyy");
String currentDate = localDate.format(f);
return currentDate;

HH:mm:ss
 */