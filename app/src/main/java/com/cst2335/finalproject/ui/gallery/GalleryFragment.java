package com.cst2335.finalproject.ui.gallery;

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

import com.cst2335.finalproject.AlbumAdapter;
import com.cst2335.finalproject.AlbumItem;
import com.cst2335.finalproject.R;
import com.cst2335.finalproject.databinding.FragmentGalleryBinding;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private ArrayList<AlbumItem> albumItems = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        GalleryViewModel galleryViewModel =
//                new ViewModelProvider(this).get(GalleryViewModel.class);

//        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

//        final TextView textView = binding.textViewSearch;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new AlbumAdapter(albumItems, getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        albumItems.add(new AlbumItem(R.drawable.headphones, "Cold Play","0","0"));
        albumItems.add(new AlbumItem(R.drawable.headphones, "Usher","1","0"));
        albumItems.add(new AlbumItem(R.drawable.headphones, "DJ Tiesto","2","0"));
        albumItems.add(new AlbumItem(R.drawable.headphones, "Bruno Mars","3","0"));
        albumItems.add(new AlbumItem(R.drawable.headphones, "Dua Lipa","4","0"));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}