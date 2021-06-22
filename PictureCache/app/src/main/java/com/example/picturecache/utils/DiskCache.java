package com.example.picturecache.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class DiskCache {
    public Bitmap getBitmap(String path){
        try {
            File file=new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/diskcache", path);
            return BitmapFactory.decodeStream(new FileInputStream(file));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public void setBitmap(String url, Bitmap bitmap){
        try {
            File file=new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), url);

            //通过得到文件的父文件,判断父文件是否存在
            File parentFile = file.getParentFile();
            assert parentFile != null;
            if (!parentFile.exists()){
                parentFile.mkdirs();
            }
            //把图片保存至本地
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
