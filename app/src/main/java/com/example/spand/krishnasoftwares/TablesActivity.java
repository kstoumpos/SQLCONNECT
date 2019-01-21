package com.example.spand.krishnasoftwares;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class TablesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);

        Intent i = getIntent();
        int CatId = i.getIntExtra("catId", 0);
        Log.e("CatId: ", CatId+"");
        String catName = i.getStringExtra("catName");
        Log.e("catName: ", catName+"");
    }
}
