package com.example.depthmapping.ui.home.recognized;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.depthmapping.App;
import com.example.depthmapping.DataBase.DataBase;
import com.example.depthmapping.DataBase.ProcessedImage;
import com.example.depthmapping.DataBase.ProcessedImageDao;
import com.example.depthmapping.R;
import com.example.depthmapping.Util;
import com.example.depthmapping.classifier.ImageClassifier;
import com.example.depthmapping.databinding.RecognizedImageFragmentBinding;
import com.example.depthmapping.ui.home.HomeViewModel;
import com.example.depthmapping.ui.home.NNPoint;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RecognizedImageFragment extends Fragment {

        private HomeViewModel homeViewModel;
        public static RecognizedImageFragmentBinding binding;

        public static Bitmap bitmap;

        private static final int CAMERA_PERMISSION_REQUEST_CODE = 1000;
        private static final int CAMERA_REQEUST_CODE = 10001;

        private ImageClassifier imageClassifier;

        private String image;
        private String originImage;
        private String flag;

        private boolean imageFlag=true;

        public static RecognizedImageFragment newInstance(String image, String originImage, String flag) {
            RecognizedImageFragment fragment = new RecognizedImageFragment();
            Bundle args = new Bundle();
            args.putString("image", image);
            args.putString("originImage", originImage);
            args.putString("flag", flag);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                image = getArguments().getString("image");
                originImage = getArguments().getString("originImage");
                flag = getArguments().getString("flag");
            }
        }

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
            binding = RecognizedImageFragmentBinding.inflate(inflater, container, false);

            try {
                initializeUIElements();
            } catch (IOException e) {
                e.printStackTrace();
            }

            View root = binding.getRoot();
            return root;
        }



        private void initializeUIElements() throws IOException {

            try {
                imageClassifier = new ImageClassifier(getActivity());
            } catch (IOException e) {
                Log.e("Image Classifier Error", "ERROR: " + e);
            }

            binding.imageView.setImageBitmap(Util.convert(image));

            binding.originButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(imageFlag) {
                        binding.imageView.setImageBitmap(Util.convert(originImage));
                        binding.originButton.setText(getString(R.string.depth_map));
                        imageFlag=false;
                    }else{
                        binding.imageView.setImageBitmap(Util.convert(image));
                        binding.originButton.setText(getString(R.string.origin));
                        imageFlag=true;
                    };
                }
            });


            if(flag.equals("db")) {
                Date currentDate = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                String dateText = dateFormat.format(currentDate);
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                String timeText = timeFormat.format(currentDate);

                ProcessedImage processedImage = new ProcessedImage(originImage, image, dateText + " " + timeText);

                DataBase.getDatabase(getActivity()).processedImageDao()
                        .insert(processedImage);
            }

//            ObjectDetector.ObjectDetectorOptions options =
//                    ObjectDetector.ObjectDetectorOptions.builder()
//                            .setBaseOptions(BaseOptions.builder().useGpu().build())
//                            .setMaxResults(1)
//                            .build();
//            ObjectDetector objectDetector =
//                    ObjectDetector.createFromFileAndOptions(
//                            getActivity(),   "ssd_mobilenet_v1_1_metadata_1", options);
//
//
//            List<Detection> results = objectDetector.detect(resizePic(Util.convert(image)));
//
//            final List<NNPoint> predicitonsList = new ArrayList<>();
//            for (Detection recog : results) {
//                predicitonsList.add(new NNPoint(recog.getCategories().get(1).toString(), Float.toString(recog.getBoundingBox().centerX())));
//            }
//            Util.saveListNNPoint(predicitonsList);
//
//            System.out.print("______________________________________________________");
//            System.out.print(results.toString());
//            System.out.print("______________________________________________________");


            List<ImageClassifier.Recognition> predicitons = imageClassifier
                                .recognizeImage(Util.convert(image), 0);

                final List<NNPoint> predicitonsList = new ArrayList<>();
                for (ImageClassifier.Recognition recog : predicitons) {
                    predicitonsList.add(new NNPoint(recog.getName(), Float.toString(recog.getConfidence())));
                }
                Util.saveListNNPoint(predicitonsList);

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            binding.recyclerView.setLayoutManager(mLayoutManager);
            NeiroNetAdapter adapter = new NeiroNetAdapter(getActivity(), predicitonsList);
            binding.recyclerView.setAdapter(adapter);

        }

    private TensorImage resizePic(Bitmap bp) {
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeOp(60, 60, ResizeOp.ResizeMethod.BILINEAR))
                        .build();
        TensorImage tImage = new TensorImage(DataType.FLOAT32);
        tImage.load(bp);
        tImage = imageProcessor.process(tImage);
        return tImage;
    }



    }