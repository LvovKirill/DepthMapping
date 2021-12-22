package com.example.depthmapping.ui;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.depthmapping.MainActivity;
import com.example.depthmapping.R;
import com.example.depthmapping.Util;
import com.example.depthmapping.databinding.FragmentLoadingBinding;
import com.example.depthmapping.ui.home.HomeViewModel;
import com.example.depthmapping.ui.home.recognized.RecognizedImageFragment;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoadingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoadingFragment extends Fragment {


    private String image;

    FragmentLoadingBinding binding;

    OkHttpClient client;
    MediaType JSON;

    public LoadingFragment() {
        // Required empty public constructor
    }

    public static LoadingFragment newInstance(String image) {
        LoadingFragment fragment = new LoadingFragment();
        Bundle args = new Bundle();
        args.putString("image", image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            image = getArguments().getString("image");



            OkHttpClient client = new OkHttpClient();

            String url = "http://c3a1-34-74-255-204.ngrok.io";

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String myResponse = response.body().string();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), myResponse, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });

//            callServer(image);



        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoadingBinding.inflate(inflater, container, false);

        binding.avi.show();

        HttpURLConnection con = null;



        View root = binding.getRoot();
        return root;
    }








//    public void callServer(String image){
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://a1bd-34-86-238-185.ngrok.io") // Адрес сервера
//                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
//                .build();
//
//        Server service = retrofit.create(Server.class);
//        Call<HTTPResult> call = service.getDepthMap(image);
//
//        System.out.println("__________________");
//        System.out.println(call.request().toString());
//        System.out.println("__________________");
//
//        call.enqueue(new Callback<HTTPResult>() {
//            @Override
//            public void onResponse(Call<HTTPResult> call, Response<HTTPResult> response) {
//                if (response.isSuccessful()) {
//                    System.out.println("__________________");
//                    System.out.println(response.body());
//                    System.out.println("__________________");
//                } else {
//                    System.out.println("__________________");
//                    System.out.println("ошибка сервера");
//                    System.out.println("__________________");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<HTTPResult> call, Throwable t) {
//                System.out.println("__________________");
//                System.out.println("ошибка времени выполнения");
//                System.out.println("__________________");
//            }
//        });
//    }

    public class HTTPResult {
        @SerializedName("image")
        public String image;
    }

//    public interface Server {
//        @POST("/")
//        Call<HTTPResult> getDepthMap(@Body String s);
//    }

    public class LoginInformation {
        String Picture;
        String Mode;

        public LoginInformation(String picture, String name) {
            Picture = picture;
            Mode = name;
        }
    }






}