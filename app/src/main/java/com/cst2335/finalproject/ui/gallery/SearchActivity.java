package com.cst2335.finalproject.ui.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cst2335.finalproject.AlbumItem;
import com.cst2335.finalproject.R;
import android.widget.ListView;
import android.widget.TextView;

import com.cst2335.finalproject.databinding.ActivityMainBinding;
import com.cst2335.finalproject.databinding.ActivitySearchBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class SearchActivity<MyOpener, MyListAdapter> extends AppCompatActivity {

    private ActivitySearchBinding binding;
    private ArrayList<AlbumItem> list = new ArrayList<>();
    private MyAdapter adapter;
    private APIBrowseCall browseReq;
    private APISearchCall searchReq;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent fromSearchFrag = getIntent();
        String searchFrag = fromSearchFrag.getStringExtra("SEARCH");

        ListView albumList = findViewById(R.id.listView); // ListView for containing messages
        albumList.setAdapter(adapter = new MyAdapter()); // setting adapter for list view
        browseReq = new APIBrowseCall();
        searchReq = new APISearchCall();

        String artistId = null;
        try {
            artistId = searchReq.execute("https://musicbrainz.org/ws/2/artist/?query=artist:" + searchFrag + "&limit=1&fmt=json").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //initial search from fragment

        browseReq.execute("https://musicbrainz.org/ws/2/release?artist=" + artistId +"&offset=0&limit=25&fmt=json");
        binding.searchText.setText(artistId);


    }

    private class APIBrowseCall extends AsyncTask <String, ArrayList<String>, String> {


        public void onPreExecute(){

        }

        @Override
        public String doInBackground(String ... args) {

            ArrayList<String> titles = new ArrayList<>();

            try {
                URL url = new URL(args[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream response = connection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string
                JSONObject uvReport = new JSONObject(result);

                JSONArray arr = uvReport.getJSONArray("releases");
                for (int i = 0; i < arr.length(); i++)
                {
                    titles.add(arr.getJSONObject(i).getString("title"));
                }

                this.publishProgress(titles);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return "done";
        }

        public void onProgressUpdate(ArrayList<String> ... values){
            ArrayList<String> titles = values[0];
            for (int i = 0; i < titles.size(); i++){
                String title = titles.get(i);
                list.add(new AlbumItem(title, "0", "0", "0"));
                Log.d("query", "Added");
            }
            adapter.notifyDataSetChanged();

        }

        public void onPostExecute(String fromDoInBackground) {

            Log.i("query", "DONE");
        }
    }


    private class APISearchCall extends AsyncTask <String, String, String> {


        public void onPreExecute(){

        }

        @Override
        public String doInBackground(String ... args) {

            String artId = new String();

            try {
                URL url = new URL(args[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream response = connection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string
                JSONObject uvReport = new JSONObject(result);

                JSONArray arr = uvReport.getJSONArray("artists");
                for (int i = 0; i < arr.length(); i++)
                {
                    artId = arr.getJSONObject(i).getString("id");
                }

                //THIS IS REQUIRED TO NOT GET BLOCKED BY THE API
                Thread.sleep(1500);



                //System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return artId;
        }

        public void onProgressUpdate(String ... values){
        }

        public void onPostExecute(String fromDoInBackground) {
            Log.i("query", "DONE");
        }
    }


    private class Album{
        private String title;

        public Album(String title){
            this.title = title;
        }

        public String getTitle(){
            return title;
        }

    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) { return list.get(position).getTitle(); }

        @Override
        public long getItemId(int position) { return 1; }

        @Override
        public View getView(int position, View old, ViewGroup viewGroup) {

            LayoutInflater inflater = getLayoutInflater();
            View row;
            TextView txtView;

            //Uses recycled view if exists, if not calls inflater.
            //view type is dependant on which button user presses (see getItemViewType)

            if(old == null){
                row = inflater.inflate(R.layout.search_item, viewGroup, false);
            } else {
                row = old;
            }

            txtView = row.findViewById(R.id.favTextView);
            txtView.setText(getItem(position).toString());



            return row;
        }

        @Override
        public int getViewTypeCount(){
            return 1;
        }
    }

}