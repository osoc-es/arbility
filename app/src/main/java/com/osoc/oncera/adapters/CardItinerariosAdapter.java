package com.osoc.oncera.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.osoc.oncera.R;
import com.osoc.oncera.javabean.Itinerario;

import java.util.List;

public class CardItinerariosAdapter extends RecyclerView.Adapter<CardItinerariosAdapter.CardItinerarioViewHolder> {
    List<Itinerario> mLista;
    DatabaseReference reference;
    private Context context;

    public CardItinerariosAdapter(List<Itinerario> mLista) {
        this.mLista = mLista;
        reference= FirebaseDatabase.getInstance().getReference("Itinerarios");
    }

    @NonNull
    @Override
    public CardItinerariosAdapter.CardItinerarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate( R.layout.card_datos_itinerario,parent, false);

        context=parent.getContext();
        return new CardItinerariosAdapter.CardItinerarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardItinerariosAdapter.CardItinerarioViewHolder holder, int position) {

        final Itinerario itinerario = mLista.get( position );

        String upperString = itinerario.getNombre().substring( 0,1 ).toUpperCase() + itinerario.getNombre().substring( 1 );

        holder.tvNombre.setText( upperString );
        if(itinerario.getDescripcion().trim().equals( "" )){
            holder.tvDescripcion.setText( "Descripcion: No especificada" );
            holder.tvNombre.setText( itinerario.getNombre());
        }else{
            holder.tvDescripcion.setText( itinerario.getDescripcion() );
            holder.tvNombre.setText( itinerario.getNombre());
        }
        holder.btnEl.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder( context );

                builder.setMessage( "¿Seguro qué quieres eliminar este itinerario de la lista?" )
                        .setPositiveButton( "Si", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mLista.remove(mLista.get(position).getId());
                                reference.child( mLista.get( position ).getId()).removeValue();
                                removeAt( position );
                            }
                        }).setNegativeButton( "Cancelar", null );

                AlertDialog alert = builder.create();
                alert.show();
            }
        } );
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }

    public static class CardItinerarioViewHolder extends RecyclerView.ViewHolder{

        public TextView tvNombre;
        public TextView tvDescripcion;
        public ImageButton btnEl;

        public CardItinerarioViewHolder(@NonNull View itemView) {
            super( itemView );
            tvNombre = itemView.findViewById( R.id.tvNombre );
            tvDescripcion = itemView.findViewById( R.id.tvDescripcion );

            btnEl = itemView.findViewById( R.id.btnEliminarIti );
        }
    }

    public void removeAt(int position){
        mLista.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mLista.size());
    }
}
