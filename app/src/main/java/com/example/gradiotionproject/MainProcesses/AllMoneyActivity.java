package com.example.gradiotionproject.MainProcesses;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gradiotionproject.ParseSites.AllMoneyAdepter;
import com.example.gradiotionproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AllMoneyActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    private AllMoneyAdepter allMoneyAdepter;
    private RecyclerView recyclerView;

    private ArrayList<String> userAllMoneyFromFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_money);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userAllMoneyFromFB = new ArrayList<>();

        getAllMoneyTypeData();

        recyclerView = findViewById(R.id.recyclerViewAllMoney);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        allMoneyAdepter = new AllMoneyAdepter(userAllMoneyFromFB);
        recyclerView.setAdapter(allMoneyAdepter);
    }

    private void getAllMoneyTypeData() {
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        String s = String.valueOf(documentSnapshot.getData());
                        String[] liveDate = s.split(" ");
                        for (String a :liveDate) {
                            userAllMoneyFromFB.add(a);
                            allMoneyAdepter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(AllMoneyActivity.this, "documentSnapshot Does not exists!!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(AllMoneyActivity.this, "Task Does not exists!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
