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

        /*holder.si.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase databse = FirebaseDatabase.getInstance();
                databse.getReference("Usuarios").child( clave ).child( "validar" ).setValue( true );
                System.out.println( "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" );
                System.out.println( Nombre+"  "+clave );
            }
        } );

        holder.no.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase databse2 = FirebaseDatabase.getInstance();
                databse2.getReference("Usuarios").child( clave ).child( "validar" ).setValue( false );
                System.out.println( "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" );
                System.out.println( Nombre+"  "+clave );
            }
        } );*/
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
        /*Button no;
        Button si;*/

        public CentroViewHolder(View itemView) {
            super( itemView );
            nombre = (TextView) itemView.findViewById( R.id.tvNombre );
            ciudad = (TextView) itemView.findViewById( R.id.tvCiudad );
            direccion = (TextView) itemView.findViewById( R.id.tvDireccion );
            valido = (TextView) itemView.findViewById( R.id.tvValido );
            /*no = (Button) itemView.findViewById( R.id.btnNO );
            si = (Button) itemView.findViewById( R.id.btnSI );*/


        }
    }

}

