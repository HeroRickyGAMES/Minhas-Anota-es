package com.herorickystudiosoficial.minhasanotaes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register_activity extends AppCompatActivity {

    private boolean testMode = false;

    String[] menssagens = {"Preencha todos os campos para continuar", "Cadastro feito com sucesso!"};

    FirebaseFirestore referencia = FirebaseFirestore.getInstance();


    private EditText editNome, editEmail, editSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editNome = findViewById(R.id.editNome);
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);

        checkinternet();

    }

    public void registerbtn(View view){
        String email = editEmail.getText().toString();

        String senha = editSenha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    Snackbar snackbar = Snackbar.make(view, menssagens[1],Snackbar.LENGTH_LONG);
                    snackbar.show();

                    FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();

                    String getUID = usuarioLogado.getUid();

                    String nome = editNome.getText().toString();
                    String email = editEmail.getText().toString();
                    String anotacaouser = "";

                    DocumentReference setDB = referencia.collection("Usuarios").document(getUID);

                    // Create a new user with a first and last name
                    Map<String, Object> user = new HashMap<>();
                    user.put("username", nome);
                    user.put("email", email);
                    user.put("anotacao-0", anotacaouser);
                    user.put("id",getUID);

                    //referencia.collection("Usuarios");

                    setDB.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Erro ao adicionar usuario no banco: " + e);
                        }
                    });

                    //referencia.child(getUID).child("username").setValue(nome);
                    //referencia.child(getUID).child("email").setValue(email);
                    //referencia.child(getUID).child("anotacao").setValue(anotacaouser);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);


                }else{
                    String erro;
                    try {

                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Digite uma senha com no mínimo 6 caracteres!";
                    }catch (FirebaseAuthUserCollisionException e) {

                        erro = "Essa conta já foi criada, crie uma nova conta ou clique em esqueci minha senha na tela de login!";
                    }catch (FirebaseAuthInvalidCredentialsException e){

                        erro = "Seu email está digitado errado, verifique novamente!";

                    }catch (Exception e){
                        erro = "Ao cadastrar usuário!";
                    }
                    Snackbar snackbar = Snackbar.make(view, erro,Snackbar.LENGTH_LONG);
                    snackbar.show();

                }

            }
        });

    }

    public void checkinternet(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){

            System.out.println("Está online!");

        }else{
            System.out.println("Está Offline!");

            Toast.makeText(this, "Você está offline! Por favor, verifique sua conexão com a rede e tente novamente!", Toast.LENGTH_SHORT).show();
            finish();
            System.exit(0);
        }
    }
}