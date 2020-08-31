package com.kds.just.enhancedview.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.kds.just.enhancedview.view.EnhancedTextView;

public class ActMain extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        findViewById(R.id.sample_enhanced_textview).setOnClickListener(this);
        findViewById(R.id.sample_enhanced_edittext).setOnClickListener(this);
        findViewById(R.id.sample_enhanced_checkbox).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.sample_enhanced_textview) {
            Intent intent = new Intent(this,SampleEnhancedView.class);
            startActivity(intent);
        } else if (id == R.id.sample_enhanced_edittext) {
            Intent intent = new Intent(this,SampleEnhancedView.class);
            intent.putExtra(SampleEnhancedView.EXTRA_VIEW_TYPE,SampleEnhancedView.TYPE_EDITTEXT);
            startActivity(intent);
        } else if (id == R.id.sample_enhanced_checkbox) {
            Intent intent = new Intent(this,SampleEnhancedView.class);
            intent.putExtra(SampleEnhancedView.EXTRA_VIEW_TYPE,SampleEnhancedView.TYPE_CHECKBOX);
            startActivity(intent);
        }
    }

}
