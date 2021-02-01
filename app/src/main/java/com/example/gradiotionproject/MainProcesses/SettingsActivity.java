package com.example.gradiotionproject.MainProcesses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gradiotionproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Date dc;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    private EditText myChangeNameText;
    private EditText myTotalGainText;
    private Button myButtonName;
    private Button myNewGameButton;
    private String userName;
    private String userEmail;
    private String date;
    private Float money;
    private String type;
    private Calendar c;
    private long milliseconds;
    private long simdikimiliseconds;
    private String simdikiZaman;
    private long gelensaniye;

    private ArrayList<Double> tumKazanclarList;
    private Double tumKazanlarinToplami;


    private Spinner spinnerDegerHesaplama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userEmail = firebaseUser.getEmail();

        myChangeNameText = findViewById(R.id.textViewYeniIsim);
        myTotalGainText = findViewById(R.id.textViewHesaplamaSonucu);
        myButtonName = findViewById(R.id.buttonIsimDegistir);
        myNewGameButton = findViewById(R.id.buttonOyunuSifirla);

        tumKazanclarList = new ArrayList<>();

        spinnerDegerHesaplama = findViewById(R.id.spinnerSettingsTime);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this, R.array.numbersYapilanHesaplamalar, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDegerHesaplama.setAdapter(adapter);
        spinnerDegerHesaplama.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String timeRange = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), timeRange, Toast.LENGTH_LONG).show();
        switch (timeRange) {
            case "All":
                getButunKazancFromFirestore();
                break;
            case "Day":
                getGunlukKazancFromFirestore();
                break;
            case "Week":
                getHaftalikKazancFromFirestore();
                break;
            case "Month":
                getAylikKazancFromFirestore();
                break;
            case "Year":
                getYillikKazancFromFirestore();
                break;
        }

    }

    private void getYillikKazancFromFirestore() {
        CollectionReference collectionReference = firestore.collection("Movements");
        collectionReference.whereEqualTo("userEmail", userEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(SettingsActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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
                            tumKazanclarList.add(Double.valueOf(money));
                        }
                    }
                    if (!tumKazanclarList.isEmpty()) {
                        for (Double i : tumKazanclarList) {
                            tumKazanlarinToplami += i;
                        }
                    }
                }
            }
        });

        if (tumKazanlarinToplami != null) {
            myTotalGainText.setText("" + tumKazanlarinToplami);
        } else
            myTotalGainText.setText("Bu yıl hiç işlem yapmadınız!!");
    }

    private void getAylikKazancFromFirestore() {
        CollectionReference collectionReference = firestore.collection("Movements");
        collectionReference.whereEqualTo("userEmail", userEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(SettingsActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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
                            tumKazanclarList.add(Double.valueOf(money));
                        }
                    }
                    if (!tumKazanclarList.isEmpty()) {
                        for (Double i : tumKazanclarList) {
                            tumKazanlarinToplami += i;
                        }
                    }
                }
            }
        });

        if (tumKazanlarinToplami != null) {
            myTotalGainText.setText("" + tumKazanlarinToplami);
        } else
            myTotalGainText.setText("Bu ay hiç işlem yapmadınız!!");
    }

    private void getHaftalikKazancFromFirestore() {
        CollectionReference collectionReference = firestore.collection("Movements");
        collectionReference.whereEqualTo("userEmail", userEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(SettingsActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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
                            tumKazanclarList.add(Double.valueOf(money));
                        }
                    }
                    if (!tumKazanclarList.isEmpty()) {
                        for (Double i : tumKazanclarList) {
                            tumKazanlarinToplami += i;
                        }
                    }
                }
            }
        });

        if (tumKazanlarinToplami != null) {
            myTotalGainText.setText("" + tumKazanlarinToplami);
        } else
            myTotalGainText.setText("Bu hafta hiç işlem yapmadınız!!");

    }

    private void getGunlukKazancFromFirestore() {

        CollectionReference collectionReference = firestore.collection("Movements");
        collectionReference.whereEqualTo("userEmail", userEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(SettingsActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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
                            tumKazanclarList.add(Double.valueOf(money));
                        }
                    }
                    if (!tumKazanclarList.isEmpty()) {
                        for (Double i : tumKazanclarList) {
                            tumKazanlarinToplami += i;
                        }
                    }
                }
            }
        });

        if (tumKazanlarinToplami != null) {
            myTotalGainText.setText("" + tumKazanlarinToplami);
        } else
            myTotalGainText.setText("Bu gün hiç işlem yapmadınız!!");
    }

    private void getButunKazancFromFirestore() {
        CollectionReference collectionReference = firestore.collection("Movements");
        collectionReference.whereEqualTo("userEmail", userEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(SettingsActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(snapshot.getTimestamp("date").toDate());
                        money = Float.parseFloat(data.get("deger").toString());
                        tumKazanclarList.add(Double.valueOf(money));
                    }
                    if (!tumKazanclarList.isEmpty()) {
                        for (Double i : tumKazanclarList) {
                            tumKazanlarinToplami += i;
                        }
                    }
                }
            }
        });
        if (tumKazanlarinToplami != null) {
            myTotalGainText.setText("" + tumKazanlarinToplami);
        } else
            myTotalGainText.setText("Hiç işlem yapmadınız!!");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void BackToAnaActivity(View view) {
        Intent intent = new Intent(SettingsActivity.this, AnaActivity.class);
        startActivity(intent);
        finish();
    }

    public void ChangeName(View view) {
        String newName = myChangeNameText.getText().toString();
        HashMap<String, Object> updataData = new HashMap<>();
        updataData.put("userName", newName);

        firestore.collection("Users").document(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).update(updataData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingsActivity.this, "İsim Değişimi Gerçekleşti.",
                                    Toast.LENGTH_LONG).show();
                            myChangeNameText.setText("");
                        } else {
                            Toast.makeText(SettingsActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public void YeniOyunBasla(View view) {


        // Firestore dan kullanıcı adını alma işlemi
        CollectionReference collectionReference = firestore.collection("Users");
        collectionReference.whereEqualTo("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(SettingsActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        userName = (String) data.get("userName");
                    }
                }
            }
        });

        //firestore da kullanıcı alanını silme ve güncelleme

        firestore.collection("Users").whereEqualTo("userEmail", FirebaseAuth
                .getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        document.getReference().delete();
                    }
                    HashMap<String, Object> postData = new HashMap<>();
                    postData.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    postData.put("userName", userName);
                    postData.put("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    postData.put("totalMoney", 1000);
                    postData.put("moneyType", "TLY");

                    firestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(postData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SettingsActivity.this, "Yeni Oyununuz Başladı.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(SettingsActivity.this, "Failed!.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        //firestore hareketler alanını silme
        firestore.collection("Movements").whereEqualTo("userEmail", FirebaseAuth
                .getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        document.getReference().delete();
                    }
                }
            }
        });
    }
}