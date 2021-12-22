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

        // Удаление Person из бд
        @Delete
        void delete(ProcessedImage processedImage);

        // Получение всех Person из бд
        @Query("SELECT * FROM processedImage")
        List<ProcessedImage> getAllProcessedImage();

        // Получение всех Person из бд с условием
        @Query("SELECT * FROM processedImage WHERE id LIKE :id")
        List<ProcessedImage> getAllPeopleWithFavoriteColor(String id);

}
