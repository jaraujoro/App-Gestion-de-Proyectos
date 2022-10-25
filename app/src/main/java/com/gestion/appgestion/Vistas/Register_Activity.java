package com.gestion.appgestion.Vistas;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;



public class Register_Activity extends AppCompatActivity implements  View.OnClickListener{
    private TextInputLayout txtNombre,txtDni,txtEmail,txtPassword,txtNumeroTelefono;
    private Button btnRegistrar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){ //oculta la barra superior
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        txtNombre         = findViewById(R.id.textInputName);
        txtDni            = findViewById(R.id.textInputDni);
        txtNumeroTelefono = findViewById(R.id.textInputMobile);
        txtEmail          = findViewById(R.id.textInputEmail);
        txtPassword       = findViewById(R.id.textInputPassword);
        btnRegistrar      = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public void onLoginClickBack1(View view){ //retrocede actividades con animaciones
        startActivity(new Intent(getApplicationContext(), Login_Activity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
        finishAffinity();
    }

    public void onLoginClickBack2(View view){ //retrocede actividades con animaciones
        startActivity(new Intent(getApplicationContext(), Login_Activity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
        finishAffinity();
    }

    public void message(String mensaje){
        Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
    }


    @Override //evento click botones:
    public void onClick(View view) {
        if(btnRegistrar == view){
            String nombre = txtNombre.getEditText().getText().toString().trim();
            String dni    = txtDni.getEditText().getText().toString().trim();
            String numero_telefono = txtNumeroTelefono.getEditText().getText().toString().trim();
            String email = txtEmail.getEditText().getText().toString().trim();
            String password = txtPassword.getEditText().getText().toString().trim();
            if(nombre.isEmpty() || dni.isEmpty() || numero_telefono.isEmpty() || email.isEmpty() || password.isEmpty()){
                message("Complete todos los campos.");
            }else{
                if(password.length()<=6){
                    message("La contraseña debe ser mayor a 6 caracteres");
                }else {
                    registerUser(nombre, dni, numero_telefono, email, password);
                }
            }
        }

    }

    public void registerUser(String nombre,String dni, String numero_telefono,String email,String password){
        progress("Guardando datos...");
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = firebaseAuth.getCurrentUser().getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", id);
                    map.put("nombre", nombre);
                    map.put("dni", dni);
                    map.put("numerotelefono",numero_telefono);
                    map.put("email", email );
                    map.put("password", password);
                    map.put("photouser", "");
                    firestore.collection("usuario").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            message("Los datos se almacenaron correctamente.");
                            loadingBar.dismiss();
                            firebaseAuth.signOut();
                            startActivity(new Intent(getApplicationContext(), Login_Activity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingBar.dismiss();
                            message("Error al registrar.");
                        }
                    });
                } else {
                    loadingBar.dismiss();
                    message("El correo ingresado ya se encuentra registrado.");
                }
            }
        });
    }

    public void progress(String mensaje){
        loadingBar=new ProgressDialog(this);
        loadingBar.setMessage(mensaje);
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

}
