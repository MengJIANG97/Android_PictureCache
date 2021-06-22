package com.example.picturecache;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * 获取分配了应用内存的1/8作为缓存大小，
 * 在一个normal/hdpi的设备上最少也有4MB(32/8)的大小。
 */

public class ImageLoader {
    private final LruCache<String, Bitmap> mMemoryCache;

    public ImageLoader(){
        /**
         * 获取最大可用 VM 内存，超过此数量将引发 OutOfMemory 异常。
         * 以kb为单位存储，因为 LruCache 在其构造函数中采用 int。
         */
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);

        /**
         * 将可用内存的 1/8 用于此内存缓存
         */
        final int cacheSize = maxMemory/8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            /**
             * 重写 sizeOf() 就是来计算一个元素的缓存的大小的，
             * 当存放的元素的总缓存大小大于 cacheSize 的话，
             * LruCache 就会删除最近最少使用的元素。
             * @param key
             * @param bitmap
             * @return
             */
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                Log.d("chan"," "+bitmap.getByteCount());
                Log.d("chan"," "+bitmap.getRowBytes()*bitmap.getHeight());
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap){
        if (getBitmapFromMemCache(key)==null){
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}


