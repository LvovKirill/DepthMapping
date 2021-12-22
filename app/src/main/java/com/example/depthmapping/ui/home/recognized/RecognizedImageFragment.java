package com.example.depthmapping.ui.home.recognized;

import static android.app.Activity.RESULT_OK;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.depthmapping.R;
import com.example.depthmapping.Util;
import com.example.depthmapping.classifier.ImageClassifier;
import com.example.depthmapping.databinding.RecognizedImageFragmentBinding;
import com.example.depthmapping.ui.LoadingFragment;
import com.example.depthmapping.ui.home.HomeViewModel;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;


    public class RecognizedImageFragment extends Fragment {

        private HomeViewModel homeViewModel;
        public static RecognizedImageFragmentBinding binding;

        public static Bitmap bitmap;

        private static final int CAMERA_PERMISSION_REQUEST_CODE = 1000;
        private static final int CAMERA_REQEUST_CODE = 10001;

        private ImageClassifier imageClassifier;

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
            binding = RecognizedImageFragmentBinding.inflate(inflater, container, false);

            initializeUIElements();

            View root = binding.getRoot();
            return root;
        }



        private void initializeUIElements() {

            try {
                imageClassifier = new ImageClassifier(getActivity());
            } catch (IOException e) {
                Log.e("Image Classifier Error", "ERROR: " + e);
            }


            binding.takepicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

            binding.takepicture.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    binding.imageView.setImageBitmap(bitmap);

                    return false;
                }
            });

        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

            if (resultCode == RESULT_OK && data != null){
                if (requestCode == CAMERA_REQEUST_CODE) {
                    Bitmap photo = (Bitmap) Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).get("data");
                    binding.imageView.setImageBitmap(photo);

                    Fragment frag2 = new LoadingFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.add(R.id.container, frag2);
                    ft.commit();

//                    callServer(Util.getBase64String(photo));



//                SshConection sshConection = new SshConection("0.tcp.ngrok.io", 18099, "root", "nNDpvbhKMtDLO0PruRCp",
//                        getActivity().getApplicationContext(), photo);


//                Bitmap bitmap_out = sshConection.start();

//                try {
//                    TimeUnit.SECONDS.sleep(180);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                binding.imageView.setImageBitmap(bitmap_out);

//                while (true){
//                    binding.imageView.setImageBitmap(bitmap);
//                    if(bitmap!=null){break;}
//                }

//                if(bitmap_out==null) {
//                    while (bitmap == null) {
//                        binding.imageView.setImageBitmap(bitmap);
//                    }
//                }

                    List<ImageClassifier.Recognition> predicitons = imageClassifier.recognizeImage(
                            photo, 0);

                    final List<NNPoint> predicitonsList = new ArrayList<>();
                    for (ImageClassifier.Recognition recog : predicitons) {
                        predicitonsList.add(new NNPoint(recog.getName(), Float.toString(recog.getConfidence())));
                    }


                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    binding.recyclerView.setLayoutManager(mLayoutManager);
                    NeiroNetAdapter adapter = new NeiroNetAdapter(getActivity(), predicitonsList);
                    binding.recyclerView.setAdapter(adapter);

                    binding.recyclerView.setItemViewCacheSize(25);
                    binding.recyclerView.setDrawingCacheEnabled(true);
                    binding.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

                }
            }
            super.onActivityResult(requestCode, resultCode, data);


            Fragment frag2 = new LoadingFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(frag2).commit();

        }



        @Override
        public void onDestroyView() {
            super.onDestroyView();
            binding = null;
        }



    }