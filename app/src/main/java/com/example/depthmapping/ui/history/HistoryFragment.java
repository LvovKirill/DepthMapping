package com.example.depthmapping.ui.history;

import androidx.lifecycle.ViewModelProvider;


import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.depthmapping.App;
import com.example.depthmapping.DataBase.DataBase;
import com.example.depthmapping.DataBase.ProcessedImage;
import com.example.depthmapping.DataBase.ProcessedImageDao;
import com.example.depthmapping.R;
import com.example.depthmapping.databinding.HistoryFragmentBinding;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {

    private HistoryViewModel mViewModel;
    HistoryFragmentBinding binding;

//    DataBase db = DataBase.

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = HistoryFragmentBinding.inflate(inflater, container, false);




//       db.getProcessedImageDao().insertAll(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.mephi_test_image), "12.02.2021 11:35"));
//        db.getProcessedImageDao().insertAll(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.mephi_test_image), "12.02.2021 11:35"));
//        db.getProcessedImageDao().insertAll(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.mephi_test_image), "12.02.2021 11:35"));

//        List<ProcessedImage> list = mViewModel.getHistory(getActivity());




//                List<ProcessedImage> listTest = new ArrayList<>();
//                listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.mephi_test_image), "12.02.2021 11:35"));
//        listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.text_test_img), "12.02.2021 11:36"));
//        listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.mephi_test_image), "12.02.2021 11:37"));
//        listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.text_test_img), "12.02.2021 11:36"));
//        listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.mephi_test_image), "12.02.2021 11:35"));
//        listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.text_test_img), "12.02.2021 11:36"));
//        listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.mephi_test_image), "12.02.2021 11:37"));
//        listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.text_test_img), "12.02.2021 11:36"));
//        listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.mephi_test_image), "12.02.2021 11:35"));
//        listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.text_test_img), "12.02.2021 11:36"));
//        listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.mephi_test_image), "12.02.2021 11:37"));
//        listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.text_test_img), "12.02.2021 11:36"));

//                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//                binding.recyclerView.setLayoutManager(mLayoutManager);
//                HistoryAdapter adapter = new HistoryAdapter(getActivity(), listTest);
//                binding.recyclerView.setAdapter(adapter);


        HistoryAsyncTask catTask = new HistoryAsyncTask();
        catTask.execute();


        View root = binding.getRoot();
        return root;
    }

    class HistoryAsyncTask extends AsyncTask<Void, Void, Void> {

        List<ProcessedImage> listTest = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            binding.avi.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.mephi_test_image), "12.02.2021 11:35"));
//            listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.text_test_img), "12.02.2021 11:36"));
//            listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.mephi_test_image), "12.02.2021 11:37"));
//            listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.mephi_test_image), "12.02.2021 11:35"));
//            listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.text_test_img), "12.02.2021 11:36"));
//            listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.mephi_test_image), "12.02.2021 11:37"));
//            listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.mephi_test_image), "12.02.2021 11:35"));
//            listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.text_test_img), "12.02.2021 11:36"));
//            listTest.add(new ProcessedImage(BitmapFactory.decodeResource(getResources(), R.drawable.mephi_test_image), "12.02.2021 11:37"));


            listTest = DataBase.getDatabase(getActivity()).processedImageDao()
                    .getAllProcessedImage();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            binding.recyclerView.setLayoutManager(mLayoutManager);
            HistoryAdapter adapter = new HistoryAdapter(getActivity(), listTest);
            binding.recyclerView.setNestedScrollingEnabled(false);
            adapter.setHasStableIds(true);
            binding.recyclerView.setHasFixedSize(true);
            binding.recyclerView.setItemViewCacheSize(20);
            binding.recyclerView.setDrawingCacheEnabled(true);
            binding.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            binding.avi.hide();

            binding.recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
    }




}