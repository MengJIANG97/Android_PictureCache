package com.example.picturecache.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import java.io.IOException;
import java.util.Objects;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InternetCache {
    private final DiskCache diskCache;
    private final MemoryCache memoryCache;

    public InternetCache(DiskCache diskCache, Pictures pictures, MemoryCache memoryCache){
        this.diskCache = diskCache;
        this.memoryCache = memoryCache;
    }

    public void getBitmapFromNet(ImageView imageView, String url) {
        new BitmapTask().execute(imageView, url);
        //启动AsyncTask
    }


    /**
     * AsyncTask就是对handler和线程池的封装
     * 第一个泛型:参数类型
     * 第二个泛型:更新进度的泛型
     * 第三个泛型:onPostExecute的返回结果
     */
    @SuppressLint("StaticFieldLeak")
    class BitmapTask extends AsyncTask<Object,Void, Bitmap>{
        @SuppressLint("StaticFieldLeak")
        private ImageView image;
        private String path;

        @Override
        protected Bitmap doInBackground(Object... objects) {
            image = (ImageView)objects[0];
            path = (String)objects[1];
            return downLoadBitmap(path);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap!=null){
                image.setImageBitmap(bitmap);
                Log.d("cache_internet","从internet缓存了图片");
                diskCache.setBitmap(path,bitmap);
                //保存到disk中
                memoryCache.setBitmap(path,bitmap);
                //保存到cache中
            }
        }
    }

    /**
     * 网络下载图片
     * @param url
     * @return
     */
    private Bitmap downLoadBitmap(String url) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inSampleSize=2;
            //宽高压缩为原来的1/2
            options.inPreferredConfig=Bitmap.Config.ARGB_4444;
            Bitmap bitmap = BitmapFactory.decodeStream(Objects.requireNonNull(response.body()).byteStream(),null,options);
            options.inJustDecodeBounds = false;
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
