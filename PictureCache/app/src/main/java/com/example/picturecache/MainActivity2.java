package com.example.picturecache;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.picturecache.utils.Pictures;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    Button BtCache,BtDisk,BtInternet;
    ImageView imageView;
    String path="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        BtCache = findViewById(R.id.btcache);
        BtDisk = findViewById(R.id.btdisk);
        BtInternet = findViewById(R.id.btinternet);
        imageView = findViewById(R.id.image2);
        BtCache.setOnClickListener(this);
        BtDisk.setOnClickListener(this);
        BtInternet.setOnClickListener(this);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btcache:
                Pictures.Memory(imageView,path);
                break;
            case R.id.btdisk:
                Pictures.Disk(imageView,path);
                break;
            case R.id.btinternet:
                Pictures.Internet(imageView,path);
                break;
            default:
                break;
        }
    }
}