package com.gestion.appgestion.Utilidades;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.gestion.appgestion.Modelo.Tablero;
import com.gestion.appgestion.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.like.LikeButton;
import com.like.OnLikeListener;
import java.util.HashMap;
import java.util.List;

public class ListAdapterTablero extends RecyclerView.Adapter<ListAdapterTablero.ViewHolder>{
    private List<Tablero> tableroList;
    private LayoutInflater layoutInflater;
    private Context context;
    final ListAdapterTablero.OnItemClickListener listener;
    private FirebaseFirestore firebaseFirestore;
    private LikeButton likeButton;

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


    @Override
    public void onBindViewHolder(@NonNull ListAdapterTablero.ViewHolder holder, int position) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        Tablero tablero  = tableroList.get(position);
        //String texto = tablero.getTitulo();
        //texto = Character.toUpperCase(texto.charAt(0)) + texto.substring(1,texto.length());
        //holder.txt_titulo.setText(texto);
        holder.bindData(tablero);
        likeButton.setLiked(tablero.isFavorito());
        //cargar_datos(holder,tablero);
        cargar_datos(tablero,holder);
    }

    /*public void countTareas(ViewHolder holder, Tablero tablero){
        firebaseFirestore.collection("tarea").whereEqualTo("id_tablero", tablero.getId_tablero()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                holder.txt_cantidad_tareas.setText("Tareas("+queryDocumentSnapshots.size()+")");
            }
        });
    }*/

    public void cargar_datos(Tablero tablero, ViewHolder holder){
        DocumentReference docRef = firebaseFirestore.collection("tablero").document(tablero.getId_tablero());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                //String titulo = documentSnapshot.getString("titulo");
                //titulo = Character.toUpperCase(titulo.charAt(0)) + titulo.substring(1,titulo.length());
                holder.txt_titulo.setText(documentSnapshot.getString("titulo"));
                //holder.txt_titulo.setText(documentSnapshot.getString("titulo"));
            }
        });
        firebaseFirestore.collection("tarea").whereEqualTo("id_tablero", tablero.getId_tablero()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                holder.txt_cantidad_tareas.setText("Tareas("+queryDocumentSnapshots.size()+")");
            }
        });
    }


    @Override
    public int getItemCount() {
        return tableroList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView txt_titulo,txt_cantidad_tareas;
       HashMap<String, Object> map = new HashMap<>();
       ViewHolder(View holder) {
           super(holder);
           txt_titulo = (TextView) holder.findViewById(R.id.titulo_tablero);
           txt_cantidad_tareas = (TextView) holder.findViewById(R.id.cantidad_tareas);
           likeButton = (LikeButton) holder.findViewById(R.id.heart_button);
       }
       void bindData(final Tablero item ){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                    //int position = getAdapterPosition();
                    //tableroList.remove(position);
                    //notifyItemRemoved(position);
                }
            });
           likeButton.setOnLikeListener(new OnLikeListener() {
               @Override
               public void liked(LikeButton likeButton) {
                   map.put("favorito", true);
                   firebaseFirestore.collection("tablero").document(item.getId_tablero()).update(map);
               }
               @Override
               public void unLiked(LikeButton likeButton) {
                   map.put("favorito", false);
                   firebaseFirestore.collection("tablero").document(item.getId_tablero()).update(map);
               }
           });
       }
    }

    public void setItems(List<Tablero> items){
        tableroList=items;
    }
}
/*
public void getFavorito(Tablero tablero){
        firebaseFirestore.collection("favorito").document(tablero.getId_tablero()).addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if(snapshot.getBoolean("favorito")!=null) {
                    islike = snapshot.getBoolean("favorito");
                    if (islike) {
                        likeButton.setLiked(islike);
                    }
                }
            }
        });
}
*/

/*
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
*/
/*
public void reference(FirebaseFirestore firebaseFirestore){
        firebaseFirestore.collection("favorito").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getBoolean("favorito")!=null) {
                            boolean favorito = document.getBoolean("favorito");
                            if (favorito) {
                                likeButton.setLiked(true);
                            }
                        }
                    }
                } else {
                    message("Error :" + task);
                }
            }
        });
    }
*/