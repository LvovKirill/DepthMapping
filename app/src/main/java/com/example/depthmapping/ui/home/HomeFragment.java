package com.example.depthmapping.ui.home;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.depthmapping.BuildConfig;
import com.example.depthmapping.DataBase.DataBase;
import com.example.depthmapping.DataBase.ProcessedImage;
import com.example.depthmapping.MainActivity;
import com.example.depthmapping.R;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import com.example.depthmapping.Util;
import com.example.depthmapping.classifier.ImageClassifier;
import com.example.depthmapping.databinding.FragmentHomeBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    public static FragmentHomeBinding binding;

    public static Bitmap bitmap;
    File newfile = null;

    private File dir, destImage,f;
    private String cameraFile = null;

    private static final int CAPTURE_FROM_CAMERA = 1;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1000;
    private static final int CAMERA_REQEUST_CODE = 10001;
    private static int TAKE_PICTURE_REQUEST = 1;

    private Uri outputFileUri;

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

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        dir = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "MyApp");
        if (!dir.isDirectory())
            dir.mkdir();

        destImage = new File(dir, new Date().getTime() + ".jpg");
        cameraFile = destImage.getAbsolutePath();
        try{
            if(!destImage.createNewFile()){}
//                Log.e("check", "unable to create empty file");

        }catch(IOException ex){
            ex.printStackTrace();
        }

        f = new File(destImage.getAbsolutePath());
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destImage));
        startActivityForResult(i,CAPTURE_FROM_CAMERA);



//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(cameraIntent, CAMERA_REQEUST_CODE);
    }

    private boolean hasPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
//        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

//        if (resultCode == RESULT_OK && data != null){
//            if (requestCode == CAMERA_REQEUST_CODE) {
//
////                Bitmap photo = (Bitmap) Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).get("data");
//                Bitmap photo= BitmapFactory.decodeFile("your_file_name_with_dir");
////                binding.imageView.setImageBitmap(photo);
//
//                Fragment frag2 = LoadingFragment.newInstance(Util.getBase64String(photo));
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.add(R.id.container, frag2);
//                ft.commit();
//
//
//            }
//        }

//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == CAMERA_REQEUST_CODE && resultCode == RESULT_OK) {
//            // Проверяем, содержит ли результат маленькую картинку
//            if (data != null) {
//                if (data.hasExtra("data")) {
//                    Bitmap thumbnailBitmap = data.getParcelableExtra("data");
//                }
//            } else {
//                // Какие-то действия с полноценным изображением,
//                // сохранённым по адресу outputFileUri
////                imageView.setImageURI(outputFileUri);
//
//                InputStream fileStream = null;
//                try {
//                    fileStream = getActivity().getContentResolver().openInputStream(outputFileUri);
//                } catch (IOException e) {
////                    Log.e(this.getClass().getName(), e.getMessage());
//                }
//
//                Bitmap bitmap = BitmapFactory.decodeStream(fileStream);
//
////                Fragment frag2 = LoadingFragment.newInstance(Util.getBase64String(bitmap));
////                FragmentTransaction ft = getFragmentManager().beginTransaction();
////                ft.add(R.id.container, frag2);
////                ft.commit();
//
//                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
//                Bundle bundle = new Bundle();
//                bundle.putString("image", Util.getBase64String(bitmap));
//                navController.navigate(R.id.nav_loading, bundle);
//            }
//        }


            super.onActivityResult(requestCode, resultCode, data);


                        if(f==null){
                            if(cameraFile!=null) {
                                f = new File(cameraFile);
                            }
//                                Log.e("check", "camera file object null line no 279");
                        }else{
//                          Log.e("check", f.getAbsolutePath());
                            Bitmap useBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

                            System.out.print("______________________________________");
                            System.out.print("______________________________________");
                            System.out.print("______________________________________");

                            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                            Bundle bundle = new Bundle();
                            bundle.putString("image", Util.getBase64String(useBitmap));
                            navController.navigate(R.id.nav_loading, bundle);

                            System.out.print("______________________________________");
                            System.out.print("______________________________________");
                            System.out.print("______________________________________");
                        }

                        //now use this bitmap wherever you want


    }




    }

