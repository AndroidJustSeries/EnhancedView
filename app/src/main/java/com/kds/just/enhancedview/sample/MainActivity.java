package com.kds.just.enhancedview.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.enhanced_textview_01).setOnClickListener(this);
        findViewById(R.id.enhanced_textview_02).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.enhanced_textview_01) {
            v.setSelected(!v.isSelected());
        }

    }
}
