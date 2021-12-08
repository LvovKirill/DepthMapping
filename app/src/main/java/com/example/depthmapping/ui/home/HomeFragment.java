package com.example.depthmapping.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.depthmapping.R;
import com.example.depthmapping.SshConection;
import com.example.depthmapping.databinding.FragmentHomeBinding;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.example.depthmapping.classifier.ImageClassifier;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    public static FragmentHomeBinding binding;

    public static Bitmap bitmap;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1000;
    private static final int CAMERA_REQEUST_CODE = 10001;

    /**
     * UI Elements
     */
    private ImageClassifier imageClassifier;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);

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

                if (hasPermission()) {
                    openCamera();
                } else {
                    requestPermission();
                }
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

                SshConection sshConection = new SshConection("0.tcp.ngrok.io", 18099, "root", "nNDpvbhKMtDLO0PruRCp",
                        getActivity().getApplicationContext(), photo);


                Bitmap bitmap_out = sshConection.start();
//                try {
//                    TimeUnit.SECONDS.sleep(180);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                binding.imageView.setImageBitmap(bitmap_out);

                while (true){
                    binding.imageView.setImageBitmap(bitmap);
                    if(bitmap!=null){break;}
                }

                if(bitmap_out==null) {
                    while (bitmap == null) {
                        binding.imageView.setImageBitmap(bitmap);
                    }
                }

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

            }
    }
        super.onActivityResult(requestCode, resultCode, data);
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}