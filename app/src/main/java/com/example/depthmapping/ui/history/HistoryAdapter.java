package com.example.depthmapping.ui.history;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.depthmapping.DataBase.DataBase;
import com.example.depthmapping.DataBase.ProcessedImage;
import com.example.depthmapping.R;
import com.example.depthmapping.Util;
import com.example.depthmapping.ui.home.LoadingFragment;
import com.example.depthmapping.ui.home.recognized.RecognizedImageFragment;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<ProcessedImage> processedImageList;
    Context context;

    HistoryAdapter(Context context, List<ProcessedImage> states) {
        this.processedImageList = states;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.history_list_item, parent, false);


        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        ProcessedImage processedImage = processedImageList.get(position);
        holder.date.setText(processedImage.getDate());
        holder.image.setImageBitmap(processedImage.getSmallBitmap());


        ProcessedImage current = processedImageList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                NavController navController = Navigation.findNavController((Activity) context, R.id.nav_host_fragment_content_main);
                Bundle bundle = new Bundle();
                bundle.putString("image", current.getImage());
                bundle.putString("originImage", current.getOriginImage());
                bundle.putString("flag", "");
                navController.navigate(R.id.nav_recognized, bundle);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                return false;
            }
        });

//        HistoryAdapterAsyncTask catTask = new HistoryAdapterAsyncTask(holder.image, processedImage.getImage());
//        catTask.execute();
    }

    @Override
    public int getItemCount() {
        return processedImageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView date;
        final ImageView image;
        ViewHolder(View view){
            super(view);
            date = view.findViewById(R.id.text);
            image = view.findViewById(R.id.image);
        }
    }

    class HistoryAdapterAsyncTask extends AsyncTask<Void, Void, Void> {

        ImageView imageView;
        String base64;

        public HistoryAdapterAsyncTask(ImageView imageView, String base64) {
            this.imageView = imageView;
            this.base64 = base64;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            imageView.setImageBitmap(Util.convert(base64));
        }
    }
}
