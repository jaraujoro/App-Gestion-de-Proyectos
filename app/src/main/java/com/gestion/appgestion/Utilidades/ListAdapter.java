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


import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<Tarea> listTarea;
    private LayoutInflater layoutInflater;
    private Context context;

    public ListAdapter(List<Tarea> itemList, Context context){
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.listTarea = itemList;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_elements,null);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        holder.bindData(listTarea.get(position));

    }

    @Override
    public int getItemCount() {
        return listTarea.size();
    }

    public void setItems(List<Tarea> items){
        listTarea=items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
       ImageView imageView;
       TextView titulo,descripcion,estado;
       ViewHolder(View view){
           super(view);
           imageView = view.findViewById(R.id.task_image);
           titulo = view.findViewById(R.id.task_tittle);
           descripcion = view.findViewById(R.id.task_descripcion);
           //estado = view.findViewById(R.id.task_status);
       }
       void bindData(final Tarea item ){
            titulo.setText(item.getTitulo());
            descripcion.setText(item.getDescripcion());

       }
    }
}
