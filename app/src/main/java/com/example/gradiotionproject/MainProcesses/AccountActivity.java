package com.example.gradiotionproject.MainProcesses;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gradiotionproject.ParseSites.AccountAdapter;
import com.example.gradiotionproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class AccountActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    private AccountAdapter accountAdapter;
    private RecyclerView recyclerView;

    private Spinner spinner;
    //recycleViewAccount

    private Calendar c;
    Date dc;
    private long milliseconds;
    private long simdikimiliseconds;
    private String simdikiZaman;
    private long gelensaniye;

    private String date;
    private Float money;
    private String type;
    private String email;

    private ArrayList<Float> userMoneyFromFB;
    private ArrayList<String> userMoneyTypeFromFB;
    private ArrayList<String> userDateFromFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        spinner = findViewById(R.id.spinnerTime);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this, R.array.numbersAccount, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        userMoneyFromFB = new ArrayList<>();
        userMoneyTypeFromFB = new ArrayList<>();
        userDateFromFB = new ArrayList<>();

        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        //getDataFromFirestore();
        recyclerView = findViewById(R.id.recycleViewAccount);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        accountAdapter = new AccountAdapter(userMoneyFromFB, userMoneyTypeFromFB, userDateFromFB);
        recyclerView.setAdapter(accountAdapter);
    }

    private void getAllDataFromFirestore() {

        userDateFromFB.removeAll(userDateFromFB);
        userMoneyFromFB.removeAll(userMoneyFromFB);
        userMoneyTypeFromFB.removeAll(userMoneyTypeFromFB);
        accountAdapter.notifyDataSetChanged();
        CollectionReference collectionReference = firebaseFirestore.collection("Movements");
        collectionReference.whereEqualTo("userEmail", email).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(AccountActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(snapshot.getTimestamp("date").toDate());
                        money = Float.parseFloat(data.get("deger").toString());
                        type = (String) data.get("type");
                        userMoneyFromFB.add(money);
                        userMoneyTypeFromFB.add(type);
                        userDateFromFB.add(date);
                        accountAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String timeRange = parent.getItemAtPosition(position).toString();
        switch (timeRange) {
            case "All":
                getAllDataFromFirestore();
                break;
            case "Day":
                getDayDataFromFirestore();
                break;
            case "Week":
                getWeekDataFromFirestore();
                break;
            case "Month":
                getMonthDataFromFirestore();
                break;
            case "Year":
                getYearDataFromFirestore();
                break;
        }
    }

    private void getDayDataFromFirestore() {
        userDateFromFB.removeAll(userDateFromFB);
        userMoneyFromFB.removeAll(userMoneyFromFB);
        userMoneyTypeFromFB.removeAll(userMoneyTypeFromFB);
        accountAdapter.notifyDataSetChanged();
        CollectionReference collectionReference = firebaseFirestore.collection("Movements");
        collectionReference.whereEqualTo("userEmail", email).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(AccountActivity.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();

                        date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(snapshot.getTimestamp("date").toDate());
                        SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

                        SimpleDateFormat bicim2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                        Date tarihSaat = new Date();
                        simdikiZaman = bicim2.format(tarihSaat);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

                        try {
                            Date d = (Date) f.parse(date);
                            milliseconds = d.getTime();

                            Date gun = (Date) simpleDateFormat.parse(simdikiZaman);
                            simdikimiliseconds = gun.getTime();

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (milliseconds >= (simdikimiliseconds - 86400000)) {

                            money = Float.parseFloat(data.get("deger").toString());
                            type = (String) data.get("type");

                            userMoneyFromFB.add(money);
                            userMoneyTypeFromFB.add(type);
                            userDateFromFB.add(date);
                            accountAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }
        });
    }

    private void getWeekDataFromFirestore() {
        userDateFromFB.removeAll(userDateFromFB);
        userMoneyFromFB.removeAll(userMoneyFromFB);
        userMoneyTypeFromFB.removeAll(userMoneyTypeFromFB);
        accountAdapter.notifyDataSetChanged();
        CollectionReference collectionReference = firebaseFirestore.collection("Movements");
        collectionReference.whereEqualTo("userEmail", email).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(AccountActivity.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();

                        date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(snapshot.getTimestamp("date").toDate());
                        SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

                        SimpleDateFormat bicim2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                        Date tarihSaat = new Date();
                        simdikiZaman = bicim2.format(tarihSaat);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

                        try {
                            Date d = (Date) f.parse(date);
                            milliseconds = d.getTime();

                            Date gun = (Date) simpleDateFormat.parse(simdikiZaman);
                            simdikimiliseconds = gun.getTime();

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (milliseconds >= (simdikimiliseconds - 604800000)) {

                            money = Float.parseFloat(data.get("deger").toString());
                            type = (String) data.get("type");

                            userMoneyFromFB.add(money);
                            userMoneyTypeFromFB.add(type);
                            userDateFromFB.add(date);
                            accountAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }
        });
    }

    private void getMonthDataFromFirestore() {
        userDateFromFB.removeAll(userDateFromFB);
        userMoneyFromFB.removeAll(userMoneyFromFB);
        userMoneyTypeFromFB.removeAll(userMoneyTypeFromFB);
        accountAdapter.notifyDataSetChanged();
        CollectionReference collectionReference = firebaseFirestore.collection("Movements");
        collectionReference.whereEqualTo("userEmail", email).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(AccountActivity.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();

                        date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(snapshot.getTimestamp("date").toDate());
                        SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

                        SimpleDateFormat bicim2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                        Date tarihSaat = new Date();
                        simdikiZaman = bicim2.format(tarihSaat);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

                        try {
                            Date d = (Date) f.parse(date);
                            milliseconds = d.getTime();

                            Date gun = (Date) simpleDateFormat.parse(simdikiZaman);
                            simdikimiliseconds = gun.getTime();

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String s = "2592000000";
                        gelensaniye = Long.parseLong(s);
                        if (milliseconds >= (simdikimiliseconds - gelensaniye)) {

                            money = Float.parseFloat(data.get("deger").toString());
                            type = (String) data.get("type");

                            userMoneyFromFB.add(money);
                            userMoneyTypeFromFB.add(type);
                            userDateFromFB.add(date);
                            accountAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }
        });
    }

    private void getYearDataFromFirestore() {

        userDateFromFB.removeAll(userDateFromFB);
        userMoneyFromFB.removeAll(userMoneyFromFB);
        userMoneyTypeFromFB.removeAll(userMoneyTypeFromFB);
        accountAdapter.notifyDataSetChanged();
        CollectionReference collectionReference = firebaseFirestore.collection("Movements");
        collectionReference.whereEqualTo("userEmail", email).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(AccountActivity.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();

                        date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(snapshot.getTimestamp("date").toDate());
                        SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

                        SimpleDateFormat bicim2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                        Date tarihSaat = new Date();
                        simdikiZaman = bicim2.format(tarihSaat);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

                        try {
                            Date d = (Date) f.parse(date);
                            milliseconds = d.getTime();

                            Date gun = (Date) simpleDateFormat.parse(simdikiZaman);
                            simdikimiliseconds = gun.getTime();

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String s = "31536000000";
                        gelensaniye = Long.parseLong(s);

                        if (milliseconds >= (simdikimiliseconds - gelensaniye)) {

                            money = Float.parseFloat(data.get("deger").toString());
                            type = (String) data.get("type");

                            userMoneyFromFB.add(money);
                            userMoneyTypeFromFB.add(type);
                            userDateFromFB.add(date);
                            accountAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}