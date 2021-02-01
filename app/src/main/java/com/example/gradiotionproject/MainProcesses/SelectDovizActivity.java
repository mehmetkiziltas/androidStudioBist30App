package com.example.gradiotionproject.MainProcesses;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gradiotionproject.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SelectDovizActivity extends AppCompatActivity implements OnChartGestureListener, OnChartValueSelectedListener {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private static final String TAG = "SelectDovizActivity";
    private TextView DovizAd, DovizAlis, DovizDurum, DovizSatis, kalanPara;
    private LineChart mChart;
    private Context context;

    private String kacDovizAlinacakBilgisi;
    private String kacDovizSatacakBilgisi;
    private String satisYapilacakDoviz;

    private String user_id;

    private Calendar calendar;
    private Date dateOfDay;
    private String dayLongName;
    private int dayLongHours;

    private ProgressBar progressBarUsd;
    private String gelenDegerTipi;
    private String gelendegerdoviz;
    private String yazilacakDoviz;
    private String kacDovizAlinmis;
    String name;
    String email;
    Float money;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_doviz);

        DovizAd = findViewById(R.id.textViewDovizAd);
        DovizAlis = findViewById(R.id.textViewDovizAlis);
        DovizDurum = findViewById(R.id.textViewDovizDurum);
        DovizSatis = findViewById(R.id.textViewDovizSatis);
        kalanPara = findViewById(R.id.dovizKalanPara);

        progressBarUsd = findViewById(R.id.progressBarUsd);

        user_id = firebaseAuth.getCurrentUser().getUid();
        getDataFromFirestore();
        DegerleriGetir();

        DateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        dateOfDay = new Date();
        calendar = Calendar.getInstance();
        dayLongName = dateFormat.format(dateOfDay);
        dayLongHours = calendar.get(Calendar.HOUR_OF_DAY);

        //Line Chart
        mChart = findViewById(R.id.borsaLineChart);
        mChart.setOnChartGestureListener(SelectDovizActivity.this);
        mChart.setOnChartValueSelectedListener(SelectDovizActivity.this);
        mChart.setDragEnabled(true);
        mChart.setSaveEnabled(false);


        ArrayList<Entry> yValues = new ArrayList<>();
        yValues.add(new Entry(0, 60f));
        yValues.add(new Entry(1, 50f));
        yValues.add(new Entry(2, 90f));
        yValues.add(new Entry(3, 10f));
        yValues.add(new Entry(4, 30f));
        yValues.add(new Entry(5, 80f));
        LineDataSet yLineDataSet = new LineDataSet(yValues, (String) DovizAd.getText());
        yLineDataSet.setFillAlpha(110);

        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
        iLineDataSets.add(yLineDataSet);

        LineData data = new LineData(iLineDataSets);
        mChart.setData(data);
    }

    private void DegerleriGetir() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            gelendegerdoviz = extras.getString("dovizAlis");
            gelenDegerTipi = extras.getString("dovizAd");
            DovizAd.setText(extras.getString("dovizAd"));
            DovizAlis.setText(extras.getString("dovizAlis"));
            DovizDurum.setText(extras.getString("degisiklik"));
            DovizSatis.setText(extras.getString("zaman"));

            if (extras.getString("degisiklik").contains("-")) {
                DovizDurum.setTextColor(Color.parseColor("#f00707"));
            } else {
                DovizDurum.setTextColor(Color.parseColor("#34eb52"));
            }
        }
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void BackToSelectDoviz(View view) {
        onBackPressed();
    }

    public void getDataFromFirestore() {

        CollectionReference collectionReference = firebaseFirestore.collection("Users");
        collectionReference.whereEqualTo("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(SelectDovizActivity.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        kacDovizAlinmis = (String) data.get(gelenDegerTipi);
                        yazilacakDoviz = (String) data.get(gelenDegerTipi + "Value");
                        name = (String) data.get("userName");
                        email = (String) data.get("userEmail");
                        money = Float.parseFloat(data.get("totalMoney").toString());
                        type = (String) data.get("moneyType");
                        kalanPara.setText("Bakiye : " + String.valueOf(money) + " " + type + "\n" + gelenDegerTipi + ": " + yazilacakDoviz + " : " + kacDovizAlinmis);
                        kalanPara.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        kalanPara.setTextColor(Color.parseColor("#ca3431"));
                    }
                }
            }
        });
    }

    public void Buyying(View view) {
        if (!dayLongName.equalsIgnoreCase("Sunday") && !dayLongName.equalsIgnoreCase("Saturday")) {
            if (dayLongHours > 8 && dayLongHours < 18) {


                AlertDialog.Builder mydialog = new AlertDialog.Builder(SelectDovizActivity.this);
                mydialog.setTitle("Please write how many stocks you will buy");

                final EditText myEditText = new EditText(SelectDovizActivity.this);
                myEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                mydialog.setView(myEditText);

                mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        kacDovizAlinacakBilgisi = myEditText.getText().toString();

                        if (type.equalsIgnoreCase("TLY")) {
                            try {

                                Double d = (Double) NumberFormat.getNumberInstance(Locale.FRANCE).parse(gelendegerdoviz);
                                if ((money - (Double.valueOf(kacDovizAlinacakBilgisi) * d)) <= 0) {
                                    Toast.makeText(SelectDovizActivity.this, "your money is insufficient", Toast.LENGTH_LONG).show();
                                } else {
                                    Double newMoneyValue = money - (Double.valueOf(kacDovizAlinacakBilgisi) * d);
                                    firebaseUser = firebaseAuth.getCurrentUser();
                                    String userEmail = firebaseUser.getEmail();
                                    HashMap<String, Object> postData = new HashMap<>();
                                    postData.put("userEmail", userEmail);
                                    postData.put("deger", newMoneyValue);
                                    postData.put("type", "TLY");
                                    postData.put(gelenDegerTipi, (Double.valueOf(kacDovizAlinacakBilgisi) * d));
                                    postData.put("date", FieldValue.serverTimestamp());
                                    firebaseFirestore.collection("Movements").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Intent intent = new Intent(SelectDovizActivity.this, AnaActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                    HashMap<String, Object> updataData = new HashMap<>();
                                    updataData.put("totalMoney", newMoneyValue);
                                    updataData.put("moneyType", "TLY");
                                    updataData.put(gelenDegerTipi, kacDovizAlinacakBilgisi);
                                    updataData.put(gelenDegerTipi + "Value", gelendegerdoviz);
                                    firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(updataData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SelectDovizActivity.this, "Alım İşlemi Gerçekleşti.", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(SelectDovizActivity.this, "Failed", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }

                            } catch (NumberFormatException | ParseException exception) {
                                exception.printStackTrace();
                            }
                        } else {
                            Toast.makeText(SelectDovizActivity.this, "You cannot make a purchase because you do not have money!!", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SelectDovizActivity.this, "Not Selected!!", Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                });
                mydialog.show();
            } else {
                Toast.makeText(SelectDovizActivity.this, "Uygun saatler dışında döviz alışı yapamazsınız!!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(SelectDovizActivity.this, "Hafta sonu doviz alışı yapamazsınız!!", Toast.LENGTH_LONG).show();
        }
    }

    public void Selling(View view) {


        if (!dayLongName.equalsIgnoreCase("Sunday") && !dayLongName.equalsIgnoreCase("Saturday")) {
            if (dayLongHours > 8 && dayLongHours < 18) {
                AlertDialog.Builder mySatisdialog = new AlertDialog.Builder(SelectDovizActivity.this);
                mySatisdialog.setTitle("Please write how many stocks you will sell");

                final EditText mySatisEditText = new EditText(SelectDovizActivity.this);
                mySatisEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                mySatisdialog.setView(mySatisEditText);

                mySatisdialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            kacDovizSatacakBilgisi = mySatisEditText.getText().toString();
                            CollectionReference collectionReference = firebaseFirestore.collection("Users");
                            collectionReference.whereEqualTo("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (error != null) {
                                        Toast.makeText(SelectDovizActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    }
                                    if (value != null) {
                                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                                            Map<String, Object> data = snapshot.getData();
                                            satisYapilacakDoviz = String.valueOf(data.get(gelenDegerTipi));
                                            money = Float.parseFloat(data.get("totalMoney").toString());


                                            int satisiOnaylacakDovizSayisi = Integer.valueOf(satisYapilacakDoviz);
                                            int satisiYapilacakHisseSenedi = Integer.valueOf(kacDovizSatacakBilgisi);


                                            if (satisYapilacakDoviz != null && satisiOnaylacakDovizSayisi >= satisiYapilacakHisseSenedi) {
                                                try {
                                                    Double d = (Double) NumberFormat.getNumberInstance(Locale.FRANCE).parse(gelendegerdoviz);
                                                    Double satisYapildiktanSonra = money + (d * satisiYapilacakHisseSenedi);
                                                    HashMap<String, Object> updataData = new HashMap<>();
                                                    updataData.put("totalMoney", satisYapildiktanSonra);
                                                    updataData.put(gelenDegerTipi, (satisiOnaylacakDovizSayisi - satisiYapilacakHisseSenedi));

                                                    firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .update(updataData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                if (satisYapilacakDoviz != null && satisiOnaylacakDovizSayisi == satisiYapilacakHisseSenedi) {
                                                                    HashMap<String, Object> updataData = new HashMap<>();
                                                                    updataData.put(gelenDegerTipi, FieldValue.delete());
                                                                    updataData.put(gelenDegerTipi + "Value", FieldValue.delete());
                                                                    firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                            .update(updataData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Toast.makeText(SelectDovizActivity.this, "Satış İşlemi Gerçekleşti.", Toast.LENGTH_LONG).show();
                                                                                Intent intent = new Intent(SelectDovizActivity.this, AnaActivity.class);
                                                                                startActivity(intent);
                                                                                finish();
                                                                            } else {
                                                                                Toast.makeText(SelectDovizActivity.this, "Failed", Toast.LENGTH_LONG).show();
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            } else {
                                                                Toast.makeText(SelectDovizActivity.this, "Failed", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });

                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                mySatisdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SelectDovizActivity.this, "Not Selected!!", Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                });
                mySatisdialog.show();
            } else {
                Toast.makeText(SelectDovizActivity.this, "Uygun saatler dışında döviz satışı yapamazsınız!!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(SelectDovizActivity.this, "Hafta sonu doviz satışı yapamazsınız!!", Toast.LENGTH_LONG).show();
        }
    }
}