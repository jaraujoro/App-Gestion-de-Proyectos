package com.gestion.appgestion.Vista_Fragment_Menu;

import static android.app.Activity.RESULT_OK;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;
import com.gestion.appgestion.Modelo.Usuario;
import com.gestion.appgestion.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;

public class TerceroFragment extends Fragment implements View.OnClickListener {

    private Button btn_loading_photo,btnActualizar;
    private CircleImageView photo_preview;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private String storage_path = "user_photo/";
    private static final int COD_SEL_IMAGE = 300;
    private Uri image_url;
    private String photo = "image_user";
    private ProgressDialog loadingBar;
    private TextInputLayout txtNombre,txtDni,txtEmail,txtPassword,txtNumeroTelefono;
    private Usuario usuario;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            usuario = (Usuario) getArguments().getSerializable("usser_class");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_tercero, container, false);
        btn_loading_photo = view.findViewById(R.id.btn_loading_photo);
        photo_preview = view.findViewById(R.id.photo_preview);
        txtNombre         = view.findViewById(R.id.textInputName2);
        txtDni            = view.findViewById(R.id.textInputDni2);
        txtNumeroTelefono = view.findViewById(R.id.textInputMobile2);
        txtEmail          = view.findViewById(R.id.textInputEmail2);
        txtPassword       = view.findViewById(R.id.textInputPassword2);
        btnActualizar     = view.findViewById(R.id.btnActualizar);
        btnActualizar.setOnClickListener(this);
        btn_loading_photo.setOnClickListener(this);
        firebaseFirestore  = FirebaseFirestore.getInstance();
        firebaseAuth       = FirebaseAuth.getInstance();
        storageReference   = FirebaseStorage.getInstance().getReference();
        loadData();
        return view;
    }

    public void loadData(){
        txtNombre.getEditText().setText(usuario.getNombre());
        txtDni.getEditText().setText(usuario.getDni());
        txtEmail.getEditText().setText(usuario.getEmail());
        txtPassword.getEditText().setText(usuario.getPassword());
        txtNumeroTelefono.getEditText().setText(usuario.getNumero_telefono());
        if(!usuario.getPhoto_user().equals("")){
            Picasso.get().load(usuario.getPhoto_user()).into(photo_preview);
        }
    }

    /*public void loadUsserData(){
        DocumentReference docRef = firebaseFirestore.collection("usuario").document(usuario.getId());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(!documentSnapshot.getString("photo_user").equals("")){
                    Picasso.get().load(documentSnapshot.getString("photo_user")).into(photo_preview);
                }
                txtNombre.getEditText().setText(documentSnapshot.getString("nombre"));
                txtDni.getEditText().setText(documentSnapshot.getString("dni"));
                txtEmail.getEditText().setText(documentSnapshot.getString("email"));
                txtPassword.getEditText().setText(documentSnapshot.getString("password"));
                txtNumeroTelefono.getEditText().setText(documentSnapshot.getString("numero_telefono"));
            }
        });
    }*/

    @Override
    public void onClick(View event) {
        if(btn_loading_photo == event){
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i,COD_SEL_IMAGE);
        }
        if(btnActualizar == event){
            uptdateDataUser();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if (requestCode == COD_SEL_IMAGE){
                image_url = data.getData();
                uploadPhoto(image_url);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uptdateDataUser(){
        String nombre = txtNombre.getEditText().getText().toString().trim();
        String dni    = txtDni.getEditText().getText().toString().trim();
        String numero_telefono = txtNumeroTelefono.getEditText().getText().toString().trim();
        String email = txtEmail.getEditText().getText().toString().trim();
        if(nombre.isEmpty() || dni.isEmpty() || numero_telefono.isEmpty() || email.isEmpty()){
            message("No se aceptan datos nulos.");
        }else {
            progress("Actualizando datos....");
            HashMap<String, Object> map = new HashMap<>();
            map.put("nombre", nombre);
            map.put("dni", dni);
            map.put("numero_telefono", numero_telefono);
            map.put("email", email);
            firebaseFirestore.collection("usuario").document(usuario.getId()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    loadingBar.dismiss();
                    message("Datos Actualizados.");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    message("error al actualizar los datos.");
                }
            });
        }
    }

    private String getfileExtension(Uri uri){
        String extension;
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        extension= mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        return extension; //obtener la extención del archivo .*
    }

    public String nombreArchivo(){
        final Calendar c = Calendar.getInstance();
        int anio         = c.get(Calendar.YEAR);
        int mes          = c.get(Calendar.MONTH);
        int dia          = c.get(Calendar.DAY_OF_MONTH);
        int hora         = c.get(Calendar.HOUR_OF_DAY);
        int seg       = c.get(Calendar.SECOND);
        return dia+""+mes+""+anio+"_"+hora+""+seg+"_"+(int) (Math.random()*80+10);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void uploadPhoto(Uri image_url) {
        progress("Actualizando foto de perfil....");
        String rute_storage_photo = storage_path+photo+"_"+nombreArchivo();
        StorageReference riversRef = storageReference.child(rute_storage_photo);
        riversRef.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                if (uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String download_uri = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("photo_user", download_uri);
                            firebaseFirestore.collection("usuario").document(usuario.getId()).update(map);
                            Picasso.get().load(download_uri).into(photo_preview);
                            usuario.setPhoto_user(download_uri);
                            loadingBar.dismiss();
                            message("Foto Actualizada.");
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                message("error al subir la foto");
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        loadData();
    }

    public void message(String mensaje){
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void progress(String mensaje){
        loadingBar=new ProgressDialog(getActivity());
        loadingBar.setMessage(mensaje);
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

}