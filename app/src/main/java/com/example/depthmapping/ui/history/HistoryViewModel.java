package com.example.depthmapping.ui.history;


import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.example.depthmapping.DataBase.DataBase;
import com.example.depthmapping.DataBase.ProcessedImage;
import com.example.depthmapping.classifier.ImageClassifier;

import java.util.List;

public class HistoryViewModel extends ViewModel {

    List<ProcessedImage> getHistory(Context context){

        DataBase db = Room.databaseBuilder(context,DataBase.class, "populous-database").build();

        List<ProcessedImage> list = db.getProcessedImageDao().getAllProcessedImage();

        return list;
    }
}