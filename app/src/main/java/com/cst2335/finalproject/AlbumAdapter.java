package com.cst2335.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{

    private final ArrayList<AlbumItem> albumItems;
    private final Context context;
    private FavDB favDB;

    public AlbumAdapter(ArrayList<AlbumItem> albumItems, Context context) {
        this.albumItems = albumItems;
        this.context = context;
    }

    @NonNull
    @Override
    public  ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        favDB.getInstance(context);
        //create table on first
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            createTableOnFirstStart();
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album,
                parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, int position) {
        final AlbumItem albumItem = albumItems.get(position);

        readCursorData(albumItem, holder);
        //holder.imageView.setImageResource(albumItem.getImageResourse());
        holder.titleTextView.setText(albumItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return albumItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleTextView, likeCountTextView;
        Button favBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewAlbum);
            titleTextView = itemView.findViewById(R.id.textViewAlbum);
            favBtn = itemView.findViewById(R.id.favBtn);

            //add to fav btn
            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    AlbumItem albumItem = albumItems.get(position);

                    if (albumItem.getFavStatus().equals("0")) {
                        // TODO here
                        albumItem.setFavStatus("1");
                        //favDB.insertIntoTheDatabase(albumItem.getTitle(), albumItem.getFavStatus());
                        favBtn.setBackgroundResource(R.drawable.ic_favorite_red_24);
                    } else {
                        albumItem.setFavStatus("0");
                        favDB.remove_fav(albumItem.getKey_id());
                        favBtn.setBackgroundResource(R.drawable.ic_favorite_shadow_24);
                    }
                }
            });
        }
    }

    private void createTableOnFirstStart() {
        favDB.insertEmpty();

        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    private void readCursorData(AlbumItem albumItem, ViewHolder viewHolder) {
        Cursor cursor = favDB.read_all_data(albumItem.getKey_id());
        SQLiteDatabase db = favDB.getReadableDatabase();
        try {
            while (cursor.moveToNext()) {
                int index = cursor.getColumnIndex(FavDB.FAVORITE_STATUS);
                String item_fav_status = cursor.getString(index);
                albumItem.setFavStatus(item_fav_status);

                //check fav status
                if (item_fav_status != null && item_fav_status.equals("1")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite_red_24);
                } else if (item_fav_status != null && item_fav_status.equals("0")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite_shadow_24);
                }
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }

    }
}
