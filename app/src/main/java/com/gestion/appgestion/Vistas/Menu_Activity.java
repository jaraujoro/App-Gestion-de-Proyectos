package com.gestion.appgestion.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import com.gestion.appgestion.Modelo.Usuario;
import com.gestion.appgestion.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Menu_Activity extends AppCompatActivity {

    private PrimerFragment  primero = new PrimerFragment();
    private SegundoFragment segundo = new SegundoFragment();
    private TerceroFragment tercero = new TerceroFragment();
    private FirebaseFirestore  firebaseFirestore;
    private Usuario usuario;
    private String  id_usser;
    private Bundle bundle = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        firebaseFirestore = FirebaseFirestore.getInstance();
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(itemSelected);
        if(getIntent()!=null){
            id_usser = getIntent().getStringExtra("id_usser");
        }
        getDataUsser(id_usser);
        loadFragment(primero);
    }


    private final BottomNavigationView.OnNavigationItemSelectedListener itemSelected = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.primer_fragment:
                    loadFragment(primero);
                    return true;
                case R.id.segundo_fragment:
                    loadFragment(segundo);
                    return true;
                case R.id.tercero_fragment:
                    loadFragment(tercero);
                    return true;
            }
            return false;
        }
    };

    public void getDataUsser(String id_usser){
        firebaseFirestore.collection("usuario").document(id_usser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usuario = new Usuario();
                usuario.setId(documentSnapshot.getString("id"));
                usuario.setNombre(documentSnapshot.getString("nombre"));
                usuario.setDni(documentSnapshot.getString("dni"));
                usuario.setNumerotelefono(documentSnapshot.getString("numerotelefono"));
                usuario.setEmail(documentSnapshot.getString("email"));
                usuario.setPhoto(documentSnapshot.getString("photouser"));
                usuario.setPassword(documentSnapshot.getString("password"));
                bundle.putSerializable("usser_class",usuario);
            }
        });
    }

    public void loadFragment(Fragment fragment){
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container,fragment);
        transaction.commit();
    }
}