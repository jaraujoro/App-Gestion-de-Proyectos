package com.gestion.appgestion.Vistas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.gestion.appgestion.Modelo.Usuario;
import com.gestion.appgestion.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Menu_Activity extends AppCompatActivity {

    PrimerFragment primero = new PrimerFragment();
    SegundoFragment segundo = new SegundoFragment();
    TerceroFragment tercero = new TerceroFragment();
    private FirebaseAuth firebaseAuth;
    Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(itemSelected);
        firebaseAuth = FirebaseAuth.getInstance();
        if(getIntent()!=null){
            usuario = (Usuario) getIntent().getSerializableExtra("data_usser");
        }
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

    public void loadFragment(Fragment fragment){
        Bundle bundle = new Bundle();
        bundle.putSerializable("data_usser", usuario);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container,fragment);
        transaction.commit();
    }
}