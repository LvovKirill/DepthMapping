package com.example.depthmapping.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.depthmapping.R;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.depthmapping.Util;
import com.example.depthmapping.classifier.ImageClassifier;
import com.example.depthmapping.databinding.FragmentHomeBinding;
import com.example.depthmapping.ui.LoadingFragment;
import com.example.depthmapping.ui.home.recognized.NNPoint;
import com.example.depthmapping.ui.home.recognized.NeiroNetAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    public static FragmentHomeBinding binding;

    public static Bitmap bitmap;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1000;
    private static final int CAMERA_REQEUST_CODE = 10001;

    private ImageClassifier imageClassifier;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });

        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_persistent);

//        LinearLayout copy = bottomSheetDialog.findViewById(R.id.copyLinearLayout);
//        LinearLayout share = bottomSheetDialog.findViewById(R.id.shareLinearLayout);
//        LinearLayout upload = bottomSheetDialog.findViewById(R.id.uploadLinearLayout);
//        LinearLayout download = bottomSheetDialog.findViewById(R.id.download);
        Button button = bottomSheetDialog.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hasPermission()) {
                    openCamera();
                    bottomSheetDialog.cancel();
                } else {
                    requestPermission();
                }

            }
        });

        bottomSheetDialog.show();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // if this is the result of our camera permission request
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (hasAllPermissions(grantResults)) {
                openCamera();
            } else {
                requestPermission();
            }
        }
    }


    private boolean hasAllPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return true;
    }


    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Toast.makeText(getActivity(), "Camera Permission Required", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQEUST_CODE);
    }

    private boolean hasPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
//        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && data != null){
            if (requestCode == CAMERA_REQEUST_CODE) {
                Bitmap photo = (Bitmap) Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).get("data");
//                binding.imageView.setImageBitmap(photo);

                Fragment frag2 = LoadingFragment.newInstance(Util.getBase64String(photo));
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.container, frag2);
                ft.commit();

//                callServer(Util.getBase64String(photo));



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

//                List<ImageClassifier.Recognition> predicitons = imageClassifier.recognizeImage(
//                        photo, 0);
//
//                final List<NNPoint> predicitonsList = new ArrayList<>();
//                for (ImageClassifier.Recognition recog : predicitons) {
//                    predicitonsList.add(new NNPoint(recog.getName(), Float.toString(recog.getConfidence())));
//                }


//                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//                binding.recyclerView.setLayoutManager(mLayoutManager);
//                NeiroNetAdapter adapter = new NeiroNetAdapter(getActivity(), predicitonsList);
//                binding.recyclerView.setAdapter(adapter);
//
//                binding.recyclerView.setItemViewCacheSize(25);
//                binding.recyclerView.setDrawingCacheEnabled(true);
//                binding.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);


//        Fragment frag2 = new LoadingFragment();
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.remove(frag2).commit();

    }




}