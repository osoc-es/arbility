package com.example.admin.administrador.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.administrador.R;
import com.example.admin.administrador.javabean.Centro;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.CentroViewHolder> {

    List<Centro> centros;
    Boolean a;
    String Nombre, Ciudad, Direccion, clave;
    Boolean Validar;

    public Adapter(List<Centro> centros) {
        this.centros = centros;
    }

    @NonNull
    @Override
    public CentroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_centro, parent, false );
        CentroViewHolder holder = new CentroViewHolder( v );
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CentroViewHolder holder, int position) {

        Centro centro = centros.get( position );

        clave = centro.getId();
        Nombre = centro.getNombre();
        Direccion = centro.getDireccion();
        Ciudad = centro.getCiudad();
        Validar = centro.getValidar();

        holder.nombre.setText( Nombre );
        holder.ciudad.setText( Ciudad );
        holder.direccion.setText( Direccion );


    }

    @Override
    public int getItemCount() {
        return centros.size();
    }

    public static class CentroViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        TextView ciudad;
        TextView direccion;
        TextView valido;

        public CentroViewHolder(View itemView) {
            super( itemView );
            nombre = (TextView) itemView.findViewById( R.id.tvNombre );
            ciudad = (TextView) itemView.findViewById( R.id.tvCiudad );
            direccion = (TextView) itemView.findViewById( R.id.tvDireccion );
            valido = (TextView) itemView.findViewById( R.id.tvValido );



        }
    }

}

