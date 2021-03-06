package com.cst2335.finalproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * It inflates the view hierarchy
 * and creates an instance of the ViewHolder class
 * initialized with the view hierarchy before
 * returning it to the RecyclerView.
 */
public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder>{

    //crating variables, arrays and instances
    private Context context;
    private List<FavItem> favItemList;
    private FavDB favDB;

    public FavAdapter(Context context, List<FavItem> favItemList) {
        this.context = context;
        this.favItemList = favItemList;
    }

    /**
     * This method inflates the layout we want to inflate
     * @param parent context of a parent object
     * @param viewType
     * @return the view of the layout associated with the holder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_item,
                parent, false);

        favDB = new FavDB(view.getContext());

        //go through the list to get album title and id of each element of the list
        Cursor cursor = favDB.select_all_favorite_list();
        while (cursor.moveToNext()) {
            int indexX = cursor.getColumnIndex(FavDB.ALBUM_TITLE);
            String title = cursor.getString(indexX);
            int indexY = cursor.getColumnIndex(FavDB.KEY_ID);
            String id = cursor.getString(indexY);
            FavItem favItem = new FavItem(title, id);
//            favItemList.add(favItem);

            //setting a clickListener to each item in the list
            view.setOnClickListener((b) -> { //
                Uri uri = Uri.parse("https://www.google.com/search?q="+favItem.getItem_title());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            });
        }
        cursor.close();
        return new ViewHolder(view);
    }

    /*
     * This data is then displayed on the layout views using the references
     * created in the constructor method of the ViewHolder class
     * initializes a Row at the position in the data array
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.favTextView.setText(favItemList.get(position).getItem_title());
        //what is this?
        //holder.favImageView.setImageResource(favItemList.get(position).getItem_image());
    }

    /**
     * count the all the items in the list
     * @return return number of items in the list
     */
    @Override
    public int getItemCount() {
        return favItemList.size();
    }

    /**
     * This class holds favourites albums views on a row:
     */
    public class ViewHolder extends RecyclerView.ViewHolder{

        //crating variables and instances
        TextView favTextView;
        Button favBtn;
        ImageView favImageView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            favTextView = itemView.findViewById(R.id.favTextView);
            favBtn = itemView.findViewById(R.id.favBtn);
            favImageView = itemView.findViewById(R.id.favImageView);

            //remove from fav after click
            favBtn.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final FavItem favItem = favItemList.get(position);
                favDB.remove_fav(favItem.getKey_id());
                removeItem(position);

                context = context.getApplicationContext();
                CharSequence text = context.getResources().getString(R.string.toast_messageRemoveFav);
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(context, text, duration).show();
            });
        }
    }

    /**
     * remove items from the favourite list
     * @param position search items with the position to be removed
     */
    private void removeItem(int position) {
        favItemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,favItemList.size());
    }
}
