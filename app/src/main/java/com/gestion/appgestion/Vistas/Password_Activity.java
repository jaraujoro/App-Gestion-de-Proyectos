package com.gestion.appgestion.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gestion.appgestion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Password_Activity extends AppCompatActivity  implements  View.OnClickListener{
    TextInputLayout txtcorreo;
    Button btnForgetPassword;
    private FirebaseAuth firebaseAuth;
    ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){ //oculta la barra superior
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        firebaseAuth = FirebaseAuth.getInstance();
        txtcorreo       = findViewById(R.id.textInputEmail);
        btnForgetPassword  = findViewById(R.id.btnOlvidePassword);
        btnForgetPassword.setOnClickListener(this);
    }

    public void OnClickLoginBack(View view){ //retrocede actividades con animaciones
        startActivity(new Intent(this, Login_Activity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
        finishAffinity();
    }

    public void OnClickRegisterBack(View view){
        startActivity(new Intent(this, Register_Activity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
        finishAffinity();
    }


    @Override //método onclick
    public void onClick(View view) {
        if(btnForgetPassword == view){
            String email = txtcorreo.getEditText().getText().toString();
            if(!email.isEmpty()){
                sendPasswordResetEmail(email);
            }else{
                Toast.makeText(getApplicationContext(),"Complete todos los campos.",Toast.LENGTH_LONG).show();
            }
        }
    }
    //referencias https://www.geeksforgeeks.org/how-to-change-password-of-user-in-android-using-firebase/
    private void sendPasswordResetEmail(String email) { //evento progress bar y recuperar contraseña
        loadingBar=new ProgressDialog(this);
        loadingBar.setMessage("Enviando Email....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                if(task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Se envio un correo, verifique su bandeja.",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Password_Activity.this, Login_Activity.class));
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Correo no existente, verifique su correo ingresado.",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        });
    }

}