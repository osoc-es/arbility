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
    String nombre, descripcion, codigoItinerario, uid;
    private Context context;

    public CardItinerariosAdapter(List<Itinerario> mLista) {
        this.mLista = mLista;
        reference= FirebaseDatabase.getInstance().getReference("Itinerarios");
    }

    @NonNull
    @Override
    public CardItinerariosAdapter.CardItinerarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate( R.layout.card_datos_itinerario,parent, false);

        CardItinerarioViewHolder holder = new CardItinerarioViewHolder( view );
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardItinerariosAdapter.CardItinerarioViewHolder holder, int position) {

        Itinerario itinerario = mLista.get( position );

        nombre = itinerario.getNombre();
        codigoItinerario = itinerario.getCodItinerario();
        descripcion = itinerario.getDescripcion();
        uid = itinerario.getId();

        holder.tvNombre.setText( nombre );
        holder.tvDescripcion.setText( descripcion );
        holder.tvCodItinerario.setText( codigoItinerario );

        holder.btnEl.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder( context );
                    builder.setMessage( "¿Seguro qué quieres eliminar este itinerario de la lista?" )
                            .setPositiveButton( "Si", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mLista.remove(mLista.get(position).getId());
                                    reference.child(mLista.get(position).getId()).removeValue();
                                    removeAt(position);
                                    notifyDataSetChanged();
                                }
                            } ).setNegativeButton( "Cancelar", null );

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

         TextView tvNombre;
         TextView tvDescripcion;
         TextView tvCodItinerario;
         ImageButton btnEl;

        public CardItinerarioViewHolder(@NonNull View itemView) {
            super( itemView );
            tvNombre = itemView.findViewById( R.id.tvNombre );
            tvDescripcion = itemView.findViewById( R.id.tvDescripcion );
            tvCodItinerario = itemView.findViewById( R.id.tvCodItinerario );
            btnEl =(ImageButton) itemView.findViewById( R.id.btnEliminarItinerario );
        }
    }

    public void removeAt(int position){
        mLista.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mLista.size());
    }
}
