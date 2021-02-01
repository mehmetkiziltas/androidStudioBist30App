package com.example.gradiotionproject.MainProcesses;

import android.content.Intent;
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

import com.example.gradiotionproject.ParseSites.ParseDovizAdepter;
import com.example.gradiotionproject.ParseSites.ParseDovizItem;
import com.example.gradiotionproject.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class DovizFragment extends Fragment {


    private RecyclerView recyclerViewDoviz;
    private ParseDovizAdepter parseDovizAdepter;
    private ArrayList<ParseDovizItem> parseDovizItems = new ArrayList<>();
    private ProgressBar progressBar;
    private ParseDovizAdepter.RecyclerViewClickListener listener;

    public DovizFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_doviz, container, false);


        progressBar = view.findViewById(R.id.progressBarDoviz);
        recyclerViewDoviz = view.findViewById(R.id.recyclerViewDoviz);
        recyclerViewDoviz.setHasFixedSize(true);
        recyclerViewDoviz.setLayoutManager(new LinearLayoutManager(getContext()));
        parseDovizAdepter = new ParseDovizAdepter(parseDovizItems,getContext(),listener);
        recyclerViewDoviz.setAdapter(parseDovizAdepter);
        Content content = new Content();
        content.execute();
        setOnClickListenerDoviz();
        return view;
    }

    private void setOnClickListenerDoviz() {
        listener = (view, position) -> {
            Intent intent = new Intent(getContext(),SelectDovizActivity.class);
            intent.putExtra("dovizAd",parseDovizItems.get(position).getCurrencyName());
            intent.putExtra("dovizAlis",parseDovizItems.get(position).getCurrencySale());
            intent.putExtra("degisiklik",parseDovizItems.get(position).getCurrencyChange());
            intent.putExtra("zaman",parseDovizItems.get(position).getCurrencyTime());
            parseDovizAdepter.notifyDataSetChanged();
            startActivity(intent);
        };
    }

    private class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
//            progressBar.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
            parseDovizAdepter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            GetHtmlDovizQuery();
            return null;
        }
    }

    private void GetHtmlDovizQuery() {
        parseDovizItems.removeAll(parseDovizItems);
        String url = "https://www.doviz.com/";
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
            for (Element row : document.select(
                    "div.table.table-narrow tr")) {
                if (row.select("td:nth-of-type(1)").text().equals("")) {
                    continue;
                } else {
                    final String currencyName =
                            row.select("td:nth-of-type(1)").text();
                    final String currencyAlisFiyat =
                            row.select("td:nth-of-type(2)").text();
                    final String currencyDurumNet =
                            row.select("td:nth-of-type(3)").text();
                    final String currencyGuncelleme =
                            row.select("td:nth-of-type(4)").text();
                    parseDovizItems.add(new ParseDovizItem(currencyName,currencyAlisFiyat,currencyDurumNet,currencyGuncelleme));

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}