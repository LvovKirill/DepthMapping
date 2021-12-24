package com.example.depthmapping.DataBase;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.depthmapping.classifier.ImageClassifier;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Entity(tableName = "processedImage")
public class ProcessedImage {
    @PrimaryKey(autoGenerate = true)
    int id;
    String originImage;
    String image;
    String smalImage;
    String date;
//    List<String> recognitionList;

    @Ignore
    public ProcessedImage(String originImage, String image, String date) {
        this.image = image;
        this.originImage = originImage;
        this.smalImage = getBase64String(resizeImage(getBitmap()));
        this.date = date;
    }

    public ProcessedImage(int id, String originImage, String image, String date) {
        this.id = id;
        this.image = image;
        this.originImage = originImage;
        this.smalImage = getBase64String(resizeImage(getBitmap()));
        this.date = date;
    }

    //    public ProcessedImage(int id, Bitmap image, String date, List<String> recognitionList) {
//        this.id = id;
//        this.image = getBase64String(image);
//        this.date = date;
//        this.recognitionList = recognitionList;
//    }

    @Ignore
    public ProcessedImage(String originImage, Bitmap image, String date) {
        this.id = id;
        this.image = getBase64String(image);
        this.originImage = originImage;
        this.smalImage = getBase64String(resizeImage(getBitmap()));
        this.date = date;
    }

    public String getOriginImage() {
        return originImage;
    }

    public void setOriginImage(String originImage) {
        this.originImage = originImage;
    }

    public String getSmalImage() {
        return smalImage;
    }

    public void setSmalImage(String smalImage) {
        this.smalImage = smalImage;
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


    public Bitmap getBitmap() throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode( image.substring(image.indexOf(",") + 1), Base64.DEFAULT );
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public Bitmap getSmallBitmap() throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode( smalImage.substring(smalImage.indexOf(",") + 1), Base64.DEFAULT );
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public Bitmap resizeImage(Bitmap image){
       return Bitmap.createScaledBitmap(image, image.getWidth()/3, image.getHeight()/3, false);
    }

    String getBase64String(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String base64String = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        return base64String;
    }


}
