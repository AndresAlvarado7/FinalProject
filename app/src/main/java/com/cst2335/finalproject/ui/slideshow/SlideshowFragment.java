package com.cst2335.finalproject.ui.slideshow;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cst2335.finalproject.FavAdapter;
import com.cst2335.finalproject.FavDB;
import com.cst2335.finalproject.FavItem;
import com.cst2335.finalproject.R;
import com.cst2335.finalproject.databinding.FragmentSlideshowBinding;

import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private RecyclerView recyclerView;
    private FavDB favDB;
    private List<FavItem> favItemList = new ArrayList<>();
    private FavAdapter favAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

//        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        favDB = new FavDB(getActivity());
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadData();

//        final TextView textView = binding.textSlideshow;
//        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void loadData() {
        if (favItemList != null) {
            favItemList.clear();
        }
        SQLiteDatabase db = favDB.getReadableDatabase();
        Cursor cursor = favDB.select_all_favorite_list();
        try {
            while (cursor.moveToNext()) {
                int indexX = cursor.getColumnIndex(FavDB.ALBUM_TITLE);
                String title = cursor.getString(indexX);
                int indexY = cursor.getColumnIndex(FavDB.KEY_ID);
                String id = cursor.getString(indexY);
                int indexZ = cursor.getColumnIndex(FavDB.ALBUM_IMAGE);
                int image = Integer.parseInt(cursor.getString(indexZ));
                FavItem favItem = new FavItem(title, id, image);
                favItemList.add(favItem);
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }

        favAdapter = new FavAdapter(getActivity(), favItemList);
        recyclerView.setAdapter(favAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}