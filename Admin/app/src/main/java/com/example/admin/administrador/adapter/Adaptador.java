package com.example.admin.administrador.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.administrador.MainActivity;
import com.example.admin.administrador.R;
import com.example.admin.administrador.javabean.Centro;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Adaptador extends RecyclerView.Adapter<Adaptador.CentroViewHolder> {

    Context context;
    ArrayList<Centro> datos;
    MainActivity activity;
    DatabaseReference reference;


    public Adaptador(Context context, ArrayList<Centro> datos) {
        this.context = context;
        this.datos = datos;
        reference= FirebaseDatabase.getInstance().getReference("Usuarios");
    }


    @NonNull
    @Override
    public CentroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_centro, parent,false);


        return new CentroViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador.CentroViewHolder holder, int position) {
        final Centro centro=datos.get(position);
        holder.tvNombre.setText( centro.getNombre() );
        holder.tvCiudad.setText( centro.getCiudad() );
        holder.tvDireccion.setText( centro.getDireccion() );


    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public void clear() {
        datos.clear();
    }

    public class CentroViewHolder extends RecyclerView.ViewHolder{
        private Context context;

        TextView tvNombre;
        TextView tvCiudad;
        TextView tvDireccion;


        public CentroViewHolder(View itemView){
            super(itemView);
            tvNombre=itemView.findViewById( R.id.tvNombre );
            tvCiudad=itemView.findViewById( R.id.tvCiudad );
        }

    }

    public void removeAt(int position){
        datos.remove(position);
        notifyItemChanged(position);
        notifyItemRangeChanged(position, datos.size());
    }
}
