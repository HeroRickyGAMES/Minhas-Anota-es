package com.herorickystudiosoficial.minhasanotaes;

//Programado por HeroRickyGames

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;


import androidx.navigation.ui.AppBarConfiguration;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.herorickystudiosoficial.minhasanotaes.databinding.ActivityMainBinding;
import com.startapp.sdk.adsbase.StartAppAd;

import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Boolean testMode = false;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    //private AnotacaoPreferencias preferencias;
    private EditText editAnotacao;


    FirebaseFirestore referencia = FirebaseFirestore.getInstance();

    FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

    //Data base
    private FirebaseDatabase database;

    FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();

    //referencia para o usuario
    DocumentReference referenciaP = referencia.collection("Usuarios").document(usuarioLogado.getUid());
    //private DatabaseReference referenciaP = FirebaseDatabase.getInstance().getReference("Usuarios");


    @RequiresApi(api = 33)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        editAnotacao = findViewById(R.id.editAnotacao);




        //preferencias = new AnotacaoPreferencias(getApplication());

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoRecuperado = editAnotacao.getText().toString();

                if (textoRecuperado.equals("")) {
                    Snackbar.make(view, "Preencha a anotação!", Snackbar.LENGTH_LONG).show();

                } else {

                    //preferencias.salvarAnotacao( textoRecuperado );

                    Snackbar.make(view, "Anotação Salva com sucesso!", Snackbar.LENGTH_LONG).show();

                    editAnotacao.getText();

                    editAnotacao.setText(textoRecuperado);

                    String uid = usuario.getUid();

                    // Create a new user with a first and last name
                    Map<String, Object> anotacao = new HashMap<>();
                    anotacao.put("anotacao-0", textoRecuperado);


                    referenciaP.update(anotacao).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Erro ao adicionar anotação no banco: " + e);
                        }
                    });

                    //referenciaP.child(uid).child("anotacao").setValue(textoRecuperado);

                }
            }
        });

        //Database push
        //DatabaseReference reference = referencia.getReference();

        DocumentReference onScreen =  referencia.collection("Usuarios").document(usuarioLogado.getUid());

        onScreen.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){
                    DocumentSnapshot document = task.getResult();

                    if(document.exists()){
                        String Anotação = document.getString("anotacao-0");

                        editAnotacao.setText(Anotação);

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        checkinternet();


        //Mostra o Interstitial Ad
        StartAppAd.showAd(this);

    }

    public void checkinternet() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            if (testMode == true) {
                System.out.println("Está online!");
            }
        } else {
            if (testMode == true) {
                System.out.println("Está Offline!");
            }
            Toast.makeText(this, "Você está offline! Por favor, verifique sua conexão com a rede e tente novamente!", Toast.LENGTH_SHORT).show();
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onRestart() {

        Intent intent = new Intent(MainActivity.this, Autenticacao_activity.class);
        startActivity(intent);

        super.onRestart();
    }
    @Override
    protected void onStop() {


        //Quando o aplicativo entra em segundo plano ele executa essa função!
        String textoRecuperado = editAnotacao.getText().toString();

        if(textoRecuperado.equals(""))

        {
            Toast.makeText(this, "Anotação vazia!", Toast.LENGTH_SHORT).show();

        }else

        {

            Toast.makeText(this, "Anotação Salva com sucesso enquanto o app fica em segundo plano!", Toast.LENGTH_SHORT).show();

            editAnotacao.getText();

            editAnotacao.setText(textoRecuperado);

            String uid = usuario.getUid();

            // Create a new user with a first and last name
            Map<String, Object> anotacao = new HashMap<>();
            anotacao.put("anotacao-0", textoRecuperado);


            referenciaP.update(anotacao).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Erro ao adicionar anotação no banco: " + e);
                }
            });

            //referenciaP.child(uid).child("anotacao").setValue(textoRecuperado);

        }

        super.onStop();

    }

    public void serverStatusbtn(View view){

        DocumentReference onScreen =  referencia.collection("Servidor").document("saúde-do-servidor");

        onScreen.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){

                    DocumentSnapshot document = task.getResult();

                    if(document.exists()){
                        String status_do_servidor = document.getString("status-do-servidor");

                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Status do Servidor")
                                .setMessage("Status do Servidor: " + status_do_servidor)
                                .setCancelable(true)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}