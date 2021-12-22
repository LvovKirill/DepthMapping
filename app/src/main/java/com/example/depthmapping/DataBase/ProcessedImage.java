package com.example.depthmapping.DataBase;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.depthmapping.classifier.ImageClassifier;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Entity
public class ProcessedImage {
    @PrimaryKey(autoGenerate = true)
    int id;
    String image;
    String date;
//    List<String> recognitionList;

    public ProcessedImage(String image, String date) {
        this.id = id;
        this.image = image;
        this.date = date;
    }

//    public ProcessedImage(int id, Bitmap image, String date, List<String> recognitionList) {
//        this.id = id;
//        this.image = getBase64String(image);
//        this.date = date;
//        this.recognitionList = recognitionList;
//    }

    public ProcessedImage(Bitmap image, String date) {
        this.id = id;
        this.image = getBase64String(image);
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public Bitmap convertToBitmap() throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode( image.substring(image.indexOf(",") + 1), Base64.DEFAULT );
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

//    public Bitmap resizeImage(Bitmap image){
//        Bitmap scaledImage = Bitmap.createScaledBitmap(image, image.getWidth()/3, image.getHeight()/3, )
//    }

    String getBase64String(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String base64String = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        return base64String;
    }


}
