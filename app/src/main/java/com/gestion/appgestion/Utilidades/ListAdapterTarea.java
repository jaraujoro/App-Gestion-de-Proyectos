package com.gestion.appgestion.Utilidades;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gestion.appgestion.Modelo.Tarea;
import com.gestion.appgestion.R;
import java.util.List;

public class ListAdapterTarea extends RecyclerView.Adapter<ListAdapterTarea.ViewHolder> {
    private List<Tarea> tareaList;
    private LayoutInflater layoutInflater;
    private Context context;
    final ListAdapterTarea.OnItemClickListener listener;

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
        Tarea tarea  = tareaList.get(position);
        holder.txt_titulo.setText(tarea.getTitulo());
        holder.txt_estado_tarea.setText(tarea.getEstado());
        if(tarea.getEstado().equals("Pendiente")){
            holder.txt_estado_tarea.setTextColor(Color.RED);
        }else if(tarea.getEstado().equals("Progreso")){
            holder.txt_estado_tarea.setTextColor(Color.rgb(255, 165, 0));
        }else if(tarea.getEstado().equals("Completado")){
            holder.txt_estado_tarea.setTextColor(Color.rgb(19, 173, 9));
        }
        holder.bindData(tareaList.get(position));
    }

    @Override
    public int getItemCount() {
        return tareaList.size();
    }

    public void setItems(List<Tarea> items){
        tareaList=items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView txt_titulo,txt_estado_tarea;
       ViewHolder(View view){
           super(view);
           txt_titulo      = (TextView) view.findViewById(R.id.titulo_tarea);
           txt_estado_tarea = (TextView) view.findViewById(R.id.estado_tarea);
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
