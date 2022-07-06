package com.herorickystudios.minhasanotaes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unity3d.services.banners.UnityBanners;

public class ServerStatus_Activity extends AppCompatActivity {

    private FirebaseDatabase referencia = FirebaseDatabase.getInstance();

    FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

    //Data base
    private FirebaseDatabase database;

    //referencia para o usuario
    private DatabaseReference referenciaP = FirebaseDatabase.getInstance().getReference("Status_do_Servidor");

    private TextView textStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_status);

        textStatus = findViewById(R.id.textStatus);
        getStatus();
    }

    public void getStatus(){
        DatabaseReference reference = referencia.getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String uid = usuario.getUid();
                String status_do_servidor = snapshot.child("Status_do_Servidor").getValue().toString();

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("FIREBASE", "Ocorreu um erro ao acessar o banco de dados da aplicação, certifique-se se está tudo certo com o codigo ou a internet" + error);
            }
        });


    }
}