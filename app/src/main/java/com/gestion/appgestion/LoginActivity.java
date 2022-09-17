package com.gestion.appgestion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gestion.appgestion.Usuario.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.Serializable;


public class LoginActivity extends AppCompatActivity implements  View.OnClickListener{
    TextInputLayout textInputEmail,textInputPassword;
    Button btnLogin;
    TextView forgetpassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private Usuario usuario;
    ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        textInputEmail    = findViewById(R.id.textInputEmail);
        textInputPassword = findViewById(R.id.textInputPassword);
        btnLogin          = findViewById(R.id.btnOlvidePassword);
        forgetpassword    = findViewById(R.id.txtOlvidePassword);
        forgetpassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    public void onLoginClick(View View){ //retrocede actividades con animaciones
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }
    public void message(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    public void progress(String mensaje){
        loadingBar=new ProgressDialog(this);
        loadingBar.setMessage(mensaje);
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    //metodo onclick
    @Override
    public void onClick(View view) {
        if(btnLogin == view){ //metodo para ingresar al sistema, se valida el usuario y contraseña
            String email    = textInputEmail.getEditText().getText().toString();
            String password = textInputPassword.getEditText().getText().toString();
            if(email.isEmpty() || password.isEmpty()){
                message("Completar Todos los campos");
            }else{
                progress("Validando datos");
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String user_id = firebaseAuth.getCurrentUser().getUid();

                            DocumentReference docRef = firestore.collection("users").document(user_id);
                            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                    usuario = new Usuario();
                                    usuario.setNombre(documentSnapshot.getString("nombre"));
                                    usuario.setDni(documentSnapshot.getString("dni"));
                                    usuario.setNumero_telefono(documentSnapshot.getString("numero_telefono"));
                                    usuario.setEmail(documentSnapshot.getString("email"));
                                    usuario.setId(documentSnapshot.getString("id"));
                                    startActivity(new Intent(LoginActivity.this, OlvidePassword.class).putExtra("data", (Serializable) usuario));
                                }
                            });

                            loadingBar.dismiss();
                            message("Bienvenido:");
                        } else {
                            loadingBar.dismiss();
                            message("Datos incorrectos, Verifique su correo o contraseña");
                        }
                    }
                });
            }
        }
        if(forgetpassword==view){ //metodo ólvide contraseña
            startActivity(new Intent(this,OlvidePassword.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        }
    }
}



/*
    Bundle data = new Bundle();
    data.putString("nombre",documentSnapshot.getString("nombre"));
    data.putString("dni",documentSnapshot.getString("dni"));
    data.putString("email",documentSnapshot.getString("email"));
    data.putString("numero_telefono",documentSnapshot.getString("numero_telefono"));
    data.putString("id",documentSnapshot.getString("id"));
    Intent intent = new Intent(this, NextActivity.class);
    intent.putExtras(data);
    startActivity(intent);
*/
