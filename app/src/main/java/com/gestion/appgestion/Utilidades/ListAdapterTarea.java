package com.gestion.appgestion.Utilidades;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.gestion.appgestion.Modelo.Tarea;
import com.gestion.appgestion.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

public class ListAdapterTarea extends RecyclerView.Adapter<ListAdapterTarea.ViewHolder> {
    private List<Tarea> tareaList;
    private LayoutInflater layoutInflater;
    private Context context;
    final ListAdapterTarea.OnItemClickListener listener;
    private FirebaseFirestore firebaseFirestore;

    public interface OnItemClickListener{
        void onItemClick(Tarea item);
    }

    public ListAdapterTarea(List<Tarea> itemList, Context context, ListAdapterTarea.OnItemClickListener listener){
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.tareaList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListAdapterTarea.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_tarea,null,false);
        return new ListAdapterTarea.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterTarea.ViewHolder holder, int position) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        Tarea tarea  = tareaList.get(position);
        holder.txt_titulo.setText(tarea.getTitulo());
        holder.txt_descripcion_tarea.setText(tarea.getDescripcion());
        holder.bindData(tareaList.get(position));
        cargar_datos(tarea,holder);
    }

    public void cargar_datos(Tarea tarea, ListAdapterTarea.ViewHolder holder){
        DocumentReference docRef = firebaseFirestore.collection("tarea").document(tarea.getId());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String estado = documentSnapshot.getString("estado");
                holder.txt_estado_tarea.setText(estado);
                if(estado.equals("No iniciada")){
                    holder.txt_estado_tarea.setTextColor(Color.RED);
                }else if(estado.equals("En curso")){
                    holder.txt_estado_tarea.setTextColor(Color.rgb(255, 165, 0));
                }else if(estado.equals("Completada")){
                    holder.txt_estado_tarea.setTextColor(Color.rgb(19, 173, 9));
                }
            }
        });
    }

    public void message(String message){
        Toast.makeText(context.getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return tareaList.size();
    }

    public void setItems(List<Tarea> items){
        tareaList=items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView txt_titulo,txt_estado_tarea, txt_descripcion_tarea;
       ViewHolder(View view){
           super(view);
           txt_titulo      = (TextView) view.findViewById(R.id.titulo_tarea);
           txt_estado_tarea = (TextView) view.findViewById(R.id.estado_tarea);
           txt_descripcion_tarea = (TextView) view.findViewById(R.id.descripcion_tarea);
       }
       void bindData(final Tarea item ){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
       }
    }
}
