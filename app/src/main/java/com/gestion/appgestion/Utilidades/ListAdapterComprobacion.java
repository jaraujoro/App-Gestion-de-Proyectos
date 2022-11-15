package com.gestion.appgestion.Utilidades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gestion.appgestion.Modelo.Comprobacion;
import com.gestion.appgestion.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class ListAdapterComprobacion extends RecyclerView.Adapter<ListAdapterComprobacion.ViewHolder>{

    private List<Comprobacion> comprobacionList;
    private LayoutInflater layoutInflater;
    private Context context;
    private FirebaseFirestore firebaseFirestore;

    public ListAdapterComprobacion(List<Comprobacion> itemList, Context context){
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.comprobacionList = itemList;
    }

    @NonNull
    @Override
    public ListAdapterComprobacion.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_comprobacion,null,false);
        return new ListAdapterComprobacion.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ListAdapterComprobacion.ViewHolder holder, int position) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        Comprobacion comprobacion  = comprobacionList.get(position);
        holder.btn_check_comprobacion.setChecked(comprobacion.isRealizado());
        holder.bindData(comprobacion);
    }

    @Override
    public int getItemCount() {
        return comprobacionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

       TextView titulo_comprobacion;
       CheckBox btn_check_comprobacion;
       ImageButton btn_eliminar_comprobacion;
       HashMap<String, Object> map = new HashMap<>();

       ViewHolder(View holder) {
           super(holder);
           titulo_comprobacion = (TextView) holder.findViewById(R.id.titulo_comprobacion);
           btn_check_comprobacion = (CheckBox)  holder.findViewById(R.id.btn_check_comprobacion);
           btn_eliminar_comprobacion = (ImageButton) holder.findViewById(R.id.btn_eliminar_comprobacion);
        }
       void bindData(final Comprobacion comprobacion ){
           titulo_comprobacion.setText(comprobacion.getTitulo());
           btn_eliminar_comprobacion.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   int position = getAdapterPosition();
                   comprobacionList.remove(position);
                   firebaseFirestore.collection("comprobacion").document(comprobacion.getId()).delete();
                   notifyItemRemoved(position);
               }
           });
           btn_check_comprobacion.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if(btn_check_comprobacion.isChecked()) {
                       map.put("realizado", true);
                       firebaseFirestore.collection("comprobacion").document(comprobacion.getId()).update(map);
                   }else{
                       map.put("realizado", false);
                       firebaseFirestore.collection("comprobacion").document(comprobacion.getId()).update(map);
                   }
               }
           });
       }
    }

    public void setItems(List<Comprobacion> items){
        comprobacionList=items;
    }

    public void message(String message){
        Toast.makeText(context.getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

}
