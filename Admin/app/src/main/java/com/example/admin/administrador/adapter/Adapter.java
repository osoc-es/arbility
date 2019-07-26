package com.example.admin.administrador.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.administrador.MainActivity;
import com.example.admin.administrador.R;
import com.example.admin.administrador.javabean.Centro;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.CentroViewHolder> {

    List<Centro> centros;
    DatabaseReference reference;
    String Nombre, Ciudad, Direccion, clave;
    Boolean Validar;
    Boolean sinValidar;
    private Context context;

    private final Centro[] centro = new Centro[1];

    public Adapter(List<Centro> centros) {

        this.centros = centros;
        reference= FirebaseDatabase.getInstance().getReference("Users");
    }

    @NonNull
    @Override
    public CentroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View v = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_centro, parent, false );
        CentroViewHolder holder = new CentroViewHolder( v );
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.CentroViewHolder holder, final int position) {

        updateInstitution();

        Centro centro = centros.get( position );

        clave = centro.getId();
        Nombre = centro.getNombre();
        Direccion = centro.getDireccion();
        Ciudad = centro.getCiudad();
        Validar = centro.getValidar();

        holder.nombre.setText( Nombre );
        holder.ciudad.setText( Ciudad );
        holder.direccion.setText( Direccion );

        holder.btnNo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder( context );
                builder.setMessage( "¿Deseas Borrar este Centro Escolar?" )
                        .setPositiveButton( "Si", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                centros.remove(centros.get(position).getId());
                                reference.child(centros.get(position).getId()).removeValue();
                                removeAt(position);
                                notifyDataSetChanged();
                            }
                        } ).setNegativeButton( "Cancelar", null );

                AlertDialog alert = builder.create();
                alert.show();
            }

        } );

holder.btnSi.setOnClickListener( new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setMessage( "¿Estña seguro que ese centro es Válido?" )
                .setPositiveButton( "Si", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        sacarValidacion();

                        sinValidar = true;
                    }
                } ).setNegativeButton( "Cancelar", null );

        AlertDialog alert = builder.create();
        alert.show();

    }
} );




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
        Button btnSi;
        Button btnNo;

        public CentroViewHolder(View itemView) {
            super( itemView );
            nombre = (TextView) itemView.findViewById( R.id.tvNombre );
            ciudad = (TextView) itemView.findViewById( R.id.tvCiudad );
            direccion = (TextView) itemView.findViewById( R.id.tvDireccion );
            valido = (TextView) itemView.findViewById( R.id.tvValido );
            btnSi = (Button) itemView.findViewById( R.id.btnSI );
            btnNo = (Button) itemView.findViewById( R.id.btnNO );



        }
    }
    public void removeAt(int position){
        centros.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, centros.size());
    }

    public void sacarValidacion(){
        Query qq4 = reference.orderByChild( "name" ).equalTo( Nombre );

        qq4.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    centro[0] = dataSnapshot1.getValue( Centro.class );
                }

                if (Nombre.equals( centro[0].getNombre() )) {

                    sinValidar = centro[0].getValidar();




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        } );

    }

    public void updateInstitution(){

        {
            reference =  FirebaseDatabase.getInstance().getReference("Users");

            Query qq = reference;

            qq.addValueEventListener( new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    centros.removeAll(centros);
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Centro ctr = dataSnapshot1.getValue( Centro.class );
                        if(ctr.getId()!=null) {

                            Nombre = ctr.getNombre();
                            Ciudad = ctr.getCiudad();
                            Direccion = ctr.getDireccion();
                            Validar = ctr.getValidar();

                        }else{
                            Toast.makeText( context, "No hay centros", Toast.LENGTH_SHORT ).show();
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );
        }
    }

}

