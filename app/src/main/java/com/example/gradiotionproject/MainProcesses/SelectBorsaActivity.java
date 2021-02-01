package com.example.gradiotionproject.MainProcesses;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SelectBorsaActivity extends AppCompatActivity implements OnChartGestureListener, OnChartValueSelectedListener {

    private TextView textViewBHisse, textViewBSon, textViewBDun, textViewBYuzde, kalanPara;
    private TextView textViewBYuksek, textViewBDusuk, textViewBHacimLot, textViewBhacimTl;
    private Button btnBAl, btnBSat;
    private LineChart mylineChart;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;


    private String user_id;
    private String satisYapilacakHisseSenedi;


    private Calendar calendar;
    private Date dateOfDay;
    private String dayLongName;
    private int dayLongHours;

    private String gelenDeger;
    private String gelenHisseAdi;
    private String kacHisseSenenediAlinacakBilgisi;
    private String kacHisseSenediSatacakBilgisi;

    String hisseyiKacaAldigininBilgisi;
    String kacHisseAlinmis;
    String name;
    String email;
    Float money;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getDataFromFirestore();

        user_id = firebaseAuth.getCurrentUser().getUid();

        setContentView(R.layout.activity_select_borsa);

        textViewBHisse = findViewById(R.id.textViewBorsaAdi);
        textViewBSon = findViewById(R.id.textViewBorsaSon);
        textViewBDun = findViewById(R.id.textViewBorsaDun);
        textViewBYuzde = findViewById(R.id.textViewBorsaYuzde);
        textViewBYuksek = findViewById(R.id.textViewBorsaYuksek);
        textViewBDusuk = findViewById(R.id.textViewBorsaDusuk);
        textViewBHacimLot = findViewById(R.id.textViewBorsaHacimLot);
        textViewBhacimTl = findViewById(R.id.textViewBorsaHacimTl);
        kalanPara = findViewById(R.id.kalanPara);

        btnBAl = findViewById(R.id.btnBorsaAl);
        btnBSat = findViewById(R.id.btnBorsaSat);
        degerleriGetir();

        DateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        dateOfDay = new Date();
        calendar = Calendar.getInstance();
        dayLongName = dateFormat.format(dateOfDay);
        dayLongHours = calendar.get(Calendar.HOUR_OF_DAY);


        //Line Chart
        mylineChart = findViewById(R.id.borsaLineChart);
        mylineChart.setOnChartGestureListener(SelectBorsaActivity.this);
        mylineChart.setOnChartValueSelectedListener(SelectBorsaActivity.this);
        mylineChart.setDragEnabled(true);
        mylineChart.setSaveEnabled(false);


        ArrayList<Entry> yValues = new ArrayList<>();
        yValues.add(new Entry(0, 60f));
        yValues.add(new Entry(1, 50f));
        yValues.add(new Entry(2, 90f));
        yValues.add(new Entry(3, 10f));
        yValues.add(new Entry(4, 30f));
        yValues.add(new Entry(5, 80f));
        LineDataSet yLineDataSet = new LineDataSet(yValues, (String) textViewBHisse.getText());
        yLineDataSet.setFillAlpha(110);

        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
        iLineDataSets.add(yLineDataSet);

        LineData data = new LineData(iLineDataSets);
        mylineChart.setData(data);
    }

    private void degerleriGetir() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            gelenDeger = bundle.getString("son");
            gelenHisseAdi = bundle.getString("hisseAdi");
            textViewBHisse.setText("   Hisse Adı :  " + bundle.getString("hisseAdi"));
            textViewBSon.setText("   Son Surum    :  " + bundle.getString("son"));
            textViewBDun.setText("   Dün   :  " + bundle.getString("dun"));
            textViewBYuzde.setText("   Yüzde    :  " + bundle.getString("yuzde"));
            textViewBYuksek.setText("   En Yüksek    :  " + bundle.getString("yuksek"));
            textViewBDusuk.setText("   En Düşük   :  " + bundle.getString("dusuk"));
            textViewBHacimLot.setText("   Hacim Lot    :  " + bundle.getString("lot"));
            textViewBhacimTl.setText("   Hacim Tl    :  " + bundle.getString("tly"));
            btnBAl = findViewById(R.id.btnBorsaAl);
            btnBSat = findViewById(R.id.btnBorsaSat);
            kalanPara.setText(String.valueOf(money));
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

    public void BackToSelectBorsa(View view) {
        onBackPressed();
    }

    public void borsaAlisYap(View view) {
        if (!dayLongName.equalsIgnoreCase("Sunday") && !dayLongName.equalsIgnoreCase("Saturday")) {
            if (dayLongHours > 8 && dayLongHours < 18) {

                AlertDialog.Builder mydialog = new AlertDialog.Builder(SelectBorsaActivity.this);
                mydialog.setTitle("Please write how many stocks you will buy");

                final EditText myEditText = new EditText(SelectBorsaActivity.this);
                myEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                mydialog.setView(myEditText);

                mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        kacHisseSenenediAlinacakBilgisi = myEditText.getText().toString();

                        if (type.equalsIgnoreCase("TLY")) {
                            try {

                                Double d = (Double) NumberFormat.getNumberInstance(Locale.FRANCE).parse(gelenDeger);
                                if ((money - (Double.valueOf(kacHisseSenenediAlinacakBilgisi) * d)) <= 0) {
                                    Toast.makeText(SelectBorsaActivity.this, "your money is insufficient", Toast.LENGTH_LONG).show();
                                } else {
                                    Double newMoneyValue = money - (Double.valueOf(kacHisseSenenediAlinacakBilgisi) * d);
                                    firebaseUser = firebaseAuth.getCurrentUser();
                                    String userEmail = firebaseUser.getEmail();
                                    HashMap<String, Object> postData = new HashMap<>();
                                    postData.put("userEmail", userEmail);
                                    postData.put("deger", newMoneyValue);
                                    postData.put("type", "TLY");
                                    postData.put(gelenHisseAdi, (Double.valueOf(kacHisseSenenediAlinacakBilgisi) * d));
                                    postData.put("date", FieldValue.serverTimestamp());
                                    firebaseFirestore.collection("Movements").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Intent intent = new Intent(SelectBorsaActivity.this, AnaActivity.class);
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
                                    updataData.put(gelenHisseAdi, kacHisseSenenediAlinacakBilgisi);
                                    updataData.put(gelenHisseAdi + "Value", gelenDeger);
                                    firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(updataData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SelectBorsaActivity.this, "Alım İşlemi Gerçekleşti.", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(SelectBorsaActivity.this, "Failed", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }

                            } catch (NumberFormatException | ParseException exception) {
                                exception.printStackTrace();
                            }
                        } else {
                            Toast.makeText(SelectBorsaActivity.this, "You cannot make a purchase because you do not have money!!", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SelectBorsaActivity.this, "Not Selected!!", Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                });
                mydialog.show();
            } else {
                Toast.makeText(SelectBorsaActivity.this, "Uygun saatler dışında borsa alışı yapamazsınız!!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(SelectBorsaActivity.this, "Hafta sonu borsa alışı yapamazsınız!!", Toast.LENGTH_LONG).show();
        }
    }

    public void borsaSatisYap(View view) {

        if (!dayLongName.equalsIgnoreCase("Sunday") && !dayLongName.equalsIgnoreCase("Saturday")) {
            if (dayLongHours > 8 && dayLongHours < 18) {
                AlertDialog.Builder mySatisdialog = new AlertDialog.Builder(SelectBorsaActivity.this);
                mySatisdialog.setTitle("Please write how many stocks you will sell");

                final EditText mySatisEditText = new EditText(SelectBorsaActivity.this);
                mySatisEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                mySatisdialog.setView(mySatisEditText);

                mySatisdialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            kacHisseSenediSatacakBilgisi = mySatisEditText.getText().toString();
                            CollectionReference collectionReference = firebaseFirestore.collection("Users");
                            collectionReference.whereEqualTo("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (error != null) {
                                        Toast.makeText(SelectBorsaActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    }
                                    if (value != null) {
                                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                                            Map<String, Object> data = snapshot.getData();
                                            satisYapilacakHisseSenedi = String.valueOf(data.get(gelenHisseAdi));
                                            money = Float.parseFloat(data.get("totalMoney").toString());


                                            int satisiOnaylacakHisseSenediSayisi = Integer.valueOf(satisYapilacakHisseSenedi);
                                            int satisiYapilacakHisseSenedi = Integer.valueOf(kacHisseSenediSatacakBilgisi);


                                            if (satisYapilacakHisseSenedi != null && satisiOnaylacakHisseSenediSayisi >= satisiYapilacakHisseSenedi) {
                                                try {
                                                    Double d = (Double) NumberFormat.getNumberInstance(Locale.FRANCE).parse(gelenDeger);
                                                    Double satisYapildiktanSonra = money + (d * satisiYapilacakHisseSenedi);
                                                    HashMap<String, Object> updataData = new HashMap<>();
                                                    updataData.put("totalMoney", satisYapildiktanSonra);
                                                    updataData.put(gelenHisseAdi, (satisiOnaylacakHisseSenediSayisi - satisiYapilacakHisseSenedi));

                                                    firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .update(updataData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                if (satisYapilacakHisseSenedi != null && satisiOnaylacakHisseSenediSayisi == satisiYapilacakHisseSenedi) {
                                                                    HashMap<String, Object> updataData = new HashMap<>();
                                                                    updataData.put(gelenHisseAdi, FieldValue.delete());
                                                                    updataData.put(gelenHisseAdi + "Value", FieldValue.delete());
                                                                    firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                            .update(updataData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Toast.makeText(SelectBorsaActivity.this, "Satış İşlemi Gerçekleşti.", Toast.LENGTH_LONG).show();
                                                                                Intent intent = new Intent(SelectBorsaActivity.this, AnaActivity.class);
                                                                                startActivity(intent);
                                                                                finish();
                                                                            } else {
                                                                                Toast.makeText(SelectBorsaActivity.this, "Failed", Toast.LENGTH_LONG).show();
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            } else {
                                                                Toast.makeText(SelectBorsaActivity.this, "Failed", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(SelectBorsaActivity.this, "Not Selected!!", Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                });
                mySatisdialog.show();
            } else {
                Toast.makeText(SelectBorsaActivity.this, "Uygun saatler dışında borsa satışı yapamazsınız!!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(SelectBorsaActivity.this, "Hafta sonu borsa satışı yapamazsınız!!", Toast.LENGTH_LONG).show();
        }
    }

    public void getDataFromFirestore() {
        CollectionReference collectionReference = firebaseFirestore.collection("Users");
        collectionReference.whereEqualTo("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(SelectBorsaActivity.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        kacHisseAlinmis = (String) data.get(gelenHisseAdi);
                        hisseyiKacaAldigininBilgisi = (String) data.get(gelenHisseAdi + "Value");
                        name = (String) data.get("userName");
                        email = (String) data.get("userEmail");
                        money = Float.parseFloat(data.get("totalMoney").toString());
                        type = (String) data.get("moneyType");
                        kalanPara.setText("Bakiye : " + String.valueOf(money) + " " + type + " \n" + gelenHisseAdi + " : " + hisseyiKacaAldigininBilgisi + " :" + kacHisseAlinmis);


                        kalanPara.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        kalanPara.setTextColor(Color.parseColor("#ca3431"));
                    }
                }
            }
        });
    }
}