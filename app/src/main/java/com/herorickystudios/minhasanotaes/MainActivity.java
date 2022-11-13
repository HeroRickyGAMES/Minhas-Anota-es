package com.herorickystudios.minhasanotaes;

//Programado por HeroRickyGames

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;


import androidx.navigation.ui.AppBarConfiguration;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.herorickystudios.minhasanotaes.databinding.ActivityMainBinding;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Boolean testMode = false;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    //private AnotacaoPreferencias preferencias;
    private EditText editAnotacao;

    AlertDialog alertDialog;

    private FirebaseDatabase referencia = FirebaseDatabase.getInstance();

    FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

    //Data base
    private FirebaseDatabase database;

    //referencia para o usuario
    private DatabaseReference referenciaP = FirebaseDatabase.getInstance().getReference("Usuarios");


    @RequiresApi(api = 33)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        editAnotacao = findViewById(R.id.editAnotacao);

        alertDialog = new AlertDialog.Builder(MainActivity.this)
//set icon
                .setIcon(R.drawable.ic_baseline_save_alt_24)
//set title
                .setTitle("Por favor espere!")
//set message
                .setMessage("Espere enquanto carregamos informações do Banco de dados")

                .setNegativeButton("Dispensar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 10000);


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

                    referenciaP.child(uid).child("anotacao").setValue(textoRecuperado);

                }
            }
        });

        //Database push
        DatabaseReference reference = referencia.getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String uid = usuario.getUid();

                Log.i("FIREBASE", snapshot.getValue().toString());
                String anotacao = snapshot.child("Usuarios").child(uid).child("anotacao").getValue().toString();

                editAnotacao.setText(anotacao);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("FIREBASE", "Ocorreu um erro ao acessar o banco de dados da aplicação, certifique-se se está tudo certo com o codigo ou a internet" + error);
            }
        });

        //Recuperar Anotação
        //String anotacao =  preferencias.recuperarAnotacao();
        /*String anotacao =  "testee";
        if( !anotacao.equals("") ){
            editAnotacao.setText(anotacao);
        }*/

        checkinternet();


        //Mostra o Interstitial Ad
        StartAppAd.showAd(this);

  /*      if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE)
                == PackageManager.PERMISSION_GRANTED){

        }else{

            Toast.makeText(MainActivity.this, "Habilite as notificações nas configurações para receber as atualizações sobre o servidor", Toast.LENGTH_LONG).show();
            Toast.makeText(MainActivity.this, "ou sobre atualizações do aplicativo!", Toast.LENGTH_LONG).show();
        }*/
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

        if(alertDialog.isShowing()){
            alertDialog.dismiss();
        }


        Intent intent = new Intent(this, Autenticacao_activity.class);
        startActivity(intent);

        super.onRestart();
    }
    @Override
    protected void onPause() {

        if(alertDialog.isShowing()){
            alertDialog.dismiss();
        }

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

            referenciaP.child(uid).child("anotacao").setValue(textoRecuperado);

        }

        super.onPause();
    }

    public void serverStatusbtn(View view){
        Intent intent = new Intent(this, ServerStatus_Activity.class);
        startActivity(intent);
    }
}