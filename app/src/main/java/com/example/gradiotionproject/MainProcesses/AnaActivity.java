package com.example.gradiotionproject.MainProcesses;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gradiotionproject.LoginProcesse.MainActivity;
import com.example.gradiotionproject.ParseSites.ParseDovizAdepter;
import com.example.gradiotionproject.ParseSites.ParseDovizItem;
import com.example.gradiotionproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class AnaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    private TextView kalanPara;
    private static final String TAG = "AnaActivity";
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private NavigationView navigationView;

    private DovizFragment euroTryFragment;
    private BorsaFragment borsaFragment;

    private RecyclerView recyclerViewDoviz;
    private RecyclerView recyclerViewBorsa;
    private ParseDovizAdepter parseDovizAdepter;
    private ArrayList<ParseDovizItem> parseDovizItems = new ArrayList<>();
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    String name ;
    String email;
    Float money ;
    String type ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        getDataFromFirestore();

        ///Menu
        setContentView(R.layout.activity_ana);
        frameLayout = findViewById(R.id.main_frame);
        bottomNavigationView = findViewById(R.id.main_nav);

        euroTryFragment = new DovizFragment();
        borsaFragment = new BorsaFragment();
        setFragment(euroTryFragment);
        recyclerViewDoviz = findViewById(R.id.recyclerViewDoviz);
        recyclerViewBorsa=findViewById(R.id.recyclerViewBorsa);
        kalanPara = findViewById(R.id.textViewKalanPara);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_usdTry: {
                        setFragment(euroTryFragment);
                        return true;
                    }
                    case R.id.nav_borsa:
                        setFragment(borsaFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
        /////Left Menu
        navigationView = findViewById(R.id.nav_left_menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_allUser: {
                        Intent intentToAllUsers = new Intent(AnaActivity.this, AllUsersActivity.class);
                        startActivity(intentToAllUsers);
                        return true;
                    }
                    case R.id.nav_Logout:
                        firebaseAuth.signOut();
                        Intent intentToLogin = new Intent(AnaActivity.this, MainActivity.class);
                        startActivity(intentToLogin);
                        finish();
                        return true;

                    case R.id.nav_Account:
                        Intent intentToAccount = new Intent(AnaActivity.this, AccountActivity.class);
                        startActivity(intentToAccount);
                        return true;

                    case R.id.nav_changeName:
                        Intent intentToSettings = new Intent(AnaActivity.this, SettingsActivity.class);
                        startActivity(intentToSettings);
                        return true;
                    case R.id.nav_TumBilgiler:
                        Intent intentToTumBilgiler = new Intent(AnaActivity.this, AllMoneyActivity.class);
                        startActivity(intentToTumBilgiler);
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
    public void getDataFromFirestore(){
        CollectionReference collectionReference = firebaseFirestore.collection("Users");
        collectionReference.whereEqualTo("userEmail",FirebaseAuth.getInstance().getCurrentUser().getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(AnaActivity.this,error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String,Object> data = snapshot.getData();
                        name = (String) data.get("userName");
                        email = (String) data.get("userEmail");
                        money = Float.parseFloat(data.get("totalMoney").toString());
                        type = (String) data.get("moneyType");
                        kalanPara.setText("Bakiye: " + String.valueOf(new DecimalFormat("####.####").format(money)) + " " + type);
                        kalanPara.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        kalanPara.setTextColor(Color.parseColor("#ca3431"));
                    }
                }
            }
        });
    }
}