package com.example.picturecache.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class Pictures {
    static public InternetCache internetCache;
    static public DiskCache diskCache;
    static public MemoryCache memoryCache;

    public Pictures(){
        diskCache = new DiskCache();
        memoryCache= new MemoryCache();
        internetCache= new InternetCache(diskCache,this,memoryCache);
    }
    static public void Internet(ImageView imageView,String url){
        internetCache.getBitmapFromNet(imageView, url);
    }

    static public void Disk(ImageView imageView,String url){
        Bitmap bitmap = diskCache.getBitmap(url);
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap);
            memoryCache.setBitmap(url,bitmap);
        }else
            return;
    }

    static public void Memory(ImageView imageView,String url){
        Bitmap bitmap = memoryCache.getBitmap(url);
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap);
        }else {
            Internet(imageView,url);
        }
    }
}
