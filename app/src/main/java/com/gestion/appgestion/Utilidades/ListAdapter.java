package com.gestion.appgestion.Utilidades;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gestion.appgestion.Modelo.Tarea;
import com.gestion.appgestion.R;
import com.google.common.util.concurrent.ListenableFutureTask;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<Tarea> listTarea;
    private LayoutInflater layoutInflater;
    private Context context;
    final ListAdapter.OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(Tarea item);
    }



    public ListAdapter(List<Tarea> itemList, Context context, ListAdapter.OnItemClickListener listener){
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.listTarea = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_elements,null,false);
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_elements, parent, false);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        Tarea tarea  = listTarea.get(position);
        holder.txt_titulo.setText(tarea.getTitulo());
        holder.txt_descripcion.setText(tarea.getDescripcion());
        holder.bindData(listTarea.get(position));
    }

    @Override
    public int getItemCount() {
        return listTarea.size();
    }

    public void setItems(List<Tarea> items){
        listTarea=items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView txt_titulo,txt_descripcion;
       ViewHolder(View view){
           super(view);
           txt_titulo      = (TextView) view.findViewById(R.id.titulo);
           txt_descripcion = (TextView) view.findViewById(R.id.descripcion);
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
