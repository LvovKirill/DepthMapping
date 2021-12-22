package com.example.depthmapping.DataBase;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.depthmapping.MainActivity;
import com.example.depthmapping.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {ProcessedImage.class /*, AnotherEntityType.class, AThirdEntityType.class */}, version = 1)
public abstract class DataBase extends RoomDatabase {
    public abstract ProcessedImageDao getProcessedImageDao();
}
