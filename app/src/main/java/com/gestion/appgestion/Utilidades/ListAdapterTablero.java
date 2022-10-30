package com.gestion.appgestion.Utilidades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gestion.appgestion.Modelo.Tablero;
import com.gestion.appgestion.R;


import java.util.List;

public class ListAdapterTablero extends RecyclerView.Adapter<ListAdapterTablero.ViewHolder> {
    private List<Tablero> tableroList;
    private LayoutInflater layoutInflater;
    private Context context;
    final ListAdapterTablero.OnItemClickListener listener;

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
        Tablero tablero  = tableroList.get(position);
        holder.txt_titulo.setText(tablero.getTitulo());
        holder.bindData(tableroList.get(position));
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
       ViewHolder(View view){
           super(view);
           txt_titulo      = (TextView) view.findViewById(R.id.titulo_tablero);
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
}
