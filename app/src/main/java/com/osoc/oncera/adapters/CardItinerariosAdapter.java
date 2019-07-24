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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.osoc.oncera.R;
import com.osoc.oncera.javabean.Itinerary;
import com.osoc.oncera.javabean.Teacher;

import java.util.List;

public class CardItinerariosAdapter extends RecyclerView.Adapter<CardItinerariosAdapter.CardItinerarioViewHolder> {
    List<Itinerary> mLista;
    DatabaseReference reference;
    String nombre, descripcion, codigoItinerario, uid, codigoDelCentro, aliasProfesor;

    Itinerary [] iti = new Itinerary[1];
    private Context context;

    private final Teacher[] teacher = new Teacher[1];

    public CardItinerariosAdapter(List<Itinerary> mLista) {
        this.mLista = mLista;
        reference= FirebaseDatabase.getInstance().getReference("Itineraries");
    }

    @NonNull
    @Override
    public CardItinerariosAdapter.CardItinerarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate( R.layout.card_itinerary_data,parent, false);

        CardItinerarioViewHolder holder = new CardItinerarioViewHolder( view );
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardItinerariosAdapter.CardItinerarioViewHolder holder, int position) {

        Itinerary itinerario = mLista.get( position );

        nombre = itinerario.getName();
        codigoItinerario = itinerario.getItineraryCode();
        descripcion = itinerario.getDescription();
        uid = itinerario.getId();
        aliasProfesor = itinerario.getTeacherAlias();
        codigoDelCentro = itinerario.getCode();


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
                                    reference= FirebaseDatabase.getInstance().getReference("Itineraries");

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
            btnEl =(ImageButton) itemView.findViewById( R.id.btnDeleteItinerary);
        }
    }

    public void removeAt(int position){
        mLista.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mLista.size());
    }

    public void obtenerDatosProfesor(){
        reference= FirebaseDatabase.getInstance().getReference("Itineraries");
        Query qq = reference.orderByChild( "teacherAlias" ).equalTo( aliasProfesor ).limitToFirst( 1 );
        qq.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    teacher[0] = dataSnapshot1.getValue( Teacher.class );
                }

                if (teacher[0] != null) {

                    if (teacher[0].getAlias().equals( aliasProfesor ) && aliasProfesor != null) {


                    } else {

                    }

                } else {


                }

                qq.removeEventListener( this );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
