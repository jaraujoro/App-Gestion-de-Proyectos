package com.gestion.appgestion;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements  View.OnClickListener{
    TextInputLayout textInputEmail,textInputPassword;
    Button btnLogin;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        textInputEmail    = findViewById(R.id.textInputEmail);
        textInputPassword = findViewById(R.id.textInputPassword);
        btnLogin          = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }
    public void message(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View view) {
        if(btnLogin == view){ //boton para ingresar al sistema, se valida el usuario y contraseña
            String email    = textInputEmail.getEditText().getText().toString();
            String password = textInputPassword.getEditText().getText().toString();
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(getApplicationContext(),"Completar todos los campos", Toast.LENGTH_SHORT).show();
            }else{
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(),"Bienvenido", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(),"Verifique su usuario o contraseña", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}
