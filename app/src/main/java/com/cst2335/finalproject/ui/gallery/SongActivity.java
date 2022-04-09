package com.cst2335.finalproject.ui.gallery;

import androidx.appcompat.app.AppCompatActivity;

import com.cst2335.finalproject.AlbumItem;
import com.cst2335.finalproject.R;
import com.cst2335.finalproject.databinding.ActivitySearchBinding;
import com.cst2335.finalproject.databinding.ActivitySongBinding;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SongActivity extends AppCompatActivity {

    private ActivitySongBinding binding;
    private APIBrowseCallSongs apiBrowseCallSongs;
    private ArrayList<AlbumItem> list = new ArrayList<>();
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent fromSearch = getIntent();
        String search = fromSearch.getStringExtra("ARTIST");
        apiBrowseCallSongs = new APIBrowseCallSongs();
        ListView songList = binding.songView;
        songList.setAdapter(adapter = new MyAdapter());
        System.out.println(search);
        apiBrowseCallSongs.execute("https://musicbrainz.org/ws/2/work?artist=" + search + "&offset=0&limit=50&fmt=json");

    }


    private class APIBrowseCallSongs extends AsyncTask<String, ArrayList<String>, String> {


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

                JSONArray arr = uvReport.getJSONArray("works");
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