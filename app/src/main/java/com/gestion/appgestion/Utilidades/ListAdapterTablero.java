package com.gestion.appgestion.Utilidades;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.gestion.appgestion.Modelo.Tablero;
import com.gestion.appgestion.R;
import com.gestion.appgestion.Vista_Usser.Register_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListAdapterTablero extends RecyclerView.Adapter<ListAdapterTablero.ViewHolder> {
    private List<Tablero> tableroList;
    private LayoutInflater layoutInflater;
    private Context context;
    final ListAdapterTablero.OnItemClickListener listener;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    boolean favorito = false;

    public interface OnItemClickListener{
        void onItemClick(Tablero item);
    }

    public ListAdapterTablero(List<Tablero> itemList, Context context, ListAdapterTablero.OnItemClickListener listener){
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.tableroList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListAdapterTablero.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_tablero,null,false);
        return new ListAdapterTablero.ViewHolder(view);
    }

    public void message(String message){
        Toast.makeText(context.getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBindViewHolder(@NonNull ListAdapterTablero.ViewHolder holder, int position) {
        Tablero tablero  = tableroList.get(position);
        holder.txt_titulo.setText(tablero.getTitulo());
        holder.bindData(tableroList.get(position));
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        favorito=true;
        getFavorito(holder,tablero);
    }

    @Override
    public int getItemCount() {
        return tableroList.size();
    }

    public void setItems(List<Tablero> items){
        tableroList=items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView txt_titulo,txt_descripcion;
       ImageButton btn_favorito;
       ViewHolder(View holder) {
           super(holder);
           txt_titulo = (TextView) holder.findViewById(R.id.titulo_tablero);
           btn_favorito = (ImageButton) holder.findViewById(R.id.btn_favorito_star);
           btn_favorito.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    int position = getAdapterPosition();
                    Tablero tablero = tableroList.get(position);
                    guardar_favorito(btn_favorito,tablero);
               }
           });
       }
       void bindData(final Tablero item ){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
       }
    }

    public void getFavorito(ListAdapterTablero.ViewHolder holder, Tablero tablero){
        firebaseFirestore.collection("favorito").document(tablero.getId_tablero()).addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if(snapshot.getBoolean("favorito")!=null) {
                    favorito = snapshot.getBoolean("favorito");
                    if (favorito) {
                        holder.btn_favorito.setImageResource(R.drawable.ic_baseline_star_rate_24_select);
                    }
                }
            }
        });
    }

    public void guardar_favorito(ImageButton btn_favorito, Tablero tablero){
        Map<String, Object> map = new HashMap<>();
        favorito=!favorito;
        if(favorito){
            map.put("id_tablero", tablero.getId_tablero());
            map.put("favorito", favorito);
            map.put("idusuario", firebaseAuth.getCurrentUser().getUid());
            firebaseFirestore.collection("favorito").document(tablero.getId_tablero()).set(map);
            btn_favorito.setImageResource(R.drawable.ic_baseline_star_rate_24_select);
            message("Se agregado a favoritos.");
        }else{
            firebaseFirestore.collection("favorito").document(tablero.getId_tablero()).delete();
            btn_favorito.setImageResource(R.drawable.ic_baseline_star_rate_24);
        }
    }


}
