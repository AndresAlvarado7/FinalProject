package com.cst2335.finalproject.ui.gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.cst2335.finalproject.R;
import android.widget.ListView;
import android.widget.TextView;
import com.cst2335.finalproject.databinding.ActivitySearchBinding;
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


public class SearchActivity<MyOpener, MyListAdapter> extends AppCompatActivity {

    private ActivitySearchBinding binding;
    private ArrayList<Album> list = new ArrayList<>();
    private MyAdapter adapter;
    private MyOpener opener;
    private SQLiteDatabase database;
    private MyTask req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ListView albumList = findViewById(R.id.listView); // ListView for containing messages
        albumList.setAdapter(adapter = new MyAdapter()); // setting adapter for list view

        MyTask req = new MyTask();
        req.execute("https://musicbrainz.org/ws/2/release?artist=53b106e7-0cc6-42cc-ac95-ed8d30a3a98e&offset=0&limit=7&fmt=json");


    }

    private class MyTask extends AsyncTask <String, ArrayList<String>, String> {


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

               //System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "done";
        }

        public void onProgressUpdate(ArrayList<String> ... values){
            ArrayList<String> titles = values[0];
            for (int i = 0; i < titles.size(); i++){
                String title = titles.get(i);
                list.add(new Album(title));
            }
            adapter.notifyDataSetChanged();

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
                row = inflater.inflate(R.layout.fav_item, viewGroup, false);
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