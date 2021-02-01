package com.example.gradiotionproject.MainProcesses;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gradiotionproject.ParseSites.ParseBorsaAdapter;
import com.example.gradiotionproject.ParseSites.ParseBorsaItem;
import com.example.gradiotionproject.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class BorsaFragment extends Fragment {

    private static final String TAG = "BorsaFragment";
    private RecyclerView recyclerView;
    private ParseBorsaAdapter parseBorsaAdapter;
    private ArrayList<ParseBorsaItem> parseBorsaItems = new ArrayList<>();
    private ProgressBar progressBar;


    private ParseBorsaAdapter.RecyclerViewClickListenerBorsa listenerBorsa;

    public BorsaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_borsa, container, false);



        progressBar = view.findViewById(R.id.progressBarBorsa);
        recyclerView = view.findViewById(R.id.recyclerViewBorsa);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        parseBorsaAdapter = new ParseBorsaAdapter(parseBorsaItems, getContext(),listenerBorsa);
        recyclerView.setAdapter(parseBorsaAdapter);

        Content content = new Content();
        content.execute();

        setOnClickListenerBorsa();
        return view;
    }

    private void setOnClickListenerBorsa() {
        listenerBorsa = (view, position) -> {
            Intent intent = new Intent(getContext(),SelectBorsaActivity.class);
            intent.putExtra("hisseAdi",parseBorsaItems.get(position).getCompanyName());
            intent.putExtra("son",parseBorsaItems.get(position).getCompanyPrice());
            intent.putExtra("dun",parseBorsaItems.get(position).getDifferancePrice());
            intent.putExtra("yuzde",parseBorsaItems.get(position).getTime());
            intent.putExtra("yuksek",parseBorsaItems.get(position).getYuksekFiyat());
            intent.putExtra("dusuk",parseBorsaItems.get(position).getDusukFiyat());
            intent.putExtra("lot",parseBorsaItems.get(position).getHacimLot());
            intent.putExtra("tly",parseBorsaItems.get(position).getHacimTl());
            parseBorsaAdapter.notifyDataSetChanged();
            startActivity(intent);
        };
    }

    private class Content extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                progressBar.setVisibility(View.GONE);
  //              progressBar.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
                parseBorsaAdapter.notifyDataSetChanged();
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            GetHtmlBorsaCurrencyQuery();
            return null;
        }

    }

    private void GetHtmlBorsaCurrencyQuery() {
        parseBorsaItems.removeAll(parseBorsaItems);

        String url = "https://www.bloomberght.com/borsa/hisseler/bist-30-endeksi";
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
            for (Element row : document.select(
                    "div.widget-table-data.type3 tr")) {
                if (row.select("td:nth-of-type(1)").text().equals("")) {
                    continue;
                } else {
                    final String ticker =
                            row.select("td:nth-of-type(1)").text();
                    final String ilkFiyat =
                            row.select("td:nth-of-type(2)").text();
                    final String degisim =
                            row.select("td:nth-of-type(3)").text();
                    final String sonFiyat =
                            row.select("td:nth-of-type(4)").text();
                    final String enDusukFiyat =
                            row.select("td:nth-of-type(5)").text();
                    final String yuzde =
                            row.select("td:nth-of-type(6)").text();
                    final String hacimLot =
                            row.select("td:nth-of-type(7)").text();
                    final String hacimTl =
                            row.select("td:nth-of-type(8)").text();
                    parseBorsaItems.add(new ParseBorsaItem(ticker, ilkFiyat,degisim, sonFiyat, yuzde, hacimLot,hacimTl,enDusukFiyat));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url2 = "https://www.bloomberght.com/borsa/hisseler/bist-30-endeksi/2";
        try {
            document = Jsoup.connect(url2).get();
            for (Element row : document.select(
                    "div.widget-table-data.type3 tr")) {
                if (row.select("td:nth-of-type(1)").text().equals("")) {
                    continue;
                } else {
                    final String ticker =
                            row.select("td:nth-of-type(1)").text();
                    final String ilkFiyat =
                            row.select("td:nth-of-type(2)").text();
                    final String degisim =
                            row.select("td:nth-of-type(3)").text();
                    final String sonFiyat =
                            row.select("td:nth-of-type(4)").text();
                    final String enDusukFiyat =
                            row.select("td:nth-of-type(5)").text();
                    final String yuzde =
                            row.select("td:nth-of-type(6)").text();
                    final String hacimLot =
                            row.select("td:nth-of-type(7)").text();
                    final String hacimTl =
                            row.select("td:nth-of-type(8)").text();
                    parseBorsaItems.add(new ParseBorsaItem(ticker, ilkFiyat,degisim, sonFiyat, yuzde, hacimLot,hacimTl,enDusukFiyat));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}