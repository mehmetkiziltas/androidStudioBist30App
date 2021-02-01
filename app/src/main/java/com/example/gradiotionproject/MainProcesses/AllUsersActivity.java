package com.example.gradiotionproject.MainProcesses;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gradiotionproject.ParseSites.AllUserAdapter;
import com.example.gradiotionproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class AllUsersActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private ArrayList<String> userNameFromFB;
    private ArrayList<Float> userMoneyFromFB;
    private ArrayList<String> userMoneyTypeFromFB;
    private ArrayList<Float> userKazancYuzdelikFB;

    private static final String TAG = "AllUsersActivity";
    AllUserAdapter userAdapter;
    private RecyclerView recyclerView;

    private Spinner spinnerAllUsers;

    Float kazancYuzdeligi;
    String name;
    String email;
    Float money;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        userNameFromFB = new ArrayList<>();
        userMoneyFromFB = new ArrayList<>();
        userMoneyTypeFromFB = new ArrayList<>();
        userKazancYuzdelikFB = new ArrayList<>();

        spinnerAllUsers = findViewById(R.id.spinnerTimeAllUsers);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this, R.array.numbersAccount, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAllUsers.setAdapter(adapter);
        spinnerAllUsers.setOnItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        getAllDataFromFirestore();

        recyclerView = findViewById(R.id.recyclerViewAllUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new AllUserAdapter(userNameFromFB, userMoneyFromFB, userMoneyTypeFromFB, userKazancYuzdelikFB);
        recyclerView.setAdapter(userAdapter);

    }

    private void getAllDataFromFirestore() {
        userNameFromFB.removeAll(userNameFromFB);
        userMoneyFromFB.removeAll(userMoneyFromFB);
        userMoneyTypeFromFB.removeAll(userMoneyTypeFromFB);
        userKazancYuzdelikFB.removeAll(userKazancYuzdelikFB);
        CollectionReference collectionReference = firebaseFirestore.collection("Users");
        collectionReference.orderBy("totalMoney", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(AllUsersActivity.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {

                        Map<String, Object> data = snapshot.getData();
                        name = (String) data.get("userName");
                        email = (String) data.get("userEmail");
                        money = Float.parseFloat(data.get("totalMoney").toString());
                        kazancYuzdeligi = ((money - 1000) / 100);

                        type = (String) data.get("moneyType");
                        userNameFromFB.add(name);
                        userMoneyFromFB.add(money);
                        userMoneyTypeFromFB.add(type);
                        userKazancYuzdelikFB.add(kazancYuzdeligi);
                        userAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        firebaseFirestore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println("Documant : " + document.getData());
                            }
                        } else {
                            System.out.println("hata");
                        }
                    }
                });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String timeRange = parent.getItemAtPosition(position).toString();
        switch (timeRange) {
            case "All":
                getAllUsersDataFromFirestore();
                break;
            case "Day":
                getDayAllUsersDataFromFirestore();
                break;
            case "Week":
                getWeekAllUsersDataFromFirestore();
                break;
            case "Month":
                getMonthAllUsersDataFromFirestore();
                break;
            case "Year":
                getYearAllUsersDataFromFirestore();
                break;
        }
    }

    private void getYearAllUsersDataFromFirestore() {

    }

    private void getMonthAllUsersDataFromFirestore() {

    }

    private void getWeekAllUsersDataFromFirestore() {

    }

    private void getDayAllUsersDataFromFirestore() {

    }

    private void getAllUsersDataFromFirestore() {
        getAllDataFromFirestore();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}