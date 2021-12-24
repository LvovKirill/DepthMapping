package com.example.depthmapping.ui.home.recognized;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.depthmapping.R;
import com.example.depthmapping.ui.home.NNPoint;

import java.util.List;

public class NeiroNetAdapter  extends RecyclerView.Adapter<NeiroNetAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<NNPoint> nnPoints;

    NeiroNetAdapter(Context context, List<NNPoint> states) {
        this.nnPoints = states;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public NeiroNetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.neiro_net_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NeiroNetAdapter.ViewHolder holder, int position) {
        NNPoint nnPoint = nnPoints.get(position);
        holder.type.setText(nnPoint.getType());
        holder.accuracy.setText(nnPoint.getAccuracy());
    }

    @Override
    public int getItemCount() {
        return nnPoints.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView type;
        final TextView accuracy;
        ViewHolder(View view){
            super(view);
            type = view.findViewById(R.id.typeText);
            accuracy = view.findViewById(R.id.scoreText);
        }
    }
}
