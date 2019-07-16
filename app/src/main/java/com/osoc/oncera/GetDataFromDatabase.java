package com.osoc.oncera;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public final class GetDataFromDatabase {

    private static DatabaseReference mDatabase;
    private static float f;
    private static String s;
    private static boolean b;

    public static float FloatData(String path)
    {

        mDatabase = FirebaseDatabase.getInstance().getReference(path);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                f = dataSnapshot.getValue(float.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        return f;
    }

    public static String StringData(String path)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference(path);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                s = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        return s;
    }

    public static boolean BooleanData(String path)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference(path);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                b = dataSnapshot.getValue(Boolean.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        return b;
    }

}
