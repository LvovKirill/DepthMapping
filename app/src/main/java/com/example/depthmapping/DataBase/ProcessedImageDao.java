package com.example.depthmapping.DataBase;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProcessedImageDao {

        @Insert
        void insertAll(ProcessedImage processedImages);

        @Insert
        void insert(ProcessedImage processedImage);

        @Query("SELECT * FROM processedImage")
        List<ProcessedImage> getAllProcessedImageQuery();


        @Delete
        void delete(ProcessedImage processedImage);

        @Query("DELETE FROM processedImage")
        void deleteAll();


        @Query("SELECT * FROM processedImage")
        List<ProcessedImage> getAllProcessedImage();


        @Query("SELECT * FROM processedImage WHERE id LIKE :id")
        List<ProcessedImage> getAllPeopleWithFavoriteColor(String id);

}
