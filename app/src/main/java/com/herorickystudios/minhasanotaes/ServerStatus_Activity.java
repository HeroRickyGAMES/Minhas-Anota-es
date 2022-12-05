package com.herorickystudios.minhasanotaes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ServerStatus_Activity extends AppCompatActivity {

    FirebaseFirestore referencia = FirebaseFirestore.getInstance();
    //Data base
    private FirebaseDatabase database;

    FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();


    //referencia para o usuario
    DocumentReference referenciaP = referencia.collection("Servidor").document("saúde-do-servidor");

    private TextView textStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_status);

        textStatus = findViewById(R.id.textStatus);
        getStatus();
    }

    public void getStatus(){

        DocumentReference onScreen =  referencia.collection("Servidor").document("saúde-do-servidor");

        onScreen.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){

                    DocumentSnapshot document = task.getResult();

                    if(document.exists()){
                        String status_do_servidor = document.getString("status-do-servidor");


                        if(status_do_servidor.equals("Em manutenção")){
                            textStatus.setTextColor(Color.rgb(255,255,0));
                            textStatus.setText("Status do servidor: " + status_do_servidor);
                            System.out.println(status_do_servidor);
                        }else if (status_do_servidor.equals("Offline")){
                            textStatus.setTextColor(Color.rgb(255,0,0));
                            textStatus.setText("Status do servidor: " + status_do_servidor);
                            System.out.println(status_do_servidor);
                        }else if (status_do_servidor.equals("Online")){
                            textStatus.setTextColor(Color.rgb(0,255,0));
                            textStatus.setText("Status do servidor: " + status_do_servidor);
                            System.out.println(status_do_servidor);
                        }

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

        onStop();

        super.onBackPressed();
    }
}