package com.gestion.appgestion.Utilidades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gestion.appgestion.Modelo.Comprobacion;
import com.gestion.appgestion.R;
import java.util.List;

public class ListAdapterComprobacion extends RecyclerView.Adapter<ListAdapterComprobacion.ViewHolder>{
    private List<Comprobacion> comprobacionList;
    private LayoutInflater layoutInflater;
    private Context context;

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
        Comprobacion comprobacion  = comprobacionList.get(position);
        holder.bindData(comprobacion);
    }

    @Override
    public int getItemCount() {
        return comprobacionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView titulo_comprobacion;
       ViewHolder(View holder) {
           super(holder);
           titulo_comprobacion = (TextView) holder.findViewById(R.id.titulo_comprobacion);
       }
       void bindData(final Comprobacion comprobacion ){
           titulo_comprobacion.setText(comprobacion.getTitulo());
       }
    }

    public void setItems(List<Comprobacion> items){
        comprobacionList=items;
    }
}
