package com.gestion.appgestion.Vistas;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.gestion.appgestion.Modelo.Usuario;
import com.gestion.appgestion.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;

public class TerceroFragment extends Fragment implements View.OnClickListener {


    private Button btn_loading_photo;
    private CircleImageView photo_preview;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private String storage_path = "user_photo/";
    private static final int COD_SEL_IMAGE = 300;
    private Uri image_url;
    private String photo = "photo";
    private ProgressDialog loadingBar;
    private Usuario usuario = new Usuario();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            usuario = (Usuario) getArguments().getSerializable("data_usser");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_tercero, container, false);
        btn_loading_photo = view.findViewById(R.id.btn_loading_photo);
        photo_preview = view.findViewById(R.id.photo_preview);
        btn_loading_photo.setOnClickListener(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        Toast.makeText(getActivity(),":" + usuario.getNombre(), Toast.LENGTH_SHORT).show();
        loadUsserData();
        return view;
    }

    public void loadUsserData(){
        Picasso.get().load(usuario.getPhoto()).into(photo_preview);
    }

    @Override
    public void onClick(View event) {
        if(btn_loading_photo == event){
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i,COD_SEL_IMAGE);
        }
    }

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

    private void uploadPhoto(Uri image_url) {
        progress("Actualizando foto de perfil....");
        String rute_storage_photo = storage_path+" "+photo+" "+firebaseAuth.getUid();
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
                            String id = firebaseAuth.getUid();
                            String download_uri = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("photo_user", download_uri);
                            firebaseFirestore.collection("usuario").document(id).update(map);
                            Picasso.get().load(usuario.getPhoto()).into(photo_preview);
                            loadingBar.dismiss();
                            message("Foto Actualizada.");
                            /*DocumentReference docRef = firebaseFirestore.collection("usuario").document(id);
                            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                    String url_photo = documentSnapshot.getString("photo_user");
                                    Picasso.get().load(url_photo).into(photo_preview);
                                    loadingBar.dismiss();
                                    message("Foto Actualizada.");
                                }
                            });*/
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error al cargar foto", Toast.LENGTH_SHORT).show();
            }
        });
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