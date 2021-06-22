package com.example.picturecache;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.Button;
import android.widget.ImageView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    ImageLoader loader;
    Button button, button1;
    ImageView imageView;
    String url = "https://www.planetware.com/wpimages/2020/02/france-in-pictures-beautiful-places-to-photograph-eiffel-tower.jpg";
    private static final int SUCCESS = 1;
    private static final int FAIL = 2;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    byte[] Picture = (byte[]) msg.obj;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(Picture, 0, Picture.length);
                    loader.addBitmapToMemoryCache("bitmap", bitmap);
                    imageView.setImageBitmap(bitmap);
                    break;
                case FAIL:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loader = new ImageLoader();
        button = findViewById(R.id.loading);
        imageView = findViewById(R.id.image1);
        button.setOnClickListener(v -> {
            Bitmap bitmap = getBitmapFromCache();
            //从缓存中获取bitmap
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                Log.d("chan", "===============getBitmapFromCacheOk");
                //缓存中存在
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getBitmapFromInternet();
                        //缓存中不存在，通过okhttp加载图片
                    }
                }).start();
            }
        });

        button1 = findViewById(R.id.trans);
        button1.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity3.class);
            startActivity(intent);
        });
    }

    private Bitmap getBitmapFromCache() {
        Log.d("chan", "===============getBitmapFromCache");
        return loader.getBitmapFromMemCache("bitmap");
    }

    private void getBitmapFromInternet() {
        Log.d("chan", "===============getBitmapFromInternet");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    byte[] Picture_bt = Objects.requireNonNull(response.body()).bytes();
                    Message message = handler.obtainMessage();
                    message.obj = Picture_bt;
                    message.what = SUCCESS;
                    handler.sendMessage(message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}