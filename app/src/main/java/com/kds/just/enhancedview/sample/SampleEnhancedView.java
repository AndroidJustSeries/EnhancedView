package com.kds.just.enhancedview.sample;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.kds.just.enhancedview.view.EnhancedControl;
import com.kds.just.enhancedview.view.EnhancedTextView;

public class SampleEnhancedView extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SampleEnhancedView";
    public static final int TYPE_TEXTVIEW = 0;
    public static final int TYPE_EDITTEXT = 1;
    public static final int TYPE_CHECKBOX = 2;

    public static final String EXTRA_VIEW_TYPE = "EXTRA_VIEW_TYPE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type = TYPE_TEXTVIEW;
        if (getIntent() != null) {
            type = getIntent().getIntExtra(EXTRA_VIEW_TYPE,TYPE_TEXTVIEW);
        }
        if (type == TYPE_TEXTVIEW) {
            setContentView(R.layout.sample_enhanced_textview);
            findViewById(R.id.enhanced_view_01).setOnClickListener(this);
            findViewById(R.id.enhanced_view_02).setOnClickListener(this);
        } else if (type == TYPE_EDITTEXT) {
            setContentView(R.layout.sample_enhanced_edittext);
            findViewById(R.id.enhanced_view_01).setOnClickListener(this);
            findViewById(R.id.enhanced_view_02).setOnClickListener(this);
        } else if (type == TYPE_CHECKBOX) {
            setContentView(R.layout.sample_enhanced_checkbox);
        }

        setupProgrammaticallyUI();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.enhanced_view_01) {
            v.setSelected(!v.isSelected());
        } else if (id == R.id.enhanced_view_02) {
            v.setSelected(!v.isSelected());
        }

        if (v == mNormalEnhancedTextView) {
            v.setSelected(!v.isSelected());
            ((TextView)mNormalEnhancedTextView).setText(v.isSelected()?"Selected state":"Normal state");
        } else if (v == mSelectedEnhancedTextView) {
            v.setSelected(!v.isSelected());
            ((TextView)mSelectedEnhancedTextView).setText(v.isSelected()?"Selected state":"Normal state");
        } else if (v == mNormalBGButton) {
            showColorPicker(mNormalBGButton);
        } else if (v == mSelectedBGButton) {
            showColorPicker(mSelectedBGButton);
        } else if (v == mNormalStrokeButton) {
            showColorPicker(mNormalStrokeButton);
        } else if (v == mSelectedStrokeButton) {
            showColorPicker(mSelectedStrokeButton);
        } else if (v == mNormalTextColorButton) {
            showColorPicker(mNormalTextColorButton);
        } else if (v == mSelectedTextColorButton) {
            showColorPicker(mSelectedTextColorButton);
        }
    }

    private EnhancedControl mNormalEnhancedTextView;
    private EnhancedControl mSelectedEnhancedTextView;
    private Button mNormalBGButton;
    private Button mSelectedBGButton;
    private Button mNormalStrokeButton;
    private Button mSelectedStrokeButton;

    private SeekBar mStrokeWidthSeekBar;
    private SeekBar mCornerRadiusSeekBar;

    private Button mNormalTextColorButton;
    private Button mSelectedTextColorButton;
    private void setupProgrammaticallyUI() {
        mNormalEnhancedTextView = findViewById(R.id.enhanced_normal);
        mSelectedEnhancedTextView = findViewById(R.id.enhanced_selected);

        mNormalBGButton = findViewById(R.id.btn_normal_bg);
        mSelectedBGButton = findViewById(R.id.btn_selected_bg);
        mNormalStrokeButton = findViewById(R.id.btn_normal_stroke);
        mSelectedStrokeButton = findViewById(R.id.btn_selected_stroke);

        mStrokeWidthSeekBar = findViewById(R.id.seekbar_stroke_width);
        mCornerRadiusSeekBar = findViewById(R.id.seekbar_corner_radius);

        mNormalTextColorButton = findViewById(R.id.btn_normal_textcolor);
        mSelectedTextColorButton = findViewById(R.id.btn_selected_textcolor);

        ((View)mNormalEnhancedTextView).setOnClickListener(this);
        ((View)mSelectedEnhancedTextView).setOnClickListener(this);

        mNormalBGButton.setOnClickListener(this);
        mSelectedBGButton.setOnClickListener(this);
        mNormalStrokeButton.setOnClickListener(this);
        mSelectedStrokeButton.setOnClickListener(this);
        mNormalTextColorButton.setOnClickListener(this);
        mSelectedTextColorButton.setOnClickListener(this);

        mStrokeWidthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mNormalEnhancedTextView.setStrokeWidth(dp2px(progress));
                    mSelectedEnhancedTextView.setStrokeWidth(dp2px(progress));
                    ((TextView)findViewById(R.id.seekbar_stroke_width_tv)).setText("Stroke Width(" + progress + "dp)");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mCornerRadiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mNormalEnhancedTextView.setRoundRadius(dp2px(progress));
                    mSelectedEnhancedTextView.setRoundRadius(dp2px(progress));
                    ((TextView)findViewById(R.id.seekbar_corner_radius_tv)).setText("Corner Radius(" + progress + "dp)");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void showColorPicker(final View v) {
        ColorPickerDialog dialog = ColorPickerDialog.newBuilder().setDialogType(ColorPickerDialog.TYPE_PRESETS).create();
        dialog.setColorPickerDialogListener(new ColorPickerDialogListener() {
            @Override
            public void onColorSelected(int dialogId, int color) {
                if (v == mNormalBGButton) {
                    mNormalBGButton.setBackgroundColor(color);
                    mNormalEnhancedTextView.setBackgroundColor(color,mSelectedEnhancedTextView.getBackgroundSelectedColor());
                    mSelectedEnhancedTextView.setBackgroundColor(color,mSelectedEnhancedTextView.getBackgroundSelectedColor());
                } else if (v == mSelectedBGButton) {
                    mSelectedBGButton.setBackgroundColor(color);
                    mNormalEnhancedTextView.setBackgroundColor(mSelectedEnhancedTextView.getBackgroundNormalColor(),color);
                    mSelectedEnhancedTextView.setBackgroundColor(mSelectedEnhancedTextView.getBackgroundNormalColor(),color);
                } else if (v == mNormalStrokeButton) {
                    mNormalStrokeButton.setBackgroundColor(color);
                    mNormalEnhancedTextView.setStrokeColor(color,mNormalEnhancedTextView.getStrokeSelectedColor());
                    mSelectedEnhancedTextView.setStrokeColor(color,mSelectedEnhancedTextView.getStrokeSelectedColor());
                } else if (v == mSelectedStrokeButton) {
                    mSelectedStrokeButton.setBackgroundColor(color);
                    mNormalEnhancedTextView.setStrokeColor(mNormalEnhancedTextView.getStrokeNormalColor(),color);
                    mSelectedEnhancedTextView.setStrokeColor(mSelectedEnhancedTextView.getStrokeNormalColor(),color);
                } else if (v == mNormalTextColorButton) {
                    mNormalTextColorButton.setTextColor(color);
                    mNormalEnhancedTextView.setTextColor(color,mNormalEnhancedTextView.getTextSelectedColor());
                    mSelectedEnhancedTextView.setTextColor(color,mSelectedEnhancedTextView.getTextSelectedColor());
                } else if (v == mSelectedTextColorButton) {
                    mSelectedTextColorButton.setTextColor(color);
                    mNormalEnhancedTextView.setTextColor(mNormalEnhancedTextView.getTextNormalColor(),color);
                    mSelectedEnhancedTextView.setTextColor(mSelectedEnhancedTextView.getTextNormalColor(),color);
                }
            }

            @Override
            public void onDialogDismissed(int dialogId) {

            }
        });
        dialog.show(getSupportFragmentManager(), "color-picker-dialog");;
    }

    public static int dp2px(float dp) {
        Resources resources = Resources.getSystem();
        float px = dp * resources.getDisplayMetrics().density;
        return (int) Math.ceil(px);
    }
}
