package com.gestion.appgestion.Vista_Usser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.gestion.appgestion.R;
import com.gestion.appgestion.Vistas.Menu_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login_Activity extends AppCompatActivity implements  View.OnClickListener{
    private TextInputLayout textInputEmail,textInputPassword;
    private Button btnLogin;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog loadingBar;
    private String id_usser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        textInputEmail    = findViewById(R.id.textInputEmail);
        textInputPassword = findViewById(R.id.textInputPassword);
        btnLogin          = findViewById(R.id.btnOlvidePassword);
        btnLogin.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            id_usser = firebaseAuth.getCurrentUser().getUid();
            startActivity(new Intent(Login_Activity.this, Menu_Activity.class).putExtra("id_usser",id_usser));
            finish();
        }
    }

    public void progress(String mensaje){
        loadingBar=new ProgressDialog(this);
        loadingBar.setMessage(mensaje);
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    public void OnClickRegister(View view){ //retrocede actividades con animaciones
        startActivity(new Intent(this, Register_Activity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }

    public void OnClickRegister2(View view){ //retrocede actividades con animaciones
        startActivity(new Intent(this, Register_Activity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String email    = textInputEmail.getEditText().getText().toString().trim();
        String password = textInputPassword.getEditText().getText().toString().trim();
        textInputEmail.getEditText().setText(email);
        textInputPassword.getEditText().setText(password);
    }

    public void OnClickForgetPassword(View view){
        startActivity(new Intent(this, Password_Activity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
    }

    public void message(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if(btnLogin == view){
            String email    = textInputEmail.getEditText().getText().toString().trim();
            String password = textInputPassword.getEditText().getText().toString().trim();
            if(email.isEmpty() || password.isEmpty()){
                message("Complete todos los campos.");
            }else{
                progress("Validando datos");
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            id_usser = firebaseAuth.getCurrentUser().getUid();
                            loadingBar.dismiss();
                            overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
                            finishAffinity();
                            startActivity(new Intent(Login_Activity.this, Menu_Activity.class).putExtra("id_usser",id_usser));
                        } else {
                            loadingBar.dismiss();
                            message("Datos incorrectos, Verifique su correo o contraseña");
                        }
                    }
                });
            }
        }
    }

    /*
    public void getDataUsser(String id_usser){
        DocumentReference docRef = firestore.collection("usuario").document(id_usser);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                usuario = new Usuario(
                documentSnapshot.getString("id"),
                documentSnapshot.getString("nombre"),
                documentSnapshot.getString("dni"),
                documentSnapshot.getString("numero_telefono"),
                documentSnapshot.getString("email"),
                documentSnapshot.getString("photo_user"));
                startActivity(new Intent(Login_Activity.this, Menu_Activity.class).putExtra("data_usser",usuario));
                finish();
            }
        });
    }*/
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
    https://www.youtube.com/watch?v=OuvhZTSU5fQ -> 👉 Como Usar el NAVIGATION COMPONENT en ANDROID ❓👈📱 📱
    https://www.youtube.com/watch?v=yCVfML0wflA&t=399s -> obtener datos según el id_usuario
*/
