package com.gestion.appgestion.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import com.gestion.appgestion.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Menu_Activity extends AppCompatActivity {

    private PrimerFragment  primero = new PrimerFragment();
    private SegundoFragment segundo = new SegundoFragment();
    private TerceroFragment tercero = new TerceroFragment();
    FirebaseFirestore       firebaseFirestore;
    FirebaseAuth            firebaseAuth;
    private String          id_usser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        firebaseFirestore = FirebaseFirestore.getInstance();
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(itemSelected);
        firebaseAuth = FirebaseAuth.getInstance();
        if(getIntent()!=null){
            id_usser = getIntent().getStringExtra("id_usser");
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
        bundle.putString("id_usser",id_usser);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container,fragment);
        transaction.commit();
    }
}