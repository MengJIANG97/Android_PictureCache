package com.example.picturecache.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

public class MemoryCache {
    private LruCache<String,Bitmap> mMemoryCache;
    public MemoryCache(){
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize = maxMemory/8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount()/1024;
            }
        };
    }

    public Bitmap getBitmap(String url){
        return mMemoryCache.get(url);
    }

    /**
     * 往内存中写图片
     * @param url
     * @param bitmap
     */
    public void setBitmap(String url, Bitmap bitmap) {
        if (getBitmap(url)==null)
            mMemoryCache.put(url,bitmap);
    }
}
